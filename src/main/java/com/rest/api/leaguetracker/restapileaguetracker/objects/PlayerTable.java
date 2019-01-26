package com.rest.api.leaguetracker.restapileaguetracker.objects;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "PLAYER_TABLE")
public class PlayerTable {

    private @Id @GeneratedValue Long id;

    @ElementCollection
    private List<String> players;

    public PlayerTable(List<String> players) {
        this.players = players;
    }
}
