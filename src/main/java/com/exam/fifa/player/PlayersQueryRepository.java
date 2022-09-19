package com.exam.fifa.player;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static com.exam.fifa.player.QPlayers.players;

@RequiredArgsConstructor
@Repository
public class PlayersQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<TeamsDto> findAllByTeam() {
        return queryFactory.select(new QTeamsDto(
                        players.team,
                        players.teamImg
                ))
                .from(players)
                .groupBy(players.team)
                .fetch();
    }

    public List<Players> findPlayersByTeam(String team) {
        return queryFactory.selectFrom(players)
                .where(players.team.eq(team))
                .fetch();
    }

    public List<Players> findPlayerByName(String name) {
        return queryFactory.selectFrom(players)
                .distinct()
                .where(players.playerName.contains(name))
                .fetch();
    }

    public List<Players> findPlayerByPosition(String position) {
        return queryFactory.selectFrom(players)
                .distinct()
                .where(players.position.contains(position))
                .fetch();
    }

//    public List<Players> searchPlayerByTag(String name, String team, String position) {
//        return queryFactory.selectFrom(players)
//                .distinct()
//                .where(
//                        players.playerName.contains(name),
//                        players.team.eq(team),
//                        players.position.eq(position))
//                .fetch();
//    }
}
