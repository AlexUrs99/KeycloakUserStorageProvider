package com.appsdeveloperblog.keycloak.user.mapper;

import com.appsdeveloperblog.keycloak.user.controller.dto.UserDto;
import com.appsdeveloperblog.keycloak.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}