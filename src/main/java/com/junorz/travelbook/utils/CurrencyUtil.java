package com.junorz.travelbook.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junorz.travelbook.context.consts.Currency;

/**
 * A currency exchange calculator
 */
public class CurrencyUtil {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyUtil.class);

    private static final String API_BASE = "https://api.exchangeratesapi.io/latest";
    private static final String API_EXCHANGE = API_BASE + "?base=";

    /**
     * Get an exchange rate list base on basic currency.
     * @param basicCurrency
     * @return An exchange rate list base on basic currency.
     */
    public static Map<String, String> getExchangeRate(Currency basicCurrency) {
        try (InputStream is = new URL(API_EXCHANGE + basicCurrency.toString()).openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            StringBuilder response = new StringBuilder();
            reader.lines().forEach(line -> response.append(line));
            
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory factory = mapper.getFactory();
            JsonParser parser = factory.createParser(response.toString());
            JsonNode actualObj = mapper.readTree(parser);
            
            JsonNode ratesNode = actualObj.get("rates");
            Map<String, String> result = new HashMap<>();
            Iterator<Entry<String, JsonNode>> it = ratesNode.fields();
            while(it.hasNext()) {
                Entry<String, JsonNode> entry = it.next();
                result.put(entry.getKey(), entry.getValue().asText());
            }
            return result;
        } catch(IOException e) {
            logger.error("Cannot fetch data from exchange APIs.");
            throw new RuntimeException(e);
        }
    }

    public static BigDecimal doExchange(Currency basicCurrency, Currency targetCurrency, String amount) {
        BigDecimal amountBigDecimal = new BigDecimal(amount);
        Map<String, String> exchangeRate = getExchangeRate(basicCurrency);
        BigDecimal targetExcahngeRate = new BigDecimal(exchangeRate.get(targetCurrency.toString()));
        return amountBigDecimal.multiply(targetExcahngeRate);
    }

}
