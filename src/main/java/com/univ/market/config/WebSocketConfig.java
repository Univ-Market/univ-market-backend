package com.univ.market.config;

import com.univ.market.security.WebSocketAuthenticationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * 실시간 채팅을 위한 STOMP 기반 WebSocket 설정을 정의합니다.
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    private final WebSocketAuthenticationInterceptor authInterceptor;
    
    /**
     * 메시지 브로커 설정
     * 
     * @param registry MessageBrokerRegistry 객체
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 구독 주제 접두사 설정
        registry.enableSimpleBroker("/topic");
        // 클라이언트가 서버로 메시지를 보낼 때 사용할 접두사 설정
        registry.setApplicationDestinationPrefixes("/app");
    }
    
    /**
     * STOMP 엔드포인트 설정
     * 
     * @param registry StompEndpointRegistry 객체
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 연결 엔드포인트 설정
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS(); // SockJS 지원 활성화
    }
    
    /**
     * 클라이언트 인바운드 채널 설정
     * 인증 인터셉터를 등록합니다.
     * 
     * @param registration ChannelRegistration 객체
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // WebSocket 인증 처리를 위한 인터셉터 등록
        registration.interceptors(authInterceptor);
    }
}
