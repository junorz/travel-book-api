package com.junorz.travelbook.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.junorz.travelbook.context.dto.TravelBookCreateDto;
import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.context.orm.TxManager;
import com.junorz.travelbook.context.security.JWTAuthenticationToken;
import com.junorz.travelbook.domain.TravelBook;
import com.junorz.travelbook.utils.JWTUtil;

@Service
public class TravelBookService {

    private final Repository rep;
    private final PlatformTransactionManager txm;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public TravelBookService(Repository rep, PlatformTransactionManager txm, PasswordEncoder passwordEncoder,
            JWTUtil jwtUtil) {
        this.rep = rep;
        this.txm = txm;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public List<TravelBook> findAll() {
        return TxManager.of(txm).tx(() -> TravelBook.findAll(rep));
    }

    public TravelBook findByUrl(String url) {
        return TxManager.of(txm).tx(() -> TravelBook.findByUrl(url, rep));
    }

    public TravelBook create(TravelBookCreateDto dto) {
        // encrypt the password
        dto.setAdminPassword(passwordEncoder.encode(dto.getAdminPassword()));
        return TxManager.of(txm).tx(() -> TravelBook.create(dto, rep));
    }
    
    public TravelBook edit(String id, String name, String adminPassword, String currency) {
        // skip password encoding if new password is not set.
        String encodedAdminPassword = StringUtils.isNotBlank(adminPassword) ? passwordEncoder.encode(adminPassword) : null;
        return TxManager.of(txm).tx(() -> TravelBook.edit(id, name, encodedAdminPassword, currency, rep));
    }

    public TravelBook delete(String id) {
        return TxManager.of(txm).tx(() -> TravelBook.delete(id, rep));
    }

    public String login(String id, String password) {
        TravelBook travelBook = TxManager.of(txm).tx(() -> TravelBook.findById(id, rep));
        if (travelBook == null) {
            return null;
        }
        boolean isPasswordMatches = passwordEncoder.matches(password, travelBook.getAdminPassword());
        if (isPasswordMatches) {
            String token = jwtUtil.generateToken(id);
            Authentication authentication = new JWTAuthenticationToken(id,
                    AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return token;
        }
        return null;
    }
}
