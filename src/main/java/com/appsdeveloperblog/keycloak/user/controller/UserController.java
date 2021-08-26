package com.appsdeveloperblog.keycloak.user.controller;

import com.appsdeveloperblog.keycloak.user.controller.dto.UserDto;
import com.appsdeveloperblog.keycloak.user.controller.dto.PasswordMatchRequestDto;
import com.appsdeveloperblog.keycloak.user.service.UserService;
import com.appsdeveloperblog.keycloak.user.controller.dto.VerifyPasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userName}")
    public UserDto getUser(@PathVariable String userName) {
        return userService.getUser(userName);
    }

    @PostMapping("/{userName}/verify-password")
    public VerifyPasswordResponse verifyPassword(@PathVariable String userName, @RequestBody String password) {
        return userService.verifyPassword(userName, password);
    }
}