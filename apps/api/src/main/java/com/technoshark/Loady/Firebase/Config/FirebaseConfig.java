package com.technoshark.Loady.Firebase.Config;

import java.io.ByteArrayInputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.technoshark.Loady.Utils.AppProperties;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final AppProperties appProperties;

    @PostConstruct // * only executes after dependencies are injected
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {

                String json = appProperties.firebaseServiceAccount();
                var inputStream = new ByteArrayInputStream(json.getBytes());
                // var inputStream = json.getBytes();
                // FileInputStream serviceAccount = new FileInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(inputStream))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase initialized.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }
}
