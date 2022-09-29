package com.exam.fifa.player;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamsDto {
    private String team;
    private String teamImg;
    private Long playerCount;
    private double teamOva;

    @QueryProjection
    public TeamsDto(String team, String teamImg, Long playerCount, double teamOva) {
        this.team = team;
        this.teamImg = teamImg;
        this.playerCount = playerCount;
        this.teamOva = teamOva;
    }
}
