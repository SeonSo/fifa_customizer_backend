package com.exam.fifa.member;

import com.exam.fifa.exception.ApiRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    @PostMapping("/v1/signup")
    public String signup(@RequestBody SignupRequestDto signupRequestDto, Member member) {

        String email = signupRequestDto.getEmail();
        String username = signupRequestDto.getUsername();

        if (memberRepository.existsByEmail(email)) {
            throw new ApiRequestException("exists same Email");
        }

        Optional<Member> found = memberRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new ApiRequestException("exists same user email");
        }

        signupRequestDto.setPassword(bCryptPasswordEncoder.encode(signupRequestDto.getPassword()));
        memberRepository.save(member);

        return "환영합니다 회원가입이 완료되었습니다.";
    }

    @GetMapping("/v1/member")
    public String member() {
        return "member";
    }

    @GetMapping("/v1/admin")
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
