package com.univ.market.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 채팅방 엔티티 클래스
 * 판매자와 구매자 간의 채팅방 정보를 관리합니다.
 */
@Entity
@Table(name = "chat_rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    /**
     * 채팅방 고유 식별자(ID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 채팅 대상 상품 (다대일 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    
    /**
     * 구매자 정보 (다대일 관계)
     * 채팅방에서 판매자는 상품의 seller로 이미 정의되므로 별도로 저장하지 않습니다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;
    
    /**
     * 채팅 메시지 목록 (일대다 관계)
     */
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();
    
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