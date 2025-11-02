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
 * 상품 엔티티 클래스
 * 판매 상품 정보와 상태를 관리합니다.
 */
@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    /**
     * 상품 고유 식별자(ID)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 상품 제목
     */
    private String title;
    
    /**
     * 상품 설명
     */
    private String description;
    
    /**
     * 상품 가격
     */
    private int price;
    
    /**
     * 상품 상태 (WAITING: 판매중, RESERVED: 예약중, COMPLETED: 판매완료)
     */
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    
    /**
     * 상품 카테고리 (다대일 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    /**
     * 판매자 정보 (다대일 관계)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;
    
    /**
     * 구매자 정보 (다대일 관계, 판매 완료 후 설정)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;
    
    /**
     * 상품 이미지 목록 (일대다 관계)
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();
    
    /**
     * 생성 일시 (변경 불가)
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 수정 일시
     */
    private LocalDateTime updatedAt;
    
    /**
     * 상품 상태를 정의하는 열거형
     */
    public enum ProductStatus {
        /**
         * 판매중 상태
         */
        WAITING, 
        
        /**
         * 예약중 상태
         */
        RESERVED, 
        
        /**
         * 판매완료 상태
         */
        COMPLETED
    }
    
    /**
     * 엔티티 생성 시 자동으로 호출되는 메서드
     * 생성 일시와 수정 일시를 현재 시간으로 설정합니다.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    /**
     * 엔티티 수정 시 자동으로 호출되는 메서드
     * 수정 일시를 현재 시간으로 업데이트합니다.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}