package com.exam.fifa.security.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.exam.fifa.member.Member;
import com.exam.fifa.member.MemberRepository;
import com.exam.fifa.member.dto.ResponseDto;
import com.exam.fifa.security.authentication.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private MemberRepository memberRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
    }

    // 인증이나 권한이 필요한 주소요청시 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {


        System.out.println("권한 필터");

        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader" + jwtHeader);

        if (jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {

            System.out.println("토큰없음");
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        System.out.println("header가 token으로 변환됨");

        String email = null;
        try {
            email =
                    JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("email").asString();
        } catch (TokenExpiredException e) {
            System.out.println("토큰 만료됨");
            ResponseDto responseDto = new ResponseDto();
            responseDto.setCode("TOKEN-0001");
            responseDto.setMessage("token has expired");
            responseDto.setStatus(HttpStatus.UNAUTHORIZED);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));

        }

        System.out.println("email");
        System.out.println(email);
        if (email != null) {

            Member memberEntity = memberRepository.findByEmail(email);

            PrincipalDetails principalDetails = new PrincipalDetails(memberEntity);
            System.out.println("memberEntity");
            System.out.println(memberEntity);
            System.out.println("principalDetails");
            System.out.println(principalDetails);

            // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 강제로 security의 세션에 접근하여 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        super.doFilterInternal(request, response, chain);
    }
}
