package com.appsdeveloperblog.keycloak.user.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPojo {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}