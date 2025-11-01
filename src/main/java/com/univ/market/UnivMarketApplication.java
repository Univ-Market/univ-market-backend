package com.univ.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 대학마켓 애플리케이션의 메인 클래스
 * 
 * @SpringBootApplication: Spring Boot 애플리케이션의 주요 설정을 자동으로 활성화합니다.
 *   - @Configuration: 빈 정의를 위한 구성 클래스로 지정합니다.
 *   - @EnableAutoConfiguration: 클래스패스 설정, 다른 빈, 다양한 속성 설정에 따라 빈 자동 구성을 활성화합니다.
 *   - @ComponentScan: 애플리케이션 컴포넌트를 스캔하는 위치를 지정합니다.
 * 
 * @EnableAsync: 비동기 메서드 실행을 활성화합니다. 이메일 전송과 같은 작업에 사용됩니다.
 */
@SpringBootApplication
@EnableAsync
public class UnivMarketApplication {

	/**
	 * 애플리케이션의 진입점
	 * Spring Boot 애플리케이션을 시작합니다.
	 * 
	 * @param args 명령줄 인수
	 */
	public static void main(String[] args) {
		SpringApplication.run(UnivMarketApplication.class, args);
	}

}