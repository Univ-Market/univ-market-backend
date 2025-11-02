package com.univ.market.controller;

import com.univ.market.dto.request.ProductRequest;
import com.univ.market.dto.response.ProductResponse;
import com.univ.market.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 상품 관련 API 엔드포인트를 제공하는 컨트롤러
 * 상품 등록, 조회, 예약, 거래 완료, 삭제 등의 기능을 처리합니다.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * 상품 등록 API
     * 
     * @param request 상품 등록 요청 데이터
     * @param userId 현재 인증된 사용자 ID
     * @return 등록된 상품 정보
     */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request,
            @AuthenticationPrincipal Long userId) {
        ProductResponse response = productService.createProduct(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * 전체 상품 목록 조회 API
     * 
     * @param pageable 페이징 정보 (기본값: 페이지당 20개)
     * @return 상품 목록 페이지
     */
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> response = productService.getAllProducts(pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 상품 검색 API
     * 
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보 (기본값: 페이지당 20개)
     * @return 검색 결과 페이지
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> response = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 상품 상세 조회 API
     * 
     * @param id 상품 ID
     * @return 상품 상세 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 상품 예약 API
     * 
     * @param id 상품 ID
     * @param userId 현재 인증된 사용자 ID (구매자)
     * @return 예약된 상품 정보
     */
    @PutMapping("/{id}/reserve")
    public ResponseEntity<ProductResponse> reserveProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        ProductResponse response = productService.reserveProduct(id, userId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 거래 완료 API
     * 
     * @param id 상품 ID
     * @param userId 현재 인증된 사용자 ID
     * @return 거래 완료된 상품 정보
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<ProductResponse> completeTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        ProductResponse response = productService.completeTransaction(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 상품 삭제 API
     * 
     * @param id 상품 ID
     * @param userId 현재 인증된 사용자 ID
     * @return 응답 없음 (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal Long userId) {
        productService.deleteProduct(id, userId);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 카테고리별 상품 조회 API
     * 
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보 (기본값: 페이지당 20개)
     * @return 카테고리별 상품 목록 페이지
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<ProductResponse> response = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(response);
    }
}