package org.choongang.global.tests;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.choongang.global.rests.ApiRequest;
import org.choongang.member.constants.Authority;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Setter
public class TestTokenService {
    private ApiRequest apiRequest;

    public String getToken(Authority authority) {
        Map<String, Object> params = new HashMap<>();

        String email = "testuser_" + authority.name() + "@choongang.org";
        String password = "user01@test.comT";

        params.put("email", email);

        params.put("password", password);
        params.put("confirmPassword", password);
        params.put("userName",  "testuser_" + authority.name());
        params.put("mobile", "01000022222");
        params.put("agree", "true");

        ApiRequest result = apiRequest.request("/account", "member-service", HttpMethod.POST, params);

        if(result.getStatus().is2xxSuccessful()) {
            Map<String, String> loginParams = new HashMap<>();
            loginParams.put("email", email);
            loginParams.put("password", password);
            ApiRequest result2 = apiRequest.request("/account/token", "member-service", HttpMethod.POST, loginParams);
            if(result2.getStatus().is2xxSuccessful() && result2.getData().isSuccess()) {

                return result2.toObj(String.class);

            }
        }
        System.out.println(result.getResponse());
        return null;

    }
}
