package org.choongang;

import org.choongang.global.tests.TestTokenService;
import org.choongang.member.constants.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestTokenServiceTest {

    @Autowired
    private TestTokenService testTokenService;

    @Test
    public void test() {

        String token  = testTokenService.getToken(Authority.USER);
        System.out.println(token);
    }

}
