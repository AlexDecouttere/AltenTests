package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setFirstname("Admin");
            adminUser.setEmail("admin@admin.com");
            adminUser.setPassword(new BCryptPasswordEncoder().encode("adminpassword"));
            userRepository.save(adminUser);
            System.out.println("Admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
