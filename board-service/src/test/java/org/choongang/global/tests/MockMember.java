package org.choongang.global.tests;

import org.choongang.member.constants.Authority;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContextFactory.class)
public @interface MockMember {
    long seq() default 1L;
    String gid() default "testgid";
    String email() default "user01@test.com";
    String password() default "user01@test.comT";
    String userName() default "test사용자01";
    String mobile() default "0100001111";
    Authority authority() default Authority.USER;
}
