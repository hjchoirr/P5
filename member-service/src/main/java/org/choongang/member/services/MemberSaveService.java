package org.choongang.member.services;

import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.member.constants.Authority;
import org.choongang.member.controllers.RequestAuthority;
import org.choongang.member.controllers.RequestJoin;
import org.choongang.member.controllers.RequestUpdate;
import org.choongang.member.entities.Authorities;
import org.choongang.member.entities.Member;
import org.choongang.member.exceptions.MemberNotFoundException;
import org.choongang.member.repositories.AuthoritiesRepository;
import org.choongang.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSaveService {
    private final MemberRepository memberRepository;
    private final AuthoritiesRepository authoritiesRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberUtil memberUtil;
    /**
     * 회원 가입 처리
     *
     * @param form
     */
    public void save(RequestJoin form) {
        Member member = new ModelMapper().map(form, Member.class);
        String hash = passwordEncoder.encode(form.getPassword()); // BCrypt 해시화
        member.setPassword(hash);

        save(member, List.of(Authority.USER));
    }

    /**
     * 회원정보 수정
     * @param form
     */
    public void save(RequestUpdate form, List<Authority> authorities) {
        String email = null;
        if (memberUtil.isAdmin() && StringUtils.hasText(form.getEmail())) {
            email = form.getEmail();
        } else {
            Member member = memberUtil.getMember();
            email = member.getEmail();
        }

        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        String password = form.getPassword();
        String mobile = form.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
        }

        member.setUserName(form.getUserName());
        member.setMobile(mobile);

        if (StringUtils.hasText(password)) {
            String hash = passwordEncoder.encode(password);
            member.setPassword(hash);
        }

        save(member, authorities);
    }

    public void save(RequestUpdate form) {
        save(form, null);
    }

    public void save(Member member, List<Authority> authorities) {

        // 휴대전화번호 숫자만 기록
        String mobile = member.getMobile();
        if (StringUtils.hasText(mobile)) {
            mobile = mobile.replaceAll("\\D", "");
            member.setMobile(mobile);
        }

        String gid = member.getGid();
        gid = StringUtils.hasText(gid) ? gid : UUID.randomUUID().toString();
        member.setGid(gid);

        memberRepository.saveAndFlush(member);


        // 권한 추가, 수정 S
        if (authorities != null) {
            List<Authorities> items = authoritiesRepository.findByMember(member);
            authoritiesRepository.deleteAll(items);
            authoritiesRepository.flush();

            items = authorities.stream().map(a -> Authorities.builder()
                    .member(member)
                    .authority(a)
                    .build()).toList();

            authoritiesRepository.saveAllAndFlush(items);
        }
        // 권한 추가, 수정 E
    }
    public void updateAuthority(RequestAuthority form) {

        Long seq = form.getMemberSeq();
        if(StringUtils.hasText(form.getAuthorityName())){
            Authority authority = Authority.valueOf(form.getAuthorityName());

            Member member = memberRepository.findById(seq).orElseThrow(MemberNotFoundException::new);
            System.out.println("member :" + member + " authority :" + authority);
            // 권한 추가, 수정 S
            if (authority != null) {

                List<Authorities> items = authoritiesRepository.findByMember(member);
                items.forEach(a -> {
                    if(a.getAuthority().equals(authority) && ! form.isInvoke()){
                        authoritiesRepository.delete(a);
                        authoritiesRepository.flush();
                        System.out.println("deleted authorities :" + a);
                    }
                });
                if(form.isInvoke()) {
                    Authorities authorities = Authorities.builder().member(member).authority(authority).build();
                    authoritiesRepository.saveAndFlush(authorities);
                    System.out.println("saved authorities :" + authorities);
                }
                authoritiesRepository.flush();
            }
            // 권한 추가, 수정 E
        }
    }
}
