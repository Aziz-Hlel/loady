package com.technoshark.Loady.Users.Service;

import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.technoshark.Loady.Enums.RoleEnums;
import com.technoshark.Loady.Firebase.Service.FirebaseAuthService;
import com.technoshark.Loady.Users.DTO.UserResponse;
import com.technoshark.Loady.Users.Mapper.UserMapper;
import com.technoshark.Loady.Users.Model.User;
import com.technoshark.Loady.Users.Repositry.UsersRepo;
import com.technoshark.Loady.shared.Dto.CustomPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UsersRepo usersRepo;
    private final UserMapper userMapper;
    private final FirebaseAuthService firebaseService;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public CustomPage<UserResponse> searchUsers(String search, RoleEnums role, Pageable pageable) {
        Specification<User> spec = UserSpecification.filter(search, role);

        Page<UserResponse> users = usersRepo.findAll(spec, pageable)
                .map(userMapper::toDto);

        CustomPage<UserResponse> customPage = CustomPage.from(users);

        return customPage;
    }

}