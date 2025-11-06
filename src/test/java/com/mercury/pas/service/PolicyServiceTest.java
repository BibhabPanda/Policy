package com.mercury.pas.service;

import com.mercury.pas.model.dto.PolicyDtos;
import com.mercury.pas.model.entity.Quote;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.entity.Vehicle;
import com.mercury.pas.model.enums.Role;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.QuoteRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.impl.PolicyServiceImpl;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PolicyServiceTest {
    @Test
    void create_policyFromQuote() {
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        QuoteRepository quoteRepository = mock(QuoteRepository.class);
        UserRepository userRepository = mock(UserRepository.class);

        Quote q = new Quote();
        q.setId(2L);
        q.setVehicle(new Vehicle());
        q.setCustomer(User.builder().id(3L).role(Role.CUSTOMER).build());
        q.setPremiumAmount(BigDecimal.valueOf(3500));
        when(quoteRepository.findById(2L)).thenReturn(Optional.of(q));
        when(userRepository.findById(10L)).thenReturn(Optional.of(User.builder().id(10L).role(Role.AGENT).build()));

        PolicyService service = new PolicyServiceImpl(policyRepository, quoteRepository, userRepository, new ModelMapper());
        var resp = service.create(new PolicyDtos.CreatePolicyRequest(2L,10L, LocalDate.now(), LocalDate.now().plusYears(1)));
        assertThat(resp.policyNumber()).isNotNull();
        verify(policyRepository).save(any());
    }
}


