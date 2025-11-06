package com.mercury.pas.service;

import com.mercury.pas.model.dto.QuoteDtos;

import java.util.List;

public interface QuoteService {
    QuoteDtos.QuoteResponse generate(QuoteDtos.GenerateQuoteRequest request);
    QuoteDtos.QuoteResponse save(QuoteDtos.SaveQuoteRequest request);
    QuoteDtos.QuoteResponse getById(Long id);
    List<QuoteDtos.QuoteResponse> getByCustomer(Long customerId);
    Long convertToPolicy(Long quoteId, Long agentId);
}


