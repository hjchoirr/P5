package org.choongang.board;

import org.choongang.global.tests.MockMember;
import org.choongang.member.MemberUtil;
import org.choongang.member.constants.Authority;
import org.choongang.member.entities.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class boardTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberUtil memberUtil;

    @Test
    @MockMember
    void test1() {
        Member member = memberUtil.getMember();
        System.out.println("mock member :" + member);
    }

    @Test
    @MockMember(authority = Authority.ADMIN)
    void test2() throws Exception {
        mockMvc.perform(get("/admin/info/free")).andDo(print());
    }
}
