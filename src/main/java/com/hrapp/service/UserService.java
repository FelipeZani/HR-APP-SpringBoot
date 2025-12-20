package com.hrapp.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hrapp.domain.User;
import com.hrapp.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional(readOnly = true)
    public List<User> getRHAndDRHUsers() {
        
        return userRepository.findByRoleIn(List.of("RH", "DRH"));
    }

    @Transactional(readOnly = true)
    public List<User> getRHUsers() {
        
        return userRepository.findByRole("RH");
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public static String generateRandomPassword() {
        return java.util.UUID.randomUUID().toString().substring(0, 8); 
    }

    @Transactional
    public User registerNewUser(User user, String rawPassword, String recipientEmail) {
            String hashedPassword = passwordEncoder.encode(rawPassword);
            user.setPassword(hashedPassword);
            
            User savedUser = userRepository.save(user);

            
            emailService.sendNewUserCredentials(recipientEmail, user.getUsername(), rawPassword);
            
            return savedUser;
    }
}