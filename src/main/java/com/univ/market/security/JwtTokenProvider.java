package com.univ.market.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 처리하는 클래스
 * JWT 기반 인증을 위한 토큰 생성, 검증, 파싱 등의 기능을 제공합니다.
 */
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;
    
    private Key key;
    
    private final CustomUserDetailsService userDetailsService;
    
    /**
     * 생성자
     * 
     * @param userDetailsService 사용자 정보 서비스
     */
    public JwtTokenProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * 초기화 메서드
     * 애플리케이션 시작 시 시크릿 키를 기반으로 HMAC 키를 생성합니다.
     */
    @PostConstruct
    protected void init() {
        if (secretKey.length() < 32) { // 256비트보다 작은 키 길이
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        }
    }
    
    /**
     * 사용자 ID를 기반으로 JWT 토큰을 생성하는 메서드
     * 
     * @param userId 사용자 ID
     * @return 생성된 JWT 토큰
     */
    public String createToken(Long userId) {
        Claims claims = Jwts.claims().setSubject(userId.toString());
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * JWT 토큰에서 인증 정보를 추출하는 메서드
     * 
     * @param token JWT 토큰
     * @return 인증 객체 (Authentication)
     */
    public Authentication getAuthentication(String token) {
        String userIdStr = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        
        Long userId = Long.parseLong(userIdStr);
        UserDetails userDetails = userDetailsService.loadUserById(userId);
        
        return new UsernamePasswordAuthenticationToken(userId, "", userDetails.getAuthorities());
    }
    
    /**
     * HTTP 요청에서 JWT 토큰을 추출하는 메서드
     * 
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰, 없으면 null
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * JWT 토큰의 유효성을 검증하는 메서드
     * 
     * @param token 검증할 JWT 토큰
     * @return 유효성 여부 (true: 유효, false: 유효하지 않음)
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}