package com.univ.market.dto.response;

import com.univ.market.domain.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 응답 DTO
 * 서버에서 클라이언트로 전송되는 채팅 메시지 정보를 담습니다.
 */
@Data
@Builder
public class ChatMessageResponse {
    /**
     * 메시지 ID
     */
    private Long id;
    
    /**
     * 메시지 내용
     */
    private String content;
    
    /**
     * 발신자 ID
     */
    private Long senderId;
    
    /**
     * 발신자 닉네임
     */
    private String senderNickname;
    
    /**
     * 채팅방 ID
     */
    private Long chatRoomId;
    
    /**
     * 메시지 생성 일시
     */
    private LocalDateTime createdAt;
    
    /**
     * ChatMessage 엔티티를 ChatMessageResponse DTO로 변환하는 정적 메서드
     * 
     * @param chatMessage 변환할 ChatMessage 엔티티
     * @return 변환된 ChatMessageResponse 객체
     */
    public static ChatMessageResponse fromEntity(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .id(chatMessage.getId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSender().getId())
                .senderNickname(chatMessage.getSender().getNickname())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
