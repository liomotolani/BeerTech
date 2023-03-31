package com.beerkhaton.mealtrackerapi.service;

import com.beerkhaton.mealtrackerapi.cache.ManualCacheHandler;
import com.beerkhaton.mealtrackerapi.dto.enums.Gender;
import com.beerkhaton.mealtrackerapi.dto.enums.MealStatus;
import com.beerkhaton.mealtrackerapi.dto.enums.Status;
import com.beerkhaton.mealtrackerapi.dto.enums.UserRole;
import com.beerkhaton.mealtrackerapi.dto.input.PasswordInputDTO;
import com.beerkhaton.mealtrackerapi.dto.input.UserInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.BasicResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.EmployeeHistoryResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.LoginResponseDTO;
import com.beerkhaton.mealtrackerapi.model.User;
import com.beerkhaton.mealtrackerapi.model.UserHistory;
import com.beerkhaton.mealtrackerapi.repository.UserHistoryRepository;
import com.beerkhaton.mealtrackerapi.repository.UserRepository;
import com.beerkhaton.mealtrackerapi.util.DateUtil;
import com.beerkhaton.mealtrackerapi.util.GenericUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.beerkhaton.mealtrackerapi.config.TokenProvider;
import com.beerkhaton.mealtrackerapi.dto.input.LoginInputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final TokenProvider tokenProvider;

    private final ManualCacheHandler manualCacheHandler;

    private final ObjectMapper mapper;

    private final UserHistoryRepository userHistoryRepository;

    @Value("${qr.code.message}")
    private String code;

    private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder(12);
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), getAuthority(user));
    }


    @Override
    public LoginResponseDTO login(LoginInputDTO dto, AuthenticationManager authenticationManager) throws Exception {

        try{
            Optional<User> user = userRepository.findByEmail(dto.getEmail());

            if(!user.isPresent()){
                return new LoginResponseDTO(Status.NOT_FOUND, "User doesn't exist");
            }

            if(!new BCryptPasswordEncoder(12).matches(dto.getPassword(), user.get().getPassword())){
                return new LoginResponseDTO(Status.BAD_REQUEST, "Incorrect Password");
            }
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.get().getName(),
                            dto.getPassword(),
                            getAuthority(user.get())
                    )
            );

            log.info("{}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User userObject = user.get();

            String token = tokenProvider.generateJWTToken(authentication);
            userObject.setLastLoginDate(new Date());
            userRepository.save(userObject);
            return new LoginResponseDTO(Status.SUCCESS,token,userObject);
        } catch (Exception ex){

            return new LoginResponseDTO(Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @Override
    public BasicResponseDTO addUser(UserInputDTO dto) {

        try{

            Optional<User> admin = userRepository.findByEmail(tokenProvider.getEmail());

            User adminUser = admin.get();

            if(!isAdmin(adminUser)){
                return new BasicResponseDTO(Status.FORBIDDEN);
            }

            Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

            if(userOptional.isPresent()){
                return new BasicResponseDTO(Status.CONFLICT, "User with this email already exist");
            }

            String code = GenericUtil.generateAlphaNumeric(12);
            String password = GenericUtil.generateAlphaNumeric(8);
            User user = getUser(dto, password);
            emailService.sendNewUserEmail(dto.getEmail(),dto.getName(),password);
            userRepository.save(user);
            manualCacheHandler.addToCache(code,user.getEmail());
            return new BasicResponseDTO(Status.SUCCESS, user);
        } catch(Exception ex) {
            return new BasicResponseDTO(Status.INTERNAL_ERROR, ex.getMessage());
        }
    }



    private User getUser(UserInputDTO dto, String password) {
        return User.builder()
                .name(dto.getName())
                .staffId(GenericUtil.generateStaffId())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .role(UserRole.EMPLOYEE)
                .mealStatus(MealStatus.ACTIVE)
                .createdDate(new Date())
                .gender(Gender.valueOf(dto.getGender().toUpperCase()))
                .password(bcryptEncoder.encode(password))
                .build();
    }

    @Override
    public BasicResponseDTO fetchAllEmployee(int pageNo) {

        User user = userRepository.findByEmail(tokenProvider.getEmail()).get();
        if(!isAdmin(user)){
            return new BasicResponseDTO(Status.FORBIDDEN);
        }

        Pageable pageable = getPageable(pageNo);
        List<User> employees = userRepository.findByRole(UserRole.EMPLOYEE,pageable)
                .stream().collect(Collectors.toList());
        return new BasicResponseDTO(Status.SUCCESS, employees);
    }

    private Pageable getPageable(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        return pageable;
    }

    @Override
    public BasicResponseDTO fetchEmployee(String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(!userOptional.isPresent()){
            return new BasicResponseDTO(Status.NOT_FOUND, "User with this email doesn't exist");
        }
        User user = userOptional.get();
        return new BasicResponseDTO(Status.SUCCESS, user);
    }

    @Override
    public BasicResponseDTO changePassword(String code, PasswordInputDTO dto) {

        String email = manualCacheHandler.getFromCache(code);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if(!userOptional.isPresent()){
            return new BasicResponseDTO(Status.NOT_FOUND,"User doesn't exist");
        }

        if(!dto.getPassword().equals(dto.getConfirmPassword())){
            return new BasicResponseDTO(Status.BAD_REQUEST, "Password doesn't match");
        }

        User user = userOptional.get();

        user.setPassword(bcryptEncoder.encode(dto.getPassword()));

        user.setLastLoginDate(new Date());

        userRepository.save(user);

        manualCacheHandler.removeCache(code);

        return new BasicResponseDTO(Status.SUCCESS);

    }

    @Override
    public BasicResponseDTO readQrCode(String text) {
        try{
            Optional<User> userOptional = userRepository.findByEmail(tokenProvider.getEmail());

            User user = userOptional.get();

            if(isAdmin(user)){
                return new BasicResponseDTO(Status.FORBIDDEN);
            }

            if(!text.equals(code)){
                return new BasicResponseDTO(Status.BAD_REQUEST, "Invalid QR code");
            }

            user.setMealStatus(MealStatus.INACTIVE);
            userRepository.save(user);
            UserHistory userHistory = mapper.convertValue(user,UserHistory.class);
            userHistoryRepository.save(userHistory);
            return new BasicResponseDTO(Status.SUCCESS, user);
        }catch (Exception ex){
            return new BasicResponseDTO(Status.BAD_REQUEST, ex.getMessage());
        }
    }

    @Override
    public EmployeeHistoryResponseDTO fetchEmployeeOrderHistory(int pageNo) {
        User user = userRepository.findByEmail(tokenProvider.getEmail()).get();
        if(!isAdmin(user)){
            return new EmployeeHistoryResponseDTO(Status.FORBIDDEN);
        }
        Pageable pageable = getPageable(pageNo);
        Date dateTo = new Date();
        Date dateFrom = DateUtil.subtractDays(dateTo, 1);
        List<UserHistory> employees = userHistoryRepository
                .findByCreatedDateBetween(dateFrom, dateTo,pageable).toList();
        long count = countTickeHistory(dateFrom,dateTo);
        return new EmployeeHistoryResponseDTO(Status.SUCCESS,employees,count);
    }

    private long countTickeHistory(Date from, Date to) {
        return userHistoryRepository.findUserHistoryByCreatedDateBetween(from,to).stream().count();
    }

    @Override
    public BasicResponseDTO fetchEmployeeOrderHistoryBetweenDates(String from, String to, int pageNo) {
        Date dateFrom = DateUtil.dateFullFormat(from);
        Date dateTo = DateUtil.dateFullFormat(to);
        Pageable pageable = getPageable(pageNo);
        List<UserHistory> employees = userHistoryRepository
                .findByCreatedDateBetween(dateFrom, dateTo,pageable).toList();
        return new BasicResponseDTO(Status.SUCCESS,employees);
    }


    private boolean isAdmin(User user){
        return user.getRole().equals(UserRole.ADMIN);
    }


    private Collection<SimpleGrantedAuthority> getAuthority(User user){
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return authorities;
    }
}
