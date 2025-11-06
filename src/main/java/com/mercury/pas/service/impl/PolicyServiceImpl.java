package com.mercury.pas.service.impl;

import com.mercury.pas.exception.NotFoundException;
import com.mercury.pas.model.dto.PolicyDtos;
import com.mercury.pas.model.entity.Policy;
import com.mercury.pas.model.entity.Quote;
import com.mercury.pas.model.entity.User;
import com.mercury.pas.model.enums.PolicyStatus;
import com.mercury.pas.repository.PolicyRepository;
import com.mercury.pas.repository.QuoteRepository;
import com.mercury.pas.repository.UserRepository;
import com.mercury.pas.service.PolicyService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {
    private final PolicyRepository policyRepository;
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public PolicyServiceImpl(PolicyRepository policyRepository, QuoteRepository quoteRepository, UserRepository userRepository, ModelMapper mapper) {
        this.policyRepository = policyRepository;
        this.quoteRepository = quoteRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public PolicyDtos.PolicyResponse create(PolicyDtos.CreatePolicyRequest request) {
        Quote quote = quoteRepository.findById(request.quoteId()).orElseThrow(() -> new NotFoundException("Quote not found"));
        User agent = userRepository.findById(request.agentId()).orElseThrow(() -> new NotFoundException("Agent not found"));
        Policy policy = Policy.builder()
                .policyNumber("MER-POL-" + System.currentTimeMillis())
                .quote(quote)
                .vehicle(quote.getVehicle())
                .customer(quote.getCustomer())
                .agent(agent)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .premiumAmount(quote.getPremiumAmount())
                .status(PolicyStatus.ACTIVE)
                .build();
        policyRepository.save(policy);
        return mapper.map(policy, PolicyDtos.PolicyResponse.class);
    }

    @Override
    public PolicyDtos.PolicyResponse getById(Long id) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new NotFoundException("Policy not found"));
        return mapper.map(policy, PolicyDtos.PolicyResponse.class);
    }

    @Override
    public List<PolicyDtos.PolicyResponse> getByCustomer(Long customerId) {
        User customer = userRepository.findById(customerId).orElseThrow(() -> new NotFoundException("Customer not found"));
        return policyRepository.findByCustomer(customer).stream().map(p -> mapper.map(p, PolicyDtos.PolicyResponse.class)).toList();
    }

    @Override
    public List<PolicyDtos.PolicyResponse> getByAgent(Long agentId) {
        User agent = userRepository.findById(agentId).orElseThrow(() -> new NotFoundException("Agent not found"));
        return policyRepository.findByAgent(agent).stream().map(p -> mapper.map(p, PolicyDtos.PolicyResponse.class)).toList();
    }

    @Override
    public PolicyDtos.PolicyResponse update(Long id, PolicyDtos.CreatePolicyRequest request) {
        Policy policy = policyRepository.findById(id).orElseThrow(() -> new NotFoundException("Policy not found"));
        policy.setStartDate(request.startDate());
        policy.setEndDate(request.endDate());
        policyRepository.save(policy);
        return mapper.map(policy, PolicyDtos.PolicyResponse.class);
    }

    @Override
    public void delete(Long id) {
        policyRepository.deleteById(id);
    }
}


