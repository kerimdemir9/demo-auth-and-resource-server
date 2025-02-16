package com.auth.demo.authserver;

import com.auth.demo.authserver.data.UserModel;
import com.auth.demo.authserver.data.UserService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@SpringBootApplication
public class DemoAuthServerApplication implements CommandLineRunner {

    final UserService userService;
    final PasswordEncoder passwordEncoder;
    
    
    @Autowired
    public DemoAuthServerApplication(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoAuthServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            val user = userService.loadUserByUsername("admin");
            log.info("Admin user found: {}", user);
        } catch (Exception e) {
            log.info("Admin user not found, creating one");
            userService.saveUser(UserModel.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(List.of("ADMIN", "USER"))
                    .build());
        }
        
        try {
            val user = userService.loadUserByUsername("user");
            log.info("User user found: {}", user);
        } catch (Exception e) {
            log.info("User user not found, creating one");
            userService.saveUser(UserModel.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .role(List.of("USER"))
                    .build());
        }
    }
}