package com.mercury.pas.service.impl;

import com.mercury.pas.model.dto.UserDtos;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDtos.UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return mapper.map(user, UserDtos.UserResponse.class);
    }

    @Override
    public UserDtos.UserResponse updateProfile(UserDtos.UpdateProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email().toLowerCase());
        user.setDob(request.dob());
        user.setLicenseNumber(request.licenseNumber());
        userRepository.save(user);
        return mapper.map(user, UserDtos.UserResponse.class);
    }

    @Override
    public List<UserDtos.UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(u -> mapper.map(u, UserDtos.UserResponse.class))
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}


