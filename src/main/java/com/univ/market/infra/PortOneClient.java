package com.univ.market.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PortOneClient {

    private final RestTemplate restTemplate;

    @Value("${portone.api-key}")
    private String apiKey;
    @Value("${portone.api-secret}")
    private String apiSecret;

    /** REST API 토큰 발급 */
    public String issueToken() {
        String url = "https://api.iamport.kr/users/getToken";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> req =
            new HttpEntity<>(Map.of("imp_key", apiKey, "imp_secret", apiSecret), headers);

        ResponseEntity<Map> res = restTemplate.postForEntity(url, req, Map.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
            throw new IllegalStateException("포트원 토큰 발급 실패");
        }
        Map body = res.getBody();
        Map response = (Map) body.get("response");
        if (response == null) throw new IllegalStateException("포트원 토큰 발급 실패(response null)");
        return (String) response.get("access_token");
    }

    /** imp_uid로 결제조회 */
    public Map getPaymentByImpUid(String token, String impUid) {
        String url = "https://api.iamport.kr/payments/" + impUid;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        ResponseEntity<Map> res =
            restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null) {
            throw new IllegalStateException("결제 조회 실패");
        }
        return res.getBody(); // { code, message, response: { status, amount, merchant_uid, ... } }
    }
}