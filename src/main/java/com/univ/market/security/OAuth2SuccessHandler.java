package com.univ.market.security;

import com.univ.market.domain.User;
import com.univ.market.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

/**
 * OAuth2 인증 성공 핸들러
 * OAuth2 로그인 성공 후 처리를 담당합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * 인증 성공 시 호출되는 메서드
     * OAuth2 사용자 정보를 처리하고 JWT 토큰을 생성한 후 리다이렉트합니다.
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authentication 인증 객체
     * @throws IOException I/O 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        // OAuth2 인증 토큰 추출
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String providerType = token.getAuthorizedClientRegistrationId();
        OAuth2User oAuth2User = token.getPrincipal();
        
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        String oauthId = null;
        String email = null;
        String nickname = null;
        
        // 소셜 로그인 제공자별 사용자 정보 추출
        if ("kakao".equals(providerType)) {
            oauthId = String.valueOf(attributes.get("id"));
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            nickname = (String) profile.get("nickname");
        }
        
        // 사용자 정보 저장 및 JWT 토큰 생성
        User user = userService.processKakaoLogin(oauthId, email, nickname);
        String jwtToken = jwtTokenProvider.createToken(user.getId());
        
        // 프론트엔드 콜백 URL로 토큰과 함께 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/callback")
                .queryParam("token", jwtToken)
                .build().toUriString();
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}