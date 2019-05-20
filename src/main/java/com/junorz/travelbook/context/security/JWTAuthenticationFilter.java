package com.junorz.travelbook.context.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junorz.travelbook.utils.JWTUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    @Autowired
    public void setJwtUtil(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        MultipleReadHttpRequest wrappedRequest = new MultipleReadHttpRequest(request);

        // Front-end can get the token from header after setting
        // Access-Control-Expose-Header
        response.setHeader("Access-Control-Expose-Headers", "token, tokenRefreshed");

        // Get token from header and travelBoodId from body.
        String token = wrappedRequest.getHeader("token");

        BufferedReader reader = wrappedRequest.getReader();
        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(line -> sb.append(line));
        String requestBody = sb.toString();
        // do parse
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = Optional.ofNullable(objectMapper.readTree(requestBody)).orElse(null);
        String travelBookId = null;
        if (node != null) {
            JsonNode travelBookIdNode = node.get("travelBookId");
            if (travelBookIdNode != null) {
                travelBookId = travelBookIdNode.asText();
            }
        }

        if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(travelBookId)) {
            // start authentication, and set JWTAuthenticationToken to SecurityContext
            try {
                Jws<Claims> jws = jwtUtil.parseToken(token);
                String travelBookIdInToken = (String) jws.getBody().get("travelBookId");
                if (travelBookId.equals(travelBookIdInToken)) {
                    JWTAuthenticationToken authentication = new JWTAuthenticationToken(travelBookId,
                            AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JwtException e) {
                // If jwt token parsed failed, try to refresh token
                String newToken = jwtUtil.refreshToken(token, travelBookId);
                if (StringUtils.isNotEmpty(newToken)) {
                    JWTAuthenticationToken authentication = new JWTAuthenticationToken(travelBookId,
                            AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    response.setHeader("tokenRefreshed", "true");
                    response.setHeader("token", newToken);
                }
            }
        }

        filterChain.doFilter(wrappedRequest, response);

    }

}
