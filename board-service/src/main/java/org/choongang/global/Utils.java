package org.choongang.global;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Utils { // 빈의 이름 - utils

    private final MessageSource messageSource;
    private final HttpServletRequest request;
    private final DiscoveryClient discoveryClient;

    public Map<String, List<String>> getErrorMessages(Errors errors) {
        // FieldErrors

        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, e -> getCodeMessages(e.getCodes()), (p1, p2) -> p1));

        // GlobalErrors
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(e -> getCodeMessages(e.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }
        return messages;
    }


    public List<String> getCodeMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);

        List<String> messages = Arrays.stream(codes)
                .map(c -> {
                    try {
                        return ms.getMessage(c, null, request.getLocale());
                    } catch (Exception e) {
                        return "";
                    }
                })
                .filter(s -> !s.isBlank())
                .toList();

        ms.setUseCodeAsDefaultMessage(true);
        return messages;
    }

    public String getMessage(String code) {
        List<String> messages = getCodeMessages(new String[] {code});

        return messages.isEmpty() ? code : messages.get(0);
    }

    public String url(String url, String serviceId) {
        System.out.println("1 serviceId :" + serviceId);
        String tmp = new String(serviceId);


        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
/*
        System.out.println("---discoveryClient instances ---");
        System.out.println(instances);
        System.out.println("-------------------------");
        instances.forEach(i -> {
            System.out.println("i.getServiceId(): " + i.getServiceId());
            System.out.println("i.getInstanceId(): " + i.getInstanceId());
            System.out.println("i.getHost(): " + i.getHost());
            System.out.println("i.getPort(): " + i.getPort());
            System.out.println("i.getUri(): " + i.getUri());
            System.out.println("i.getMetadata(): " + i.getMetadata());
        });
*/
        try {
            System.out.println("2 serviceId :" + serviceId);
            System.out.println("2 tmp : " + tmp);
            /*
            if(!instances.get(0).getInstanceId().contains(serviceId)) {
                throw new RuntimeException(serviceId + "를 찾을 수 없습니다.");
            }
            */
            return String.format("%s%s", instances.get(0).getUri().toString(), url);
        } catch (Exception e) {
            System.out.println("3 serviceId :" + serviceId);
            System.out.println("3 tmp : " + tmp);
            return String.format("%s://%s:%d%s%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), url);
        }
    }

    /**
     * 요청 받은 JWT 토큰 조회
     *
     * @return
     */
    public String getToken() {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)
                && bearerToken.toUpperCase().startsWith("BEARER ")) {
            return bearerToken.substring(7).trim();
        }

        String token = request.getParameter("token");
        if (StringUtils.hasText(token)) {
            return token;
        }

        return null;
    }

    public int guestUid() {
        String ip = request.getRemoteAddr();
        String ua = request.getHeader("User-Agent");
        return Objects.hash(ip, ua);
    }
}