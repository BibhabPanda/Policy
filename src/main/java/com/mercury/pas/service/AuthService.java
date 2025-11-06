package com.mercury.pas.service;

import com.mercury.pas.model.dto.AuthDtos;
import com.mercury.pas.model.dto.UserDtos;

public interface AuthService {
    AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request);
    AuthDtos.AuthResponse login(AuthDtos.LoginRequest request);
    void resetPassword(AuthDtos.ResetPasswordRequest request);
    UserDtos.UserResponse me();
}


