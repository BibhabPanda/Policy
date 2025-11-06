package com.mercury.pas.service;

import com.mercury.pas.model.dto.AuthDtos;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.Role;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.security.JwtService;
import com.mercury.pas.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Test
    void register_returnsToken() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        ModelMapper mapper = new ModelMapper();

        when(encoder.encode(any())).thenReturn("enc");
        when(jwtService.generateToken(any(), any())).thenReturn("token");

        AuthService service = new AuthServiceImpl(userRepository, encoder, authManager, jwtService, mapper);
        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("a","b","a@b.com","pass", Role.CUSTOMER,null,null);
        AuthDtos.AuthResponse resp = service.register(req);
        assertThat(resp.accessToken()).isEqualTo("token");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void resetPassword_updatesPassword() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        JwtService jwtService = mock(JwtService.class);
        ModelMapper mapper = new ModelMapper();

        User u = User.builder().email("x@y.com").password("old").role(Role.CUSTOMER).build();
        when(userRepository.findByEmail("x@y.com")).thenReturn(Optional.of(u));

        AuthService service = new AuthServiceImpl(userRepository, encoder, authManager, jwtService, mapper);
        service.resetPassword(new AuthDtos.ResetPasswordRequest("x@y.com","newpass"));
        verify(userRepository).save(any(User.class));
    }
}


