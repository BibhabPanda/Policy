package com.mercury.pas.service.impl;

import com.mercury.pas.model.dto.AuthDtos;
import com.mercury.pas.model.dto.UserDtos;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.security.JwtService;
import com.mercury.pas.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ModelMapper mapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @Override
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .dob(request.dob())
                .licenseNumber(request.licenseNumber())
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));
        return new AuthDtos.AuthResponse(token, "Bearer");
    }

    @Override
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));
        return new AuthDtos.AuthResponse(token, "Bearer");
    }

    @Override
    public void resetPassword(AuthDtos.ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow();
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDtos.UserResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return mapper.map(user, UserDtos.UserResponse.class);
    }
}


