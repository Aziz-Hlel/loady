package com.technoshark.Loady.Users.Mapper;

import org.mapstruct.Mapper;

import com.technoshark.Loady.Users.DTO.UserResponse;
import com.technoshark.Loady.Users.Model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDto(User user);
}
