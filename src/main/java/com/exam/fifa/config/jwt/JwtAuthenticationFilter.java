package com.exam.fifa.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.exam.fifa.member.Member;
import com.exam.fifa.principalDetail.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/login");
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도중");
        try {
            ObjectMapper om = new ObjectMapper();
            System.out.println("입력정보");
            Member member = om.readValue(request.getInputStream(), Member.class);
            System.out.println(member);

            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            // PrincipalDetailsService의 loadUserByEmail() 함수가 실행된 후 정상이면 authentication이 리턴됨.
            // DB에 있는 email과 password가 일치한다
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // => 로그인이 되었다는 뜻
            PrincipalDetails principalDetails= (PrincipalDetails) authentication.getPrincipal();
            System.out.println("=================");
            System.out.println("로그인 완료됨: " + principalDetails.getMember().getNickname()); // 로그인 정상적으로 되었다는뜻
            // authentication 객체가 session 영역에 저장됨.
            // 리턴의 이유는 권한 관리를 security가 대신 해주기때문에 편하려고 하는거임
            // 굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리때문에 session 넣어준다.

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("login token")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getMember().getMemberId())
                .withClaim("email", principalDetails.getMember().getEmail())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        response.addHeader("Authorization", JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
