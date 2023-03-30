package com.beerkhaton.mealtrackerapi.controller;

import com.beerkhaton.mealtrackerapi.dto.input.LoginInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.LoginResponseDTO;
import com.beerkhaton.mealtrackerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController extends Controller {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginInputDTO dto) throws Exception {
        return updateHttpStatus(userService.login(dto,authenticationManager));
    }

}
