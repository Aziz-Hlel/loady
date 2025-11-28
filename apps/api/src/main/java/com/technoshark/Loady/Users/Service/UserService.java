package com.technoshark.Loady.Users.Service;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.google.firebase.auth.FirebaseToken;
import com.technoshark.Loady.Enums.RoleEnums;
import com.technoshark.Loady.ErrorHandler.Exceptions.ResourceNotFoundException;
import com.technoshark.Loady.Firebase.Service.FirebaseAuthService;
import com.technoshark.Loady.Users.DTO.UserResponse;
import com.technoshark.Loady.Users.Mapper.UserMapper;
import com.technoshark.Loady.Users.Model.User;
import com.technoshark.Loady.Users.Repositry.UsersRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UsersRepo usersRepo;
    private final UserMapper userMapper;
    private final FirebaseAuthService firebaseService;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public UserResponse createUser(FirebaseToken firebaseToken) {

        if (usersRepo.existsByEmail(firebaseToken.getEmail())) {
            throw new IllegalArgumentException("User with email " + firebaseToken.getEmail() + " already exists.");
        }

        String userId = firebaseToken.getUid();

        User userEntity = User.builder()
                .id(userId)
                .username(firebaseToken.getName())
                .email(firebaseToken.getEmail())
                .signInProvider(getSignInProvider(firebaseToken))
                .role(RoleEnums.ADMIN)
                .build();

        // User userEntity = userMapper.toEntity(userRequest);

        // userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        User savedUser = usersRepo.save(userEntity);

        firebaseService.setCustomClaims(userId.toString(), savedUser.getRole(), null);

        UserResponse userResponse = userMapper.toDto(savedUser, firebaseToken);
        return userResponse;
    }

    public UserResponse login(FirebaseToken firebaseToken) {

        String userEmail = firebaseToken.getEmail();
        User user = usersRepo.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User with email not found."));

        var claims = firebaseToken.getClaims();

        if (claims == null || !claims.get("role").toString().equals(user.getRole().name())) {
            // TODO : set other claims like schoolId
            firebaseService.setCustomClaims(user.getId(), user.getRole(), null);
        }
        UserResponse userResponse = userMapper.toDto(user, firebaseToken);
        return userResponse;
    }

    public UserResponse addUser(FirebaseToken firebaseToken) {

        String userId = firebaseToken.getUid();

        User userEntity = User.builder()
                .id(userId)
                .username(firebaseToken.getName())
                .email(firebaseToken.getEmail())
                .signInProvider(getSignInProvider(firebaseToken))
                .role(RoleEnums.ADMIN)
                .build();

        firebaseService.setCustomClaims(userId, userEntity.getRole(), null);

        User savedUser = usersRepo.save(userEntity);

        UserResponse userResponse = userMapper.toDto(savedUser, firebaseToken);
        return userResponse;
    }

    @SuppressWarnings("unchecked")
    public String getSignInProvider(FirebaseToken firebaseToken) {
        var claims = firebaseToken.getClaims();
        Map<String, Object> firebaseMap = (Map<String, Object>) claims.get("firebase");
        String signInProvider = firebaseMap.get("sign_in_provider").toString();
        return signInProvider;
    }

    public UserResponse authenticateWithProvider(FirebaseToken firebaseToken) {
        String userEmail = firebaseToken.getEmail();
        Optional<User> user = usersRepo.findByEmail(userEmail);
        if (user.isEmpty()) {

            return this.addUser(firebaseToken);
        }

        var claims = firebaseToken.getClaims();
        String userRole = user.get().getRole().name();
        Object tokenRole = claims.get("role");
        boolean missingOrOutdatedRole = (tokenRole == null) || !tokenRole.equals(userRole);

        if (missingOrOutdatedRole) {
            // TODO : set other claims like schoolId
            firebaseService.setCustomClaims(user.get().getId(), user.get().getRole(), null);
        }

        UserResponse userResponse = userMapper.toDto(user.get(), firebaseToken);

        return userResponse;
    }

    public UserResponse getCurrentUser(FirebaseToken firebaseToken) {
        String userId = firebaseToken.getUid();
        logger.info("Getting current user with ID: " + userId);
        User user = usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id not found."));

        UserResponse userResponse = userMapper.toDto(user, firebaseToken);
        return userResponse;
    }

}
