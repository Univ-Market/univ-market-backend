package com.univ.market.dto.request;

import lombok.Data;

/**
 * 채팅 메시지 전송 요청 DTO
 * 클라이언트에서 서버로 전송되는 채팅 메시지 데이터를 담습니다.
 */
@Data
public class ChatMessageRequest {
    /**
     * 채팅방 ID
     */
    private Long chatRoomId;
    
    /**
     * 발신자 ID
     */
    private Long senderId;
    
    /**
     * 메시지 내용
     */
    private String content;
}