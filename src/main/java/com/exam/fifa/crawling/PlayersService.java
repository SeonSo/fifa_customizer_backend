package com.exam.fifa.crawling;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayersService {
    private static String fifaSite = "https://sofifa.com/";
    private static String fifaDatabaseUrl = "https://sofifa.com/players?type=all&lg%5B%5D=83";
    private final PlayersRepository playersRepository;

    @PostConstruct
    @Transactional
    public List<Players> getFifaDatabases() throws IOException {

        List<Players> playersList = new ArrayList<>();

        for(int i=0; i<=300; i = i + 60){
            String offset = "&offset="+i;
            Document doc = Jsoup.connect(fifaDatabaseUrl+offset).get();
            Elements contents = doc.select("table tbody tr");

            for(Element content : contents) {
                Elements tdContents = content.select("td");

                Players players = Players.builder()
                        .playerName(tdContents.get(1).getElementsByClass("ellipsis").text())
                        .playerImg(tdContents.get(0).getElementsByTag("img").attr("data-src"))
                        .position(tdContents.get(1).getElementsByTag("span").text())
                        .team(tdContents.get(5).getElementsByTag("a").text())
                        .teamImg(tdContents.get(5).getElementsByTag("img").attr("data-src"))
                        .overall(Integer.parseInt(tdContents.get(3).text()))
                        .build();
                playersList.add(players);
                playersRepository.save(players);
            }
        }
        return playersList;
    }

    @Transactional
    public List<Players> getPlayerList() {
        return this.playersRepository.findAll();
    }

    @Transactional
    public Optional<Players> getPlayerById(Long playerId) {
        return this.playersRepository.findById(playerId);
    }
}
