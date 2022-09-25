package com.exam.fifa.member;

public class MemberResponseDto {
    private Long memberId;
    private String username;
    private String profileImg;

    public MemberResponseDto(Member member){
        this.memberId = member.getMemberId();
        this.username = member.getUsername();
        this.profileImg = member.getProfileImg();
    }
}
