package com.mercury.pas.service;

import com.mercury.pas.model.dto.UserDtos;

import java.util.List;

public interface UserService {
    UserDtos.UserResponse getCurrentUser();
    UserDtos.UserResponse updateProfile(UserDtos.UpdateProfileRequest request);
    List<UserDtos.UserResponse> getAll();
    void deleteById(Long id);
}


