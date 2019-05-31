package com.junorz.travelbook.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.junorz.travelbook.context.consts.Currency;
import com.junorz.travelbook.utils.CurrencyUtil;


@Service
public class CurrencyService {
    
    public List<String> getAvaliableCurrency() {
        Builder<String> listBuilder = ImmutableList.builder();
        Stream.of(Currency.values()).forEach(c -> listBuilder.add(c.toString()));
        return listBuilder.build();
    }
    
    public String getExchangeRate(String originalCurrency, String targetCurrency) {
        Currency original = Currency.valueOf(originalCurrency);
        Map<String, String> result = CurrencyUtil.getExchangeRate(original);
        return result.get(targetCurrency);
    }
    
}
