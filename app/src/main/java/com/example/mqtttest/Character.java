package com.example.mqtttest;

public class Character {

    String name;
    int level;
    int hp;
    int mp;
    int map;
    String cellule;

    public Character(String name, int level, int hp, int mp, int map, String cellule) {
        this.name = name;
        this.level = level;
        this.hp = hp;
        this.mp = mp;
        this.map = map;
        this.cellule = cellule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public String getCellule() {
        return cellule;
    }

    public void setCellule(String cellule) {
        this.cellule = cellule;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", hp=" + hp +
                ", mp=" + mp +
                ", map=" + map +
                ", cellule='" + cellule + '\'' +
                '}';
    }
}
