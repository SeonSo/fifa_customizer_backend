package com.exam.fifa.player;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlayersController {
    private final PlayersService playersService;

    @GetMapping("/test")
    public String getTest() {
        return "Test";
    }
    @GetMapping("/player")
    public List<Players> getPlayersList() {
        return this.playersService.getPlayerList();
    }

    @GetMapping("/team")
    public List<TeamsDto> getTeams() {
        return this.playersService.getTeamList();
    }

    @GetMapping("/player/{playerId}")
    public Optional<Players> getPlayers(@PathVariable Long playerId) {
        return this.playersService.getPlayerById(playerId);
    }

    @GetMapping("/search/name/{playerName}")
    public List<Players> getPlayerName(@PathVariable String playerName) {
        return this.playersService.getPlayerName(playerName);
    }

    @GetMapping("/search/position/{position}")
    public List<Players> getPlayerPosition(@PathVariable String position) {
        return this.playersService.getPlayerPosition(position);
    }

    @GetMapping("/search/team/{team}")
    public List<Players> getPlayerTeam(@PathVariable String team) {
        return this.playersService.getPlayerTeam(team);
    }
}
