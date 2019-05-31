package com.junorz.travelbook.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.junorz.travelbook.config.AppConfig.DefaultValue;
import com.junorz.travelbook.context.ApplicationInfo;
import com.junorz.travelbook.context.ApplicationInfo.TokenInfo;
import com.junorz.travelbook.context.audit.AuditHandler;
import com.junorz.travelbook.context.response.Response;
import com.junorz.travelbook.utils.JWTUtil;
import com.junorz.travelbook.utils.MessageUtil;

import lombok.Data;

@Configuration
@EnableConfigurationProperties(DefaultValue.class)
@EnableScheduling
@EnableAspectJAutoProxy
public class AppConfig {

    public static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

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
    MessageUtil messageUtil() {
        return new MessageUtil(messageSource());
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

    @Bean
    @Scope(value = "application")
    public ApplicationInfo applicationInfo() {
        return new ApplicationInfo();
    }

    @Bean
    public Response response() {
        return new Response(messageSource());
    }
    
    @Bean
    public AuditHandler auditHandler() {
        return new AuditHandler(messageSource());
    }

    @Bean
    public Hibernate5Module jsonHibernate5Lazy() {
        return new Hibernate5Module();
    }

    // clear token histories every 1 hour
    @Scheduled(cron = "0 0 * * * ?")
    public void clearTokenHistories() {
        ApplicationInfo applicationInfo = applicationInfo();
        logger.info("Start to clear token historires.");
        synchronized (applicationInfo) {
            Iterator<Entry<String, TokenInfo>> it = applicationInfo.getTokenHistories().entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, TokenInfo> entry = it.next();
                LocalDateTime expiredDateTime = entry.getValue().getExpireDateTime();
                if (Duration.between(expiredDateTime, LocalDateTime.now()).getSeconds() > 24 * 3600) {
                    logger.info("token: {}, has been removed from token histories.", entry.getKey());
                    it.remove();
                }
            }
        }
        logger.info("Tokens' clear task finished.");
    }

}
