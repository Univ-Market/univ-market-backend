package com.univ.market.service;

import com.univ.market.domain.ChatMessage;
import com.univ.market.domain.ChatRoom;
import com.univ.market.domain.Product;
import com.univ.market.domain.User;
import com.univ.market.dto.request.ChatMessageRequest;
import com.univ.market.dto.response.ChatMessageResponse;
import com.univ.market.dto.response.ChatRoomResponse;
import com.univ.market.repository.ChatMessageRepository;
import com.univ.market.repository.ChatRoomRepository;
import com.univ.market.repository.ProductRepository;
import com.univ.market.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 채팅 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 채팅방 생성, 메시지 전송, 채팅 내역 조회 등의 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    /**
     * 채팅방을 생성하는 메서드
     * 이미 존재하는 채팅방이면 기존 채팅방을 반환합니다.
     * 
     * @param productId 상품 ID
     * @param buyerId 구매자 ID
     * @return 생성되거나 조회된 채팅방 정보
     * @throws IllegalArgumentException 존재하지 않는 상품이나 사용자인 경우
     */
    @Transactional
    public ChatRoomResponse createChatRoom(Long productId, Long buyerId) {
        // 상품 정보 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        
        // 구매자 정보 조회
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 이미 존재하는 채팅방인지 확인
        ChatRoom existingRoom = chatRoomRepository.findByProductIdAndBuyerId(productId, buyerId)
                .orElse(null);
        
        if (existingRoom != null) {
            return ChatRoomResponse.fromEntity(existingRoom);
        }
        
        // 새 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .product(product)
                .buyer(buyer)
                .build();
        
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.fromEntity(savedChatRoom);
    }
    
    /**
     * 채팅 메시지를 전송하는 메서드
     * 
     * @param request 메시지 전송 요청 데이터
     * @param senderId 발신자 ID
     * @return 전송된 메시지 정보
     * @throws IllegalArgumentException 존재하지 않는 채팅방이나 사용자인 경우
     * @throws IllegalStateException 해당 채팅방 참여자가 아닌 경우
     */
    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request, Long senderId) {
        // 채팅방 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        
        // 발신자 정보 조회
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        
        // 채팅방 참여자 확인 (판매자 또는 구매자만 메시지 전송 가능)
        if (!sender.getId().equals(chatRoom.getBuyer().getId()) && 
            !sender.getId().equals(chatRoom.getProduct().getSeller().getId())) {
            throw new IllegalStateException("해당 채팅방에 참여할 권한이 없습니다.");
        }
        
        // 메시지 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .content(request.getContent())
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
        
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        return ChatMessageResponse.fromEntity(savedMessage);
    }
    
    /**
     * 채팅방의 메시지 목록을 조회하는 메서드
     * 
     * @param chatRoomId 채팅방 ID
     * @param userId 요청자 ID
     * @return 메시지 목록
     * @throws IllegalArgumentException 존재하지 않는 채팅방인 경우
     * @throws IllegalStateException 해당 채팅방 참여자가 아닌 경우
     */
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatMessages(Long chatRoomId, Long userId) {
        // 채팅방 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        
        // 채팅방 참여자 확인
        if (!userId.equals(chatRoom.getBuyer().getId()) && 
            !userId.equals(chatRoom.getProduct().getSeller().getId())) {
            throw new IllegalStateException("해당 채팅방에 접근할 권한이 없습니다.");
        }
        
        // 메시지 목록 반환
        return chatRoom.getMessages().stream()
                .map(ChatMessageResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자의 채팅방 목록을 조회하는 메서드
     * 판매자 또는 구매자로 참여한 모든 채팅방을 반환합니다.
     * 
     * @param userId 사용자 ID
     * @return 채팅방 목록
     */
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getMyChatRooms(Long userId) {
        // 구매자로 참여한 채팅방
        List<ChatRoom> buyerRooms = chatRoomRepository.findByBuyerId(userId);
        
        // 판매자로 참여한 채팅방
        List<ChatRoom> sellerRooms = chatRoomRepository.findByProductSellerId(userId);
        
        // 중복 제거 후 합치기
        return Stream.concat(buyerRooms.stream(), sellerRooms.stream())
                .distinct()
                .map(ChatRoomResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 채팅방 상세 정보를 조회하는 메서드
     * 
     * @param roomId 채팅방 ID
     * @param userId 요청자 ID
     * @return 채팅방 상세 정보
     * @throws IllegalArgumentException 존재하지 않는 채팅방인 경우
     * @throws IllegalStateException 해당 채팅방 참여자가 아닌 경우
     */
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoomById(Long roomId, Long userId) {
        // 채팅방 정보 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        
        // 채팅방 참여자 확인
        if (!userId.equals(chatRoom.getBuyer().getId()) && 
            !userId.equals(chatRoom.getProduct().getSeller().getId())) {
            throw new IllegalStateException("해당 채팅방에 접근할 권한이 없습니다.");
        }
        
        return ChatRoomResponse.fromEntity(chatRoom);
    }
}