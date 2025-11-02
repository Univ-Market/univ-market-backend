package com.univ.market.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AWS S3 설정 클래스
 * S3 클라이언트 및 관련 설정을 정의합니다.
 */
@Configuration
public class S3Config {
    
    @Value("${aws.s3.access-key}")
    private String accessKey;
    
    @Value("${aws.s3.secret-key}")
    private String secretKey;
    
    @Value("${aws.s3.region}")
    private String region;
    
    /**
     * Amazon S3 클라이언트 설정
     * 
     * @return 구성된 AmazonS3 클라이언트
     */
    @Bean
    public AmazonS3 amazonS3Client() {
        // AWS 인증 정보 설정
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        
        // S3 클라이언트 빌더 설정
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.fromName(region))
                .build();
    }
}