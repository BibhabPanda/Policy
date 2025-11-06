package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.dto.QuoteDtos;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.Quote;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.entity.Vehicle;
import com.mercury.pas.model.enums.PolicyStatus;
import com.mercury.pas.model.enums.QuoteStatus;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.QuoteRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.repository.VehicleRepository;
import com.mercury.pas.service.QuoteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final PolicyRepository policyRepository;
    private final ModelMapper mapper;

    public QuoteServiceImpl(QuoteRepository quoteRepository, VehicleRepository vehicleRepository, UserRepository userRepository, PolicyRepository policyRepository, ModelMapper mapper) {
        this.quoteRepository = quoteRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.policyRepository = policyRepository;
        this.mapper = mapper;
    }

    @Override
    public QuoteDtos.QuoteResponse generate(QuoteDtos.GenerateQuoteRequest request) {
        User customer = userRepository.findById(request.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findByVin(request.vin()).orElseGet(() -> {
            Vehicle v = Vehicle.builder()
                    .make(request.make())
                    .model(request.model())
                    .year(request.year())
                    .vin(request.vin())
                    .customer(customer)
                    .build();
            return vehicleRepository.save(v);
        });

        BigDecimal premium = calculatePremium(request.driverAge(), request.year());
        Quote quote = Quote.builder()
                .quoteNumber("MER-QUO-" + UUID.randomUUID())
                .vehicle(vehicle)
                .customer(customer)
                .premiumAmount(premium)
                .coverageDetails("Standard auto coverage")
                .status(QuoteStatus.GENERATED)
                .createdAt(OffsetDateTime.now())
                .build();
        quoteRepository.save(quote);
        return mapper.map(quote, QuoteDtos.QuoteResponse.class);
    }

    @Override
    public QuoteDtos.QuoteResponse save(QuoteDtos.SaveQuoteRequest request) {
        User customer = userRepository.findById(request.customerId()).orElseThrow(() -> new NotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findById(request.vehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));
        Quote quote = Quote.builder()
                .quoteNumber("MER-QUO-" + UUID.randomUUID())
                .vehicle(vehicle)
                .customer(customer)
                .premiumAmount(request.premiumAmount())
                .coverageDetails(request.coverageDetails())
                .status(QuoteStatus.SAVED)
                .createdAt(OffsetDateTime.now())
                .build();
        quoteRepository.save(quote);
        return mapper.map(quote, QuoteDtos.QuoteResponse.class);
    }

    @Override
    public QuoteDtos.QuoteResponse getById(Long id) {
        Quote quote = quoteRepository.findById(id).orElseThrow(() -> new NotFoundException("Quote not found"));
        return mapper.map(quote, QuoteDtos.QuoteResponse.class);
    }

    @Override
    public List<QuoteDtos.QuoteResponse> getByCustomer(Long customerId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
        return quoteRepository.findByCustomer(customer).stream()
                .map(q -> mapper.map(q, QuoteDtos.QuoteResponse.class))
                .toList();
    }

    @Override
    public Long convertToPolicy(Long quoteId, Long agentId) {
        Quote quote = quoteRepository.findById(quoteId).orElseThrow(() -> new NotFoundException("Quote not found"));
        User agent = userRepository.findById(agentId).orElseThrow(() -> new NotFoundException("Agent not found"));
        Policy policy = Policy.builder()
                .policyNumber("MER-POL-" + System.currentTimeMillis())
                .quote(quote)
                .vehicle(quote.getVehicle())
                .customer(quote.getCustomer())
                .agent(agent)
                .startDate(java.time.LocalDate.now())
                .endDate(java.time.LocalDate.now().plusYears(1))
                .premiumAmount(quote.getPremiumAmount())
                .status(PolicyStatus.ACTIVE)
                .build();
        policyRepository.save(policy);
        quote.setStatus(QuoteStatus.CONVERTED);
        quoteRepository.save(quote);
        return policy.getId();
    }

    private BigDecimal calculatePremium(int driverAge, int vehicleYear) {
        BigDecimal base = BigDecimal.valueOf(3000);
        BigDecimal premium = base;
        if (driverAge < 25) {
            premium = premium.add(base.multiply(BigDecimal.valueOf(0.20)));
        }
        int currentYear = java.time.Year.now().getValue();
        if (currentYear - vehicleYear > 10) {
            premium = premium.add(base.multiply(BigDecimal.valueOf(0.15)));
        }
        return premium;
    }
}


