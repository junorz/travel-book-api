package com.junorz.travelbook.utils;

import org.junit.Test;

import com.junorz.travelbook.context.consts.Currency;

public class CurrencyUtilTest {

    
    @Test
    public void getExchangeRateTest() {
        System.out.println(CurrencyUtil.getExchangeRate(Currency.CNY));
    }
    
    @Test
    public void doExchangeTest() {
        System.out.println(CurrencyUtil.doExchange(Currency.CNY, Currency.JPY, "2000"));
    }
}
