package com.exam.fifa.member;

import com.exam.fifa.exception.ApiRequestException;
import com.exam.fifa.member.token.TokenDto;
import com.exam.fifa.member.token.TokenResponseDto;
import com.exam.fifa.member.token.refreshToken.RefreshToken;
import com.exam.fifa.member.token.refreshToken.RefreshTokenRepository;
import com.exam.fifa.security.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.exam.fifa.member.token.TokenResponseDto.createMemberTokenResponseDto;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto requestDto){
        String email = requestDto.getEmail();
        String username = requestDto.getUsername();

        if (memberRepository.existsByEmail(email)) {
            throw new ApiRequestException("이미 가입되어 있는 유저입니다");
        }

        // 회원 email(ID)중복확인
        Optional<Member> found = memberRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new ApiRequestException("중복된 사용자 email(ID)가 존재합니다.");
        }

        // 닉네임 중복확인
        existUsername(username);

        // 패스워드 인코딩
        String password = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(password);

        Member member = Member.createMember(requestDto);
        memberRepository.save(member);
    }

    // 로그인
    @Transactional
    public TokenResponseDto login(LoginRequestDto requestDto){
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.authenticationToken();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        Member member = getMemberByEmail(requestDto.getEmail());

        return createMemberTokenResponseDto(tokenDto, member);
    }

    // 닉네임 중복확인
    private void existUsername(String username){
        if(memberRepository.existsByUsername(username)){
            throw  new ApiRequestException("이미 존재하는 닉네임입니다.");
        }
    }

    // 이메일로 멤버 찾기
    private Member getMemberByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new ApiRequestException("멤버를 찾을수없는 이메일입니다.")
        );
    }
}

