package org.choongang.member;

import org.choongang.member.entities.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberUtilTest {
    @Autowired
    MemberUtil util;

    @Test
    void test() {
        Member member = util.getMember();
        System.out.println(member);

    }
}
