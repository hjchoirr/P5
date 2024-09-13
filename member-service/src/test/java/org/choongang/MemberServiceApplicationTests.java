package org.choongang;

import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.repositories.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MemberServiceApplicationTests {

	@Autowired
	MemberRepository memberRepository;

	@Test
	void contextLoads() {
	}
	@Test
	void test() {
		Member member = memberRepository.findByEmail("user01@test.com").orElse(null);
		List<Authorities> authorities = member.getAuthorities();
		System.out.println(authorities);
	}
}
