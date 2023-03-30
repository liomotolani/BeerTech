package com.beerkhaton.mealtrackerapi.controller;


import com.beerkhaton.mealtrackerapi.dto.input.LoginInputDTO;
import com.beerkhaton.mealtrackerapi.dto.input.UserInputDTO;
import com.beerkhaton.mealtrackerapi.dto.output.BasicResponseDTO;
import com.beerkhaton.mealtrackerapi.dto.output.LoginResponseDTO;
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

    @PatchMapping("/{id}/status")
    public BasicResponseDTO updateMealStatus(@PathVariable("id") String id) throws Exception {
        return updateHttpStatus(userService.changeUserStatus(id));
    }

    @GetMapping("/all")
    public BasicResponseDTO fetchEmployees(@RequestParam("pageNo") int pageNo) throws Exception {
        return updateHttpStatus(userService.fetchAllEmployee(pageNo));
    }

    @GetMapping("")
    public BasicResponseDTO fetchEmployee(@PathVariable("id") String id) throws Exception {
        return updateHttpStatus(userService.fetchEmployee(id));
    }

}
