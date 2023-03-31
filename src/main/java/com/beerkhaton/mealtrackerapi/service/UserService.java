package com.beerkhaton.mealtrackerapi.service;

import com.beerkhaton.mealtrackerapi.dto.input.PasswordInputDTO;
import com.beerkhaton.mealtrackerapi.dto.input.UserInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.BasicResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.EmployeeHistoryResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.LoginResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.input.LoginInputDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {


    LoginResponseDTO login(LoginInputDTO loginInputDTO, AuthenticationManager authenticationManager) throws Exception;

    BasicResponseDTO addUser(UserInputDTO dto);

    BasicResponseDTO fetchAllEmployee(int pageNo);

    BasicResponseDTO fetchEmployee(String id);

    BasicResponseDTO changePassword(String code, PasswordInputDTO dto);

    BasicResponseDTO readQrCode(String code);

    EmployeeHistoryResponseDTO fetchEmployeeOrderHistory(int pageNo);

    BasicResponseDTO fetchEmployeeOrderHistoryBetweenDates(String from, String to, int pageNo);


}
