package org.choongang.member.entities;

import jakarta.persistence.*;
import lombok.*;
import org.choongang.global.entities.BaseEntity;

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

    private String department; // 부서
    private String manager; // 부서장

    private String role; // 직무

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<Authorities> authorities;
}