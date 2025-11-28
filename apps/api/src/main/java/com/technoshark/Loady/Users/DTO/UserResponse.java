package com.technoshark.Loady.Users.DTO;

import com.technoshark.Loady.Enums.RoleEnums;

public record UserResponse(
        String id,
        String email,
        String username,
        RoleEnums role,
        String avatar) {

}
