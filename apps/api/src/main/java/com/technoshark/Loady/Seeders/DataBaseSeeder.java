
package com.technoshark.Loady.Seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
public class DataBaseSeeder implements CommandLineRunner {

    private final SeedUser seedUser;
    private final ProdSeeder prodSeeder;

    @Override
    public void run(String... args) throws Exception {

        log.info("Seeding database...");

        prodSeeder.seed();
        seedUser.seed();

    }

}
