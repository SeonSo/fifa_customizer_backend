package com.exam.fifa.principalDetail;

import com.exam.fifa.member.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails {
    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        return authorities;
    }

    public String getUsername() {
        return member.getUsername();
    }

    public String getUserEmail() {
        return member.getEmail();
    }

    public String getPassword() {
        return member.getPassword();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }
}
