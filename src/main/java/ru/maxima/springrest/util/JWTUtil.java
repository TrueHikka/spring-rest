package ru.maxima.springrest.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

    public String generateToken(String username) {
        return JWT.create()
                .withSubject("Details about user")
                .withClaim("username", username)
                .withClaim("school", "Maxima")
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndReturnUsername(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Details about user")
                .withIssuer(issuer)
                .build();

        DecodedJWT verify = jwtVerifier.verify(token);
        System.out.println(verify.getClaim("school"));

        return verify.getClaim("username").asString();
    }

}
