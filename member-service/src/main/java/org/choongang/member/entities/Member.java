package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.*;
import org.choongang.global.entities.BaseEntity;
import org.choongang.member.constants.Gender;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;

    @Column(length=45, nullable = false)
    private String gid;

    @Column(length=65, unique = true, nullable = false)
    private String email;

    @Column(length=65, nullable = false)
    private String password;

    @Column(length=40, nullable = false)
    private String userName;

    @Column(length=15, nullable = false)
    private String mobile;

    private String department; // 학과
    private String professor; // 지도 교수

    private String zonecode; // 우펴번호
    private String address; // 주소
    private String addressSub; // 나머지 주소

    @Column(length=10)
    private LocalDate birth; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(length=10)
    private Gender gender; // 성별

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Authorities> authorities;
}