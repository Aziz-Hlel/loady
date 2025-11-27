package com.technoshark.Loady.Users.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.technoshark.Loady.Users.DTO.UserResponse;
import com.technoshark.Loady.Users.DTO.UserRequest;
import com.technoshark.Loady.Users.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", constant = "ADMIN")
    User toEntity(UserRequest userRequest);

    UserResponse toDto(User user);
}
