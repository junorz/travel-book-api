package com.junorz.travelbook.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.utils.CurrencyUtil;

@Service
public class CurrencyService {
    
    public String getExchangeRate(String originalCurrency, String targetCurrency) {
        Currency original = Currency.valueOf(originalCurrency);
        Map<String, String> result = CurrencyUtil.getExchangeRate(original);
        return result.get(targetCurrency);
    }
    
}
