package com.univ.market.controller;

import com.univ.market.dto.request.CategoryRequest;
import com.univ.market.dto.response.CategoryResponse;
import com.univ.market.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 카테고리 관련 API 엔드포인트를 제공하는 컨트롤러
 * 카테고리 조회 및 등록 기능을 처리합니다.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * 전체 카테고리 목록 조회 API
     * 
     * @return 카테고리 목록
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    /**
     * 카테고리 등록 API
     * 
     * @param request 카테고리 등록 요청 데이터
     * @return 등록된 카테고리 정보
     */
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
}