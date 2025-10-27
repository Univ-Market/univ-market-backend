package com.univ.market.controller;

import com.univ.market.dto.request.ChatMessageRequest;
import com.univ.market.dto.response.ChatMessageResponse;
import com.univ.market.dto.response.ChatRoomResponse;
import com.univ.market.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 채팅 관련 API 엔드포인트를 제공하는 컨트롤러
 * WebSocket을 통한 실시간 채팅과 REST API를 통한 채팅방 관리 기능을 처리합니다.
 */
@RestController
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    
    /**
     * WebSocket을 통한 메시지 처리 메서드
     * 클라이언트가 보낸 메시지를 처리하고 해당 채팅방의 구독자들에게 브로드캐스팅합니다.
     * 
     * @param roomId 채팅방 ID
     * @param message 클라이언트가 보낸 메시지
     */
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest message) {
        // 메시지 저장 및 DTO 변환
        ChatMessageResponse response = chatService.sendMessage(message, message.getSenderId());
        // 해당 채팅방 구독자들에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);
    }
    
    /**
     * 채팅방 생성 API
     * 
     * @param productId 상품 ID
     * @param userId 현재 인증된 사용자 ID (구매자)
     * @return 생성된 채팅방 정보
     */
    @PostMapping("/api/chat/rooms")
    public ResponseEntity<ChatRoomResponse> createChatRoom(
            @RequestParam Long productId,
            @AuthenticationPrincipal Long userId) {
        ChatRoomResponse chatRoom = chatService.createChatRoom(productId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoom);
    }
    
    /**
     * 내 채팅방 목록 조회 API
     * 판매자 또는 구매자로 참여한 모든 채팅방을 반환합니다.
     * 
     * @param userId 현재 인증된 사용자 ID
     * @return 채팅방 목록
     */
    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getMyChatRooms(@AuthenticationPrincipal Long userId) {
        List<ChatRoomResponse> chatRooms = chatService.getMyChatRooms(userId);
        return ResponseEntity.ok(chatRooms);
    }
    
    /**
     * 채팅방 메시지 목록 조회 API
     * 
     * @param roomId 채팅방 ID
     * @param userId 현재 인증된 사용자 ID
     * @return 메시지 목록
     */
    @GetMapping("/api/chat/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(
            @PathVariable Long roomId,
            @AuthenticationPrincipal Long userId) {
        List<ChatMessageResponse> messages = chatService.getChatMessages(roomId, userId);
        return ResponseEntity.ok(messages);
    }
}
