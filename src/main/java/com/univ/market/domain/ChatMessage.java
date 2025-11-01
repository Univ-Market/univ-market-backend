package com.univ.market.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 엔티티 클래스
 * 채팅방 내 주고받은 메시지를 관리합니다.
 */
@Entity
@Table(name = "chat_messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    /**
     * 메시지 고유 식별자(ID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 메시지 내용
     */
    private String content;
    
    /**
     * 메시지 발신자 (다대일 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;
    
    /**
     * 메시지가 속한 채팅방 (다대일 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
    
    /**
     * 생성 일시 (변경 불가)
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 엔티티 생성 시 자동으로 호출되는 메서드
     * 생성 일시를 현재 시간으로 설정합니다.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
