package com.univ.market.service;

import com.univ.market.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * 이메일 전송 관련 기능을 처리하는 서비스 클래스
 * 비동기로 이메일을 전송합니다.
 */
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    /**
     * 대학교 인증 이메일을 전송하는 메서드
     * 
     * @param to 수신자 이메일
     * @param verificationCode 인증 코드
     */
    @Async
    public void sendVerificationEmail(String to, String verificationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // 템플릿 컨텍스트 설정
            Context context = new Context();
            context.setVariable("verificationCode", verificationCode);
            
            // 템플릿 처리
            String htmlContent = templateEngine.process("verification-email", context);
            
            // 메일 내용 설정
            helper.setTo(to);
            helper.setSubject("[대학마켓] 대학교 인증 코드");
            helper.setText(htmlContent, true);
            
            // 이메일 전송
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
    
    /**
     * 새 상품 등록 알림 이메일을 전송하는 메서드
     * 
     * @param product 등록된 상품 정보
     */
    @Async
    public void sendNewProductNotification(Product product) {
        try {
            // 대학 내 사용자들에게 이메일 발송
            // 여기서는 예시로 판매자에게만 발송
            String to = product.getSeller().getEmail();
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // 템플릿 컨텍스트 설정
            Context context = new Context();
            context.setVariable("productTitle", product.getTitle());
            context.setVariable("productPrice", product.getPrice());
            context.setVariable("sellerName", product.getSeller().getNickname());
            
            // 템플릿 처리
            String htmlContent = templateEngine.process("new-product-notification", context);
            
            // 메일 내용 설정
            helper.setTo(to);
            helper.setSubject("[대학마켓] 새로운 상품이 등록되었습니다: " + product.getTitle());
            helper.setText(htmlContent, true);
            
            // 이메일 전송
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
    
    /**
     * 상품 예약 알림 이메일을 전송하는 메서드
     * 
     * @param product 예약된 상품 정보
     */
    @Async
    public void sendReservationNotification(Product product) {
        try {
            String to = product.getSeller().getEmail();
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            // 템플릿 컨텍스트 설정
            Context context = new Context();
            context.setVariable("productTitle", product.getTitle());
            context.setVariable("buyerName", product.getBuyer().getNickname());
            
            // 템플릿 처리
            String htmlContent = templateEngine.process("reservation-notification", context);
            
            // 메일 내용 설정
            helper.setTo(to);
            helper.setSubject("[대학마켓] 상품 예약 알림: " + product.getTitle());
            helper.setText(htmlContent, true);
            
            // 이메일 전송
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
    
    /**
     * 거래 완료 알림 이메일을 전송하는 메서드
     * 판매자와 구매자 모두에게 이메일을 전송합니다.
     * 
     * @param product 거래 완료된 상품 정보
     */
    @Async
    public void sendTransactionCompletedNotification(Product product) {
        try {
            // 판매자에게 발송
            MimeMessage sellerMessage = mailSender.createMimeMessage();
            MimeMessageHelper sellerHelper = new MimeMessageHelper(sellerMessage, true, "UTF-8");
            
            Context sellerContext = new Context();
            sellerContext.setVariable("productTitle", product.getTitle());
            sellerContext.setVariable("buyerName", product.getBuyer().getNickname());
            sellerContext.setVariable("price", product.getPrice());
            
            String sellerHtmlContent = templateEngine.process("transaction-completed-seller", sellerContext);
            
            sellerHelper.setTo(product.getSeller().getEmail());
            sellerHelper.setSubject("[대학마켓] 거래 완료 알림: " + product.getTitle());
            sellerHelper.setText(sellerHtmlContent, true);
            
            mailSender.send(sellerMessage);
            
            // 구매자에게 발송
            MimeMessage buyerMessage = mailSender.createMimeMessage();
            MimeMessageHelper buyerHelper = new MimeMessageHelper(buyerMessage, true, "UTF-8");
            
            Context buyerContext = new Context();
            buyerContext.setVariable("productTitle", product.getTitle());
            buyerContext.setVariable("sellerName", product.getSeller().getNickname());
            buyerContext.setVariable("price", product.getPrice());
            
            String buyerHtmlContent = templateEngine.process("transaction-completed-buyer", buyerContext);
            
            buyerHelper.setTo(product.getBuyer().getEmail());
            buyerHelper.setSubject("[대학마켓] 구매 완료 알림: " + product.getTitle());
            buyerHelper.setText(buyerHtmlContent, true);
            
            mailSender.send(buyerMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
