package com.exam.fifa.member;

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

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String profileImg;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
