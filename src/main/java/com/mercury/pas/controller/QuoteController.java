package com.mercury.pas.controller;

import com.mercury.pas.model.dto.QuoteDtos;
import com.mercury.pas.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@Validated
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping("/generate")
    public ResponseEntity<QuoteDtos.QuoteResponse> generate(@Valid @RequestBody QuoteDtos.GenerateQuoteRequest request) {
        return ResponseEntity.ok(quoteService.generate(request));
    }

    @PostMapping("/save")
    public ResponseEntity<QuoteDtos.QuoteResponse> save(@Valid @RequestBody QuoteDtos.SaveQuoteRequest request) {
        return ResponseEntity.ok(quoteService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuoteDtos.QuoteResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(quoteService.getById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<QuoteDtos.QuoteResponse>> byCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(quoteService.getByCustomer(customerId));
    }

    @PostMapping("/convert-to-policy/{quoteId}")
    @PreAuthorize("hasAnyRole('AGENT','ADMIN')")
    public ResponseEntity<Long> convert(@PathVariable Long quoteId, @RequestParam Long agentId) {
        return ResponseEntity.ok(quoteService.convertToPolicy(quoteId, agentId));
    }
}


