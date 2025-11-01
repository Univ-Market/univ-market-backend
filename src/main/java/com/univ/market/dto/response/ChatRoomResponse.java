package com.univ.market.dto.response;

import com.univ.market.domain.ChatRoom;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 채팅방 응답 DTO
 * 서버에서 클라이언트로 전송되는 채팅방 정보를 담습니다.
 */
@Data
@Builder
public class ChatRoomResponse {
    /**
     * 채팅방 ID
     */
    private Long id;
    
    /**
     * 상품 ID
     */
    private Long productId;
    
    /**
     * 상품 제목
     */
    private String productTitle;
    
    /**
     * 상품 이미지 URL
     */
    private String productImageUrl;
    
    /**
     * 판매자 ID
     */
    private Long sellerId;
    
    /**
     * 판매자 닉네임
     */
    private String sellerNickname;
    
    /**
     * 구매자 ID
     */
    private Long buyerId;
    
    /**
     * 구매자 닉네임
     */
    private String buyerNickname;
    
    /**
     * 채팅방 생성 일시
     */
    private LocalDateTime createdAt;
    
    /**
     * 마지막 메시지 정보
     */
    private ChatMessageResponse lastMessage;
    
    /**
     * ChatRoom 엔티티를 ChatRoomResponse DTO로 변환하는 정적 메서드
     * 
     * @param chatRoom 변환할 ChatRoom 엔티티
     * @return 변환된 ChatRoomResponse 객체
     */
    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        // 마지막 메시지 설정
        ChatMessageResponse lastMessage = null;
        if (!chatRoom.getMessages().isEmpty()) {
            lastMessage = ChatMessageResponse.fromEntity(
                    chatRoom.getMessages().get(chatRoom.getMessages().size() - 1));
        }
        
        // 상품 이미지 URL 설정
        String productImageUrl = null;
        if (!chatRoom.getProduct().getImages().isEmpty()) {
            productImageUrl = chatRoom.getProduct().getImages().get(0).getImageUrl();
        }
        
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .productId(chatRoom.getProduct().getId())
                .productTitle(chatRoom.getProduct().getTitle())
                .productImageUrl(productImageUrl)
                .sellerId(chatRoom.getProduct().getSeller().getId())
                .sellerNickname(chatRoom.getProduct().getSeller().getNickname())
                .buyerId(chatRoom.getBuyer().getId())
                .buyerNickname(chatRoom.getBuyer().getNickname())
                .createdAt(chatRoom.getCreatedAt())
                .lastMessage(lastMessage)
                .build();
    }
}
