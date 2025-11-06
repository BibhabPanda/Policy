package com.mercury.pas.service;

import com.mercury.pas.model.dto.ClaimDtos;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.Role;
import com.mercury.pas.repository.ClaimRepository;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.impl.ClaimServiceImpl;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClaimServiceTest {
    @Test
    void file_claimCreatesNew() {
        ClaimRepository claimRepository = mock(ClaimRepository.class);
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        when(policyRepository.findById(1L)).thenReturn(Optional.of(new Policy()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder().id(2L).role(Role.CUSTOMER).build()));

        ClaimService service = new ClaimServiceImpl(claimRepository, policyRepository, userRepository, new ModelMapper());
        var resp = service.fileClaim(new ClaimDtos.FileClaimRequest(1L,2L,"desc"));
        assertThat(resp.claimNumber()).isNotNull();
        verify(claimRepository).save(any());
    }
}


