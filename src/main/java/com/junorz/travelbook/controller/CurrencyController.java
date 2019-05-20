package com.junorz.travelbook.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.context.response.Status;
import com.junorz.travelbook.service.CurrencyService;

@RestController
@RequestMapping("/travelbooks/currency")
@CrossOrigin("*")
public class CurrencyController {
    
    private final CurrencyService currencyService;
    private final MessageSource messageSource;
    
    public CurrencyController(CurrencyService currencyService, MessageSource messageSource) {
        this.currencyService = currencyService;
        this.messageSource = messageSource;
    }
    
    @GetMapping("/{originalCurrency}/{targetCurrency}")
    public ResponseEntity<Response> getExchangeRate(@PathVariable("originalCurrency") String originalCurrency, @PathVariable("targetCurrency") String targetCurrency) {
        String data = currencyService.getExchangeRate(originalCurrency, targetCurrency);
        String message = messageSource.getMessage(Messages.EXCHANGE_RATE_FETCH_SUCCESS, null, Locale.getDefault());
        return ResponseEntity.status(HttpStatus.OK).body(Response.of(data, message, Status.SUCCESS));
    }
    
}
