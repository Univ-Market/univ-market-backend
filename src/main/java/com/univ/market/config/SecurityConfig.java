package com.univ.market.config;

import com.univ.market.security.JwtAuthenticationFilter;
import com.univ.market.security.JwtTokenProvider;
import com.univ.market.security.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * Spring Security 설정 클래스
 * 보안 설정, 인증/인가 규칙, CORS 설정 등을 정의합니다.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    
    /**
     * 보안 필터 체인 설정
     * 인증/인가 규칙, CORS, CSRF, 세션 관리 등을 설정합니다.
     * 
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 보안 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS 설정 적용
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CORS 설정 적용
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/login/**", "/oauth2/**", "/api/categories").permitAll()
                .requestMatchers("/api/products").permitAll() // 상품 목록 조회는 인증 없이도 가능
                .requestMatchers("/api/chat/**").permitAll() //임시추가
                .requestMatchers("/swagger-ui/**", "/api-docs/**").permitAll() // swagger 추가
                .requestMatchers("/ping").permitAll() // 서버 상태 확인 API
                // 인증이 필요한 URL
                .requestMatchers("/api/products/*/reserve", "/api/products/*/complete").authenticated()
                .requestMatchers("/api/upload/**").authenticated()
                .requestMatchers("/api/users/verify/**").authenticated()
                //.requestMatchers("/api/chat/**").authenticated() //임시로 삭제
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            // OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
            )
            // JWT 인증 필터 추가
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    /**
     * CORS 설정
     * Cross-Origin Resource Sharing 정책을 정의합니다.
     *
     * @return CorsConfigurationSource 구현체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 허용할 출처
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 허용할 헤더
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // 인증 정보 포함 허용
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
}