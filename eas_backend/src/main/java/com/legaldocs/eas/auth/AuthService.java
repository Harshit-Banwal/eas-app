package com.legaldocs.eas.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.legaldocs.eas.auth.dto.LoginRequest;
import com.legaldocs.eas.auth.dto.SignupRequest;
import com.legaldocs.eas.auth.jwt.JwtUtil;

@Service
public class AuthService {
	
	private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }
    
    public void signup(SignupRequest req) {

        userRepo.findByEmail(req.email())
            .ifPresent(u -> {
                throw new RuntimeException("Email already registered");
            });

        User user = new User();
        user.setEmail(req.email());
        user.setPasswordHash(encoder.encode(req.password()));

        userRepo.save(user);
    }
    
    public String login(LoginRequest req) {

        User user = userRepo.findByEmail(req.email())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(req.password(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return JwtUtil.generateToken(user.getId(), user.getEmail());
    }
}
