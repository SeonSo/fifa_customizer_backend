package com.exam.fifa.player;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamsDto {
    private String team;
    private String teamImg;
    private Integer playerCount;
    private Integer teamOva;

    @QueryProjection
    public TeamsDto(String team, String teamImg) {
        this.team = team;
        this.teamImg = teamImg;
    }



}
