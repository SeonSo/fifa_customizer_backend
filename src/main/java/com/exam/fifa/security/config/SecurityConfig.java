package com.exam.fifa.security.config;

import com.exam.fifa.member.MemberRepository;
import com.exam.fifa.security.config.jwt.JwtAuthenticationFilter;
import com.exam.fifa.security.config.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return
                // CSRF 설정 Disable
                http
                        .csrf().disable()
                        // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .and()

                        //.addFilter(corsFilter)// @CrossOrigin 어노테이션을 컨트롤러에 걸면 인증이 없을때 작동함, 인증이 있을때는 시큐리티 필터에 cors설정을 걸어줘야함
                        .formLogin().disable()
                        .httpBasic().disable()
                        .apply(new MyCustomDsl())
                        .and()
                        //권한설정
                        .authorizeRequests(authorize -> authorize
                                .antMatchers("/api/v1/member/**")
                                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                                .antMatchers("/api/v1/manager/**")
                                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                                .antMatchers("/api/v1/admin/**")
                                .access("hasRole('ROLE_ADMIN')")
                                .anyRequest().permitAll()
                        )
                        .build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository));
        }
    }
}
