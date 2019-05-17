package com.junorz.travelbook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.junorz.travelbook.config.AppConfig.DefaultValue;
import com.junorz.travelbook.utils.JWTUtil;

import lombok.Data;

@Configuration
@EnableConfigurationProperties(DefaultValue.class)
public class AppConfig {

    @ConfigurationProperties
    @Data
    public class DefaultValue {
        private String currency;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    @Bean
    public LocalValidatorFactoryBean getValidator(MessageSource messageSource) {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }
    
    @Bean
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }

}
