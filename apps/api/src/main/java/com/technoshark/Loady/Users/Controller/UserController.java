package com.technoshark.Loady.Users.Controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.technoshark.Loady.Enums.RoleEnums;
import com.technoshark.Loady.Users.DTO.UserResponse;
import com.technoshark.Loady.Users.Service.UserService;
import com.technoshark.Loady.Utils.ApiResponse;
import com.technoshark.Loady.shared.Dto.CustomPage;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping({ "", "/" })
    public ResponseEntity<ApiResponse<CustomPage<UserResponse>>> users(@RequestParam(required = false) String search,
            @RequestParam(required = false) RoleEnums role,
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var users = userService.searchUsers(search, role, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CustomPage<UserResponse>>builder()
                        .message("Users fetched successfully")
                        .data(users)
                        .status(HttpStatus.OK.value())
                        .build());
    }

}
