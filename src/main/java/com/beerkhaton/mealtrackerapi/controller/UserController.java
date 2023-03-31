package com.beerkhaton.mealtrackerapi.controller;


import com.beerkhaton.mealtrackerapi.dto.input.PasswordInputDTO;
import com.beerkhaton.mealtrackerapi.dto.input.UserInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.BasicResponseDTO;
import com.beerkhaton.mealtrackerapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController extends Controller{

    private final UserService userService;

    @PostMapping("/add")
    public BasicResponseDTO addUser(@RequestBody @Valid UserInputDTO dto) throws Exception {
        return updateHttpStatus(userService.addUser(dto));
    }


    @GetMapping("/all")
    public BasicResponseDTO fetchEmployees(@RequestParam("pageNo") int pageNo) throws Exception {
        return updateHttpStatus(userService.fetchAllEmployee(pageNo));
    }

    @GetMapping("")
    public BasicResponseDTO fetchEmployee(@PathVariable("id") String id) throws Exception {
        return updateHttpStatus(userService.fetchEmployee(id));
    }

    @GetMapping("/in_active")
    public BasicResponseDTO fetchInActiveEmployee(@RequestParam("pageNo") int pageNo) throws Exception {
        return updateHttpStatus(userService.fetchEmployeeWithInActiveStatus(pageNo));
    }

    @PatchMapping("/read_qrcode")
    public BasicResponseDTO readQRCode(@RequestParam("code") String code)  {
       return updateHttpStatus(userService.readQrCode(code));
    }


}
