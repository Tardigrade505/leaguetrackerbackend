package com.rest.api.leaguetracker.restapileaguetracker;

import com.rest.api.leaguetracker.restapileaguetracker.objects.Player;
import com.rest.api.leaguetracker.restapileaguetracker.objects.Season;
import com.rest.api.leaguetracker.restapileaguetracker.repositories.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(SeasonRepository repository) {
        return args -> {
            List<Player> season1PlayerList = Arrays.asList(new Player("Elliot", 0, 0),
                    new Player("Ryan", 0, 0), new Player("Zack", 0, 0));
            List<Player> season2PlayerList = Arrays.asList(new Player("Elliot", 4, 0),
                    new Player("Ryan", 0, 0), new Player("Zack", 2, 0),
                    new Player("Peter", 0, 0), new Player("Brandon", 1, 0),
                    new Player("Mario", 5, 0));
            log.info("Preloading " + repository.save(new Season("Brawlin' on a Budget", season1PlayerList, null)));
            log.info("Preloading " + repository.save(new Season("Commander vs. the World", season2PlayerList, null)));
        };
    }
}