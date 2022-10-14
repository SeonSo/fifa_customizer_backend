package com.exam.fifa.security.authentication;

import com.exam.fifa.member.Member;
import com.exam.fifa.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("login요청");
        System.out.println("email: " + email);
        Member memberEntity = memberRepository.findByEmail(email);
        return new PrincipalDetails(memberEntity);
    }
}
