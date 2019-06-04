package com.junorz.travelbook.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        return new BigDecimal(result.get(targetCurrency)).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
    
}
