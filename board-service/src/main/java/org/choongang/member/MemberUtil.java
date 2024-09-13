package org.choongang.member;

import lombok.RequiredArgsConstructor;
import org.choongang.member.constants.Authority;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberUtil {
    /*
        public boolean isLogin() {
            return true;
        }

        public boolean isAdmin() {

            return true;
        }
        public Member getMember() {

            Member member = new Member();
            member.setUserName("사용자01");
            member.setEmail("user01@test.com");
    *
            List<Authorities> authorities = List.of(USER, ADMIN).stream().map(s -> {
                Authorities authority = new Authorities();
                authority.setAuthority(s);
                return authority;
            }).toList();
            System.out.println(authorities);
    *
            List<Authorities> authorities1 = List.of(Authority.ADMIN, Authority.USER).stream().map(s -> {
                return Authorities.builder().authority(s).build();
                    }).toList();
            System.out.println("authorities1 :" + authorities1);

            member.setAuthorities(authorities1);

            return member;
        }

    */
    public boolean isLogin() {
        return getMember() != null;
    }

    public boolean isAdmin() {
        if (isLogin()) {
            List<Authorities> authorities = getMember().getAuthorities();
            return authorities.stream().anyMatch(s -> s.getAuthority().equals(Authority.ADMIN));
        }
        return false;
    }

    public Member getMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof MemberInfo memberInfo) {
            return memberInfo.getMember();
        }

        return null;
    }


}