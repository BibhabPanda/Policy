package com.mercury.pas.config;

import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.Role;
import com.mercury.pas.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByEmail("admin@mercury.com").isEmpty()) {
                userRepository.save(User.builder()
                        .firstName("System")
                        .lastName("Admin")
                        .email("admin@mercury.com")
                        .password(encoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .build());
            }
            if (userRepository.findByEmail("agent@mercury.com").isEmpty()) {
                userRepository.save(User.builder()
                        .firstName("Default")
                        .lastName("Agent")
                        .email("agent@mercury.com")
                        .password(encoder.encode("Agent@123"))
                        .role(Role.AGENT)
                        .build());
            }
            if (userRepository.findByEmail("customer@mercury.com").isEmpty()) {
                userRepository.save(User.builder()
                        .firstName("Demo")
                        .lastName("Customer")
                        .email("customer@mercury.com")
                        .password(encoder.encode("Customer@123"))
                        .role(Role.CUSTOMER)
                        .build());
            }
        };
    }
}


