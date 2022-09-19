package com.exam.fifa.member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Data
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(unique=true, nullable = false)
    private String username;

    @Column(unique=true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long memberId, String username,
                  String email, String password,
                  String profileImg, Role role) {
        this.memberId = memberId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileImg = profileImg;
        this.role = Role.MEMBER;
    }

    public static Member createMember(SignupRequestDto signupRequestDto) {
        return Member.builder()
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .username(signupRequestDto.getUsername())
                .profileImg(signupRequestDto.getProfileImg())
                .build();
    }
}
