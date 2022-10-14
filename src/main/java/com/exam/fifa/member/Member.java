package com.exam.fifa.member;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;
    private String username;
    private String email;
    private String password;
    private String roles;
    @Column(columnDefinition = "TEXT")
    private String profileImg;

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
