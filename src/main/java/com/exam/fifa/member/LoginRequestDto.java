package com.exam.fifa.member;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken authenticationToken() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
