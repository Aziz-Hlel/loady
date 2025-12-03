package com.technoshark.Loady.Users.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.google.firebase.auth.FirebaseToken;
import com.technoshark.Loady.Users.DTO.UserProfileResponse;
import com.technoshark.Loady.Users.DTO.UserResponse;
import com.technoshark.Loady.Users.Model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    UserResponse toDto(User user);

    @Mapping(target = "avatar", source = "firebaseToken.picture")
    @Mapping(target = "email", source = "user.email")
    UserProfileResponse toUserProfileDto(User user, FirebaseToken firebaseToken);
}
