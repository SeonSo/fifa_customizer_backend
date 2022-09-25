package com.exam.fifa.member.token;

import com.exam.fifa.member.Member;
import com.exam.fifa.member.MemberResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;
    private MemberResponseDto userInfo;

    @Builder
    public TokenResponseDto(TokenDto tokenDto, Member member){
        this.grantType = tokenDto.getGrantType();
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        this.accessTokenExpiresIn = tokenDto.getAccessTokenExpiresIn();
        this.userInfo = new MemberResponseDto(member);
    }

    public static TokenResponseDto createMemberTokenResponseDto(TokenDto tokenDto,
                                                                Member member){
        return TokenResponseDto.builder()
                .tokenDto(tokenDto)
                .member(member)
                .build();
    }

}