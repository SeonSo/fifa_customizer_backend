package com.exam.fifa.player;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.exam.fifa.player.QPlayers.players;

@RequiredArgsConstructor
@Repository
public class PlayersQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<TeamsDto> findAllByTeam() {
        return queryFactory.select(new QTeamsDto(
                        players.team,
                        players.teamImg,
                        players.playerId.count(),
                        players.overall.round()
                ))
                .from(players)
                .groupBy(players.team)
                .fetch();
    }

    public List<Long> getPlayerCount(String team) {
        return queryFactory
                .select(players.playerId.count())
                .from(players)
                .where(players.team.eq(team))
                .groupBy(players.team)
                .fetch();
    }

    public List<Double> getTeamOva(String team) {
        return queryFactory
                .select(players.overall.avg())
                .from(players)
                .where(players.team.eq(team))
                .groupBy(players.team)
                .fetch();
    }

    public List<Players> searchPlayerByTag(String name, String team, String position) {

        return queryFactory.selectFrom(players)
                .distinct()
                .where(predicatesBySearch(name, team, position))
                .orderBy(players.overall.desc())
                .fetch();
    }

    private Predicate[] predicatesBySearch(String name, String team, String position) {
        Predicate[] predicates = new Predicate[2];
        if (name.equals("ALL")) {
            if (!team.equals("ALL") && !position.equals("ALL")) { // 포지션, 팀 search
                predicates = new Predicate[]{
                        players.team.eq(team),
                        players.position.contains(position)
                };
            } else if (team.equals("ALL") && !position.equals("ALL")) { // 포지션 search
                predicates = new Predicate[]{
                        players.position.contains(position)
                };
            } else if (!team.equals("ALL")) { // 팀 search
                predicates = new Predicate[]{
                        players.team.eq(team)
                };
            }
        } else {
            if (!team.equals("ALL") && !position.equals("ALL")) { // 포지션, 팀 search
                predicates = new Predicate[]{
                        players.playerName.contains(name),
                        players.team.eq(team),
                        players.position.contains(position)
                };
            } else if (team.equals("ALL") && !position.equals("ALL")) { // 포지션 search
                predicates = new Predicate[]{
                        players.playerName.contains(name),
                        players.position.contains(position)
                };
            } else if (!team.equals("ALL")) { // 팀 search
                predicates = new Predicate[]{
                        players.playerName.contains(name),
                        players.team.eq(team)
                };
            }
        }
        return predicates;
    }


}
