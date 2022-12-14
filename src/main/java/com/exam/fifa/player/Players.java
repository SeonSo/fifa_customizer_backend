package com.exam.fifa.player;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Entity
@Getter
@NoArgsConstructor
public class Players {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "player_id")
    private Long playerId;
    private String playerName;
    private String playerImg;
    private String position;
    private String team;
    private String teamImg;
    private Integer overall;

    @Builder
    public Players(String playerName, String playerImg, String position,
                   String team, String teamImg, Integer overall) {
        this.playerName = playerName;
        this.playerImg = playerImg;
        this.position = position;
        this.team = team;
        this.teamImg = teamImg;
        this.overall = overall;
    }
}
