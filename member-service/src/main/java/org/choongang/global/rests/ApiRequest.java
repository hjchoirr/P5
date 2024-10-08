package org.choongang.global.rests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.choongang.global.Utils;
import org.choongang.global.tests.TestTokenService;
import org.choongang.member.constants.Authority;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Component
@Setter
@RequiredArgsConstructor
public class ApiRequest {
    private final RestTemplate restTemplate;
    private final ObjectMapper om;
    private final Utils utils;
    private final TestTokenService tokenService;

    private boolean test;
    private Authority authority;

    private ResponseEntity<JSONData> response;
    private JSONData jsonData;

    public ApiRequest request(String url, String serviceId) {
        return request(url, serviceId, HttpMethod.GET, null);
    }

    public ApiRequest request(String url, String serviceId, HttpMethod method) {
        return request(url, serviceId, method, null);
    }

    public ApiRequest request(String url, String serviceId, HttpMethod method, Object data) {
        String requestUrl = utils.url(url, serviceId);
        System.out.println("requestUrl: " + requestUrl);
        method = Objects.requireNonNullElse(method, HttpMethod.GET);

        //if (System.getenv("mode").equals("test")) {
        if (System.getenv("spring.profiles.active").contains("test")) {
            test = true;
        }

        HttpHeaders headers = new HttpHeaders();
        String token = test ? tokenService.getToken(Objects.requireNonNullElse(authority, Authority.USER)) : utils.getToken();
        test = false;

        //String token = utils.getToken();
        if (StringUtils.hasText(token)) { // 토큰이 있다면 토큰 함께 전달
            headers.setBearerAuth(token);
        }

        if (method != HttpMethod.GET && method != HttpMethod.DELETE) { // POST, PUT, PATCH 방식인 경우
            headers.setContentType(MediaType.APPLICATION_JSON);
            try {
                String body = om.writeValueAsString(data);
                HttpEntity<String> request = new HttpEntity<>(body, headers);

                this.response = restTemplate.exchange(URI.create(requestUrl), method, request, JSONData.class);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else { // GET 또는 DELETE 방식인 경우

            HttpEntity<Void> request = new HttpEntity<>(headers);
            this.response = restTemplate.exchange(URI.create(requestUrl), method, request, JSONData.class);
        }

        if (this.response != null) {
            jsonData = this.response.getBody();
        }
        return this;
    }

    /**
     * 응답 코드
     * @return
     */
    public HttpStatusCode getStatus() {
        return response.getStatusCode();
    }

    public ResponseEntity<JSONData> getResponse() {
        return response;
    }

    public JSONData getData() {
        return jsonData;
    }

    /**
     * JSON으로 응답 데이터 변환
     *
     * @param clazz
     * @return
     * @param <T>
     */

    public <T> T toObj(Class<T> clazz) {

        JSONData jsonData = response.getBody();
        try {
        String body = om.writeValueAsString(jsonData.getData());
            if (StringUtils.hasText(body)) {
                return om.readValue(body, clazz);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON으로 응답 데이터 변환
     *
     * @param typeReference
     * @return
     * @param <T>
     */
    public <T> List<T> toList(TypeReference<List<T>> typeReference) {

        JSONData jsonData = response.getBody();
        try {
            String body = om.writeValueAsString(jsonData.getData());
            if (StringUtils.hasText(body)) {
                return om.readValue(body, typeReference);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 헤더 정보 조회
     *
     * @return
     */
    public HttpHeaders getHeaders() {
        return response.getHeaders();
    }

    /**
     * 문자열로 응답 데이터 조회
     *
     * @return
     */
    public String toString() {
        return (String)jsonData.getData();
    }
}
