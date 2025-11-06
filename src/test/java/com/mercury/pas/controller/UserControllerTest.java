package com.mercury.pas.controller;

import com.mercury.pas.model.dto.UserDtos;
import com.mercury.pas.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    @Test
    void me_returnsUser() {
        UserService userService = mock(UserService.class);
        when(userService.getCurrentUser()).thenReturn(new UserDtos.UserResponse(1L,"a","b","e", null, null, null));
        UserController controller = new UserController(userService);
        ResponseEntity<UserDtos.UserResponse> resp = controller.me();
        assertThat(resp.getBody()).isNotNull();
        verify(userService, times(1)).getCurrentUser();
    }
}


