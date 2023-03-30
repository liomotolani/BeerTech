package com.beerkhaton.mealtrackerapi.service;

import com.beerkhaton.mealtrackerapi.dto.enums.Gender;
import com.beerkhaton.mealtrackerapi.dto.enums.MealStatus;
import com.beerkhaton.mealtrackerapi.dto.enums.Status;
import com.beerkhaton.mealtrackerapi.dto.enums.UserRole;
import com.beerkhaton.mealtrackerapi.dto.input.UserInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.BasicResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.LoginResponseDTO;
import com.beerkhaton.mealtrackerapi.model.User;
import com.beerkhaton.mealtrackerapi.repository.UserRepository;
import com.beerkhaton.mealtrackerapi.util.GenericUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.beerkhaton.mealtrackerapi.config.TokenProvider;
import com.beerkhaton.mealtrackerapi.dto.input.LoginInputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());

            if(userOptional.isPresent()){
                return new BasicResponseDTO(Status.CONFLICT, "User with this email already exist");
            }

            String password = GenericUtil.generateAlphaNumeric(8);
            User user = getUser(dto, password);
            emailService.sendNewUserEmail(dto.getEmail(),dto.getName(),password);
            userRepository.save(user);
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
    public BasicResponseDTO changeUserStatus(String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(!userOptional.isPresent()){
            return new BasicResponseDTO(Status.NOT_FOUND, "User with this email doesn't exist");
        }
        User user = userOptional.get();
        user.setMealStatus(MealStatus.ACTIVE);
        userRepository.save(user);
        return new BasicResponseDTO(Status.SUCCESS);
    }

    @Override
    public BasicResponseDTO fetchAllEmployee(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        List<User> employees = userRepository.findByRole(UserRole.EMPLOYEE,pageable)
                .stream().collect(Collectors.toList());
        return new BasicResponseDTO(Status.SUCCESS, employees);
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


    private Collection<SimpleGrantedAuthority> getAuthority(User user){
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return authorities;
    }
}
