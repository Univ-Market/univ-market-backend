package com.univ.market.service;

import com.univ.market.domain.Image;
import com.univ.market.domain.Product;
import com.univ.market.repository.ImageRepository;
import com.univ.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 이미지 관련 비즈니스 로직을 처리하는 서비스 클래스
 * 이미지 추가, 삭제 등의 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class ImageService {
    
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;
    
    /**
     * 상품에 이미지를 추가하는 메서드
     * 
     * @param productId 상품 ID
     * @param imageUrls 추가할 이미지 URL 목록
     * @return 추가된 이미지 목록
     * @throws IllegalArgumentException 존재하지 않는 상품인 경우
     */
    @Transactional
    public List<Image> addImagesToProduct(Long productId, List<String> imageUrls) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        
        // 이미지 엔티티 생성 및 저장
        List<Image> images = imageUrls.stream()
                .map(url -> {
                    Image image = new Image();
                    image.setImageUrl(url);
                    image.setProduct(product);
                    return image;
                })
                .collect(Collectors.toList());
        
        return imageRepository.saveAll(images);
    }
    
    /**
     * 이미지를 삭제하는 메서드
     * S3에서 파일을 삭제하고 DB에서 이미지 정보를 삭제합니다.
     * 
     * @param imageId 삭제할 이미지 ID
     * @param userId 요청자 ID (소유자 확인용)
     * @throws IllegalArgumentException 존재하지 않는 이미지인 경우
     * @throws IllegalStateException 이미지 소유자가 아닌 경우
     */
    @Transactional
    public void deleteImage(Long imageId, Long userId) {
        // 이미지 조회
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
        
        // 이미지 소유자 확인
        if (!image.getProduct().getSeller().getId().equals(userId)) {
            throw new IllegalStateException("본인이 등록한 상품의 이미지만 삭제할 수 있습니다.");
        }
        
        // S3에서 파일 삭제
        s3Service.deleteFile(image.getImageUrl());
        
        // DB에서 이미지 정보 삭제
        imageRepository.delete(image);
    }
    
    /**
     * 상품의 모든 이미지를 삭제하는 메서드
     * 상품 삭제 시 호출됩니다.
     * 
     * @param product 삭제할 이미지가 속한 상품
     */
    @Transactional
    public void deleteAllProductImages(Product product) {
        // S3에서 모든 이미지 파일 삭제
        product.getImages().forEach(image -> {
            s3Service.deleteFile(image.getImageUrl());
        });
        
        // DB에서 이미지 정보 삭제는 Product 엔티티의 cascade 설정으로 자동 처리됨
    }
}