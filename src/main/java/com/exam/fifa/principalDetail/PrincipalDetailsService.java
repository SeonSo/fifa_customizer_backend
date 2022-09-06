package com.exam.fifa.principalDetail;

import com.exam.fifa.member.Member;
import com.exam.fifa.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberDetails loadMemberByEmail(String email) throws UsernameNotFoundException {
        System.out.println("login 요청");
        System.out.println("email: " + email);
        Member memberEntity = memberRepository.findByEmail(email);
        System.out.println("loadUserByEamil: " + memberEntity);
        return new PrincipalDetails(memberEntity);
    }
}
