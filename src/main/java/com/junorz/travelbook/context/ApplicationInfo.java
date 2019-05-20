package com.junorz.travelbook.context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.junorz.travelbook.config.SecurityConfig.JWTInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Bean to store application scoped information.
 */
@Data
public class ApplicationInfo {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInfo.class);

    // ============ Application Scoped Data ===============
    private Key publicKey;
    private Key privateKey;

    private Map<String, TokenInfo> tokenHistories = new HashMap<>();
    // ====================================================

    private JWTInfo jwtInfo;

    @Autowired
    public void setJwtInfo(JWTInfo jwtInfo) {
        this.jwtInfo = jwtInfo;
    }

    @PostConstruct
    public void init() {
        // read public key
        publicKey = readPublicKey();

        // read private key
        privateKey = readPrivateKey();
    }

    private Key readPrivateKey() {
        try (InputStream is = new FileInputStream(jwtInfo.getPrivateKeyPath());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(line -> sb.append(line));
            String privateKeyString = sb.toString().replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));
            return kf.generatePrivate(keySpec);
        } catch (Exception e) {
            logger.error("An exception has occurred while trying to get private key.");
            throw new RuntimeException(e);
        }
    }

    private Key readPublicKey() {
        try (InputStream is = new FileInputStream(jwtInfo.getPublicKeyPath());
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(line -> sb.append(line));
            String publicKeyString = sb.toString().replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
            return kf.generatePublic(keySpec);
        } catch (Exception e) {
            logger.error("An exception has occurred while trying to get public key.");
            throw new RuntimeException(e);
        }
    }

    public TokenInfo createTokenInfo(String travelBookId, LocalDateTime createdDateTime) {
        return new TokenInfo(travelBookId, createdDateTime);
    }

    @Data
    @AllArgsConstructor
    public class TokenInfo {
        String travelBookId;
        LocalDateTime expireDateTime;
    }

}
