package com.charjay.jwt;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenHelper {

    private static Key getKeyInstanse(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretBytes = DatatypeConverter.parseBase64Binary("JWT-TOKEN");
        Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
        return signingKey;
    }

    public static String createToken() {

        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("username", "token");
        claims.put("role", "admin");
        JwtBuilder builder = Jwts.builder().setClaims(claims)
                .setId("tokenid")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+10*60*1000))
                .signWith(SignatureAlgorithm.HS256, getKeyInstanse());

        return builder.compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(getKeyInstanse())
                .parseClaimsJws(token).getBody();
    }

    public static void validateToken(String token) {
        try{
            Claims claims = parseToken(token);
            String username = claims.get("username").toString();
            String role = claims.get("role").toString();
            String tokenid = claims.getId();
            System.out.println("[username]:"+username);
            System.out.println("[role]:"+role);
            System.out.println("[tokenid]:"+tokenid);
            System.out.println("[claims]:"+claims);
        } catch(ExpiredJwtException e) {
            System.out.println("token expired");
        } catch (InvalidClaimException e) {
            System.out.println("token invalid");
        } catch (Exception e) {
            System.out.println("token error");
        }
    }

    public static void main(String[] args) {
        validateToken(createToken());
    }

}
