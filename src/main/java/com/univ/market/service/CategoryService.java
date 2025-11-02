package com.univ.market.service;

import com.univ.market.domain.Category;
import com.univ.market.dto.request.CategoryRequest;
import com.univ.market.dto.response.CategoryResponse;
import com.univ.market.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 카테고리 조회 및 등록 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    /**
     * 모든 카테고리를 조회하는 메서드
     * 
     * @return 카테고리 목록
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 카테고리를 등록하는 메서드
     * 
     * @param request 카테고리 등록 요청 데이터
     * @return 등록된 카테고리 정보
     */
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();
        
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(savedCategory);
    }
}
