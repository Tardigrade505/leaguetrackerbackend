package com.rest.api.leaguetracker.restapileaguetracker.objects;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public
class Season {

    private @Id @GeneratedValue Long id;
    private String name;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Player> players;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Achievement> achievements;

    public Season(String name, List<Player> players, List<Achievement> achievements) {
        this.name = name;
        this.players = players;
        this.achievements = achievements;
    }

    /**
     * Adds a player to the season
     * @param player - the player to add
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    Season() {}
}