package com.technoshark.Loady.Users.DTO;

import java.time.Instant;
import java.util.Date;

import com.technoshark.Loady.Enums.RoleEnums;

public record UserResponse(
        String id,
        String email,
        String username,
        RoleEnums role,
        Date createdAt) {

}
