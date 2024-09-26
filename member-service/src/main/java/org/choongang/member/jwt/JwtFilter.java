package org.choongang.member.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider provider;

    /**
     * 요청헤더  Authorization: Bearer JWT토큰 값
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse)response;
        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            // 토큰으로 회원 인증 객체 -> 로그인 유지 처리
            Authentication authentication = provider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    /**
     *  요청 헤더에서 JWT 토큰 추출
     *  Authorization: Bearer JWT토큰 값
     *
     * @param request
     * @return
     */
    private String getToken(ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        /*
        Enumeration<String> headerNames = req.getHeaderNames();
        headerNames.asIterator().forEachRemaining(headerName -> {
            System.out.println(req.getRequestURI() + "=>" +  headerName + ": " + req.getHeader(headerName));
        });
        */
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = req.getHeader(headerName);
            System.out.println(req.getRequestURI() + " " + headerName + ":" + headerValue);
        }


        String bearerToken = req.getHeader("Authorization");
        System.out.println("getToken token: " + bearerToken);

        if (StringUtils.hasText(bearerToken)
                && bearerToken.toUpperCase().startsWith("BEARER ")) {

            return bearerToken.substring(7).trim();
        }

        return null;
    }
}
