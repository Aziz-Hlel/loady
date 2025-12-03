package com.technoshark.Loady.Seeders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.technoshark.Loady.Enums.RoleEnums;
import com.technoshark.Loady.Users.Model.User;
import com.technoshark.Loady.Users.Repositry.UsersRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SeedUser {

    private final UsersRepo usersRepo;

    public void seed() {

        List<User> usersToSeed = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {

            String seedEmail = "user" + i + "@example.com";
            String seedUsername = "user" + i;
            String seedId = UUID.randomUUID().toString();
            User user = User.builder()
                    .id(seedId)
                    .email(seedEmail)
                    .username(seedUsername)
                    .role(RoleEnums.ADMIN)
                    .build();
            Optional<User> existingUser = usersRepo.findByEmail(user.getEmail());
            if (existingUser.isEmpty()) {
                usersToSeed.add(user);
            }
        }
        usersRepo.saveAll(usersToSeed);
        log.info("Seeded users.");

    }
}