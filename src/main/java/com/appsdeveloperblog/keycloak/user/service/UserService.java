package com.appsdeveloperblog.keycloak.user.service;

import com.appsdeveloperblog.keycloak.user.controller.dto.UserDto;
import com.appsdeveloperblog.keycloak.user.controller.dto.VerifyPasswordResponse;
import com.appsdeveloperblog.keycloak.user.mapper.UserMapper;
import com.appsdeveloperblog.keycloak.user.model.User;
import com.appsdeveloperblog.keycloak.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    public UserDto getUser(String userName) {
        User user = findByUsername(userName);
        return mapper.toDto(user);
    }

    public VerifyPasswordResponse verifyPassword(String userName, String requestPassword) {
        User user = findByUsername(userName);

        VerifyPasswordResponse response = new VerifyPasswordResponse("It's not a match!");

        String databasePassword = user.getPassword();

        if (encoder.matches(requestPassword, databasePassword)) {
            response.setResult("It's a match!");
            return response;
        }

        return response;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private User findByUsername(String userName) {
        return userRepository.findByUsername(userName)
                .orElseThrow(() -> new EntityNotFoundException("Username: " + userName + " not found!"));
    }


}
