package com.exam.fifa.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    @PostMapping("/signup")
    public String signup(@RequestBody Member member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRole(Role.MEMBER);
        memberRepository.save(member);
        return "환영합니다 회원가입이 완료되었습니다.";
    }

    @GetMapping("/member")
    public String member() {
        return "member";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("session")
    public String getSession() {
        String contextHolder = SecurityContextHolder.getContext().getAuthentication().toString();
        System.out.println(contextHolder);
        return "session";
    }
}
