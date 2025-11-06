package com.mercury.pas.repository;

import com.mercury.pas.model.entity.Quote;
import com.mercury.pas.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    Optional<Quote> findByQuoteNumber(String quoteNumber);
    List<Quote> findByCustomer(User customer);
}


