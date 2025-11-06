package com.mercury.pas.controller;

import com.mercury.pas.model.dto.QuoteDtos;
import com.mercury.pas.service.QuoteService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QuoteControllerTest {
    @Test
    void generate_returnsQuote() {
        QuoteService quoteService = mock(QuoteService.class);
        when(quoteService.generate(any())).thenReturn(new QuoteDtos.QuoteResponse(1L,"Q",1L,1L, java.math.BigDecimal.TEN, "c", com.mercury.pas.model.enums.QuoteStatus.GENERATED, java.time.OffsetDateTime.now()));
        QuoteController controller = new QuoteController(quoteService);
        var resp = controller.generate(new QuoteDtos.GenerateQuoteRequest(1L,"m","m",2020,"vin",30));
        assertThat(resp.getBody()).isNotNull();
        verify(quoteService).generate(any());
    }
}


