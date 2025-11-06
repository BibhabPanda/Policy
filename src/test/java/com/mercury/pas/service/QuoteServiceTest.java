package com.mercury.pas.service;

import com.mercury.pas.model.dto.QuoteDtos;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.Role;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.QuoteRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.repository.VehicleRepository;
import com.mercury.pas.service.implQuoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuoteServiceTest {
    @Test
    void generate_createsQuoteWithPremium() {
        QuoteRepository quoteRepository = mock(QuoteRepository.class);
        VehicleRepository vehicleRepository = mock(VehicleRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        PolicyRepository policyRepository = mock(PolicyRepository.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).email("c@x.com").role(Role.CUSTOMER).build()));

        QuoteService service = new com.mercury.pas.service.impl.QuoteServiceImpl(quoteRepository, vehicleRepository, userRepository, policyRepository, new ModelMapper());
        QuoteDtos.GenerateQuoteRequest req = new QuoteDtos.GenerateQuoteRequest(1L, "Toyota","Camry",2010,"VIN123",24);
        var resp = service.generate(req);
        assertThat(resp.premiumAmount()).isNotNull();
        verify(quoteRepository).save(any());
    }
}


