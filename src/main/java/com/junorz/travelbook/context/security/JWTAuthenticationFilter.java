package com.junorz.travelbook.context.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.junorz.travelbook.utils.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    @Autowired
    public void setJwtUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Front-end can get the token from header after setting
        // Access-Control-Expose-Header
        response.setHeader("Access-Control-Expose-Headers", "token, travelBookId");

        // Get token and travel book's ID from header
        String token = request.getHeader("token");
        String travelBookId = request.getHeader("travelBookId");

        if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(travelBookId)) {
            // start authentication, and set JWTAuthenticationToken to SecurityContext
            Jws<Claims> jws = jwtUtil.parseToken(token);
            String travelBookIdInToken = (String) jws.getBody().get("travelBookId");
            if (travelBookId.equals(travelBookIdInToken)) {
                JWTAuthenticationToken authentication = new JWTAuthenticationToken(travelBookId,
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);

    }

}
