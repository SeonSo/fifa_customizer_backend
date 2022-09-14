package com.exam.fifa.crawling;

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
    @GetMapping("/list")
    public List<Players> getPlayersList() {
        return this.playersService.getPlayerList();
    }

    @GetMapping("/list/{playerId}")
    public Optional<Players> getPlayers(@PathVariable Long playerId) {
        return this.playersService.getPlayerById(playerId);
    }
}
