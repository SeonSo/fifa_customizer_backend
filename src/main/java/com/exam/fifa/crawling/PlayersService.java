package com.exam.fifa.crawling;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlayersService {
    private static String fifaDatabaseUrl = "https://kr.fifaaddict.com/fo4db?country=korea-republic&class=live";
    private final PlayersRepository playersRepository;

    @PostConstruct
    public List<Players> getFifaDatabases() throws IOException {

        List<Players> playersList = new ArrayList<>();
        Document doc = Jsoup.connect(fifaDatabaseUrl).get();
        Elements contents = doc.select("table tbody tr");

        for(Element content : contents) {
            Elements tdContents = content.select("td");

            Players players = Players.builder()
                    .playerName(tdContents.get(1).getElementsByClass("player-name").text())
                    .playerImg(tdContents.get(1).getElementsByTag("img").attr("src"))
                    .position(tdContents.get(0).text())
                    .team(tdContents.get(1).getElementsByClass("crests small crests-team").attr("title"))
                    .teamImg(tdContents.get(1).getElementsByClass("crests small crests-team").attr("src"))
                    .overall(Integer.parseInt(tdContents.get(4).text()))
                    .build();
            playersList.add(players);
            playersRepository.save(players);
        }
        return playersList;
    }
}
