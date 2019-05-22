package com.junorz.travelbook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.junorz.travelbook.config.SecurityConfig.JWTInfo;
import com.junorz.travelbook.context.security.JWTAuthenticationFilter;

import lombok.Data;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JWTInfo.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @ConfigurationProperties(prefix = "travelbook.security.jwt")
    @Data
    public class JWTInfo {
        private String privateKeyPath;
        private String publicKeyPath;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().cacheControl();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers("/api/travelbooks/**/delete").hasRole("ADMIN")
                .antMatchers("/api/travelbooks/**/edit").hasRole("ADMIN")
                .antMatchers("/api/travelbooks/members/**").hasRole("ADMIN")
                .antMatchers("/api/travelbooks/details/**/create").hasRole("ADMIN")
                .antMatchers("/api/travelbooks/details/**/edit").hasRole("ADMIN")
                .antMatchers("/api/travelbooks/details/**/delete").hasRole("ADMIN").anyRequest().permitAll();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

}
