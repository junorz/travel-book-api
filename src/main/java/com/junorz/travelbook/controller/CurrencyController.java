package com.junorz.travelbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.junorz.travelbook.context.consts.Messages;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.service.CurrencyService;
import com.junorz.travelbook.utils.ControllerUtil;

@RestController
@RequestMapping("/api/travelbooks/currency")
@CrossOrigin("*")
public class CurrencyController {
    
    private final CurrencyService currencyService;
    private final Response response;
    
    public CurrencyController(CurrencyService currencyService, Response response) {
        this.currencyService = currencyService;
        this.response = response;
    }
    
    @GetMapping("")
    public ResponseEntity<Response> getAvaliableCurrency() {
        return ControllerUtil.ok(response.of(currencyService.getAvaliableCurrency(), Messages.AVALIABLE_CURRENCY_FETCH_SUCCESS));
    }
    
    @GetMapping("/{originalCurrency}/{targetCurrency}")
    public ResponseEntity<Response> getExchangeRate(@PathVariable("originalCurrency") String originalCurrency, @PathVariable("targetCurrency") String targetCurrency) {
        String data = currencyService.getExchangeRate(originalCurrency, targetCurrency);
        return ControllerUtil.ok(response.of(data, Messages.EXCHANGE_RATE_FETCH_SUCCESS));
    }
    
}
