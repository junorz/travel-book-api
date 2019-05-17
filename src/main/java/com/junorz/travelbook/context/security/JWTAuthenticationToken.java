package com.junorz.travelbook.context.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -6153783419390426295L;
    private String travelBookId;
    
    public JWTAuthenticationToken(String travelBookId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.travelBookId = travelBookId;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return travelBookId;
    }
    
}
