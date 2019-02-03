package com.rest.api.leaguetracker.restapileaguetracker.objects;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "PLAYER")
public
class Player {

    private @Id @GeneratedValue Long id;

    private String name;
    private int totalPoints;
    private int bonusMoney;
//    private int seasonId;

    public Player(String name, int totalPoints, int bonusMoney) {
        this.name = name;
        this.totalPoints = totalPoints;
        this.bonusMoney = bonusMoney;
//        this.seasonId = seasonId;
    }

    Player() {}
}