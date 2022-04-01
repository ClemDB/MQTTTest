package com.example.mqtttest;

public class Leaderboard {
    String nom;
    int coups;
    int temps;

    public Leaderboard( String nom, int coups, int temps) {

        this.nom = nom;
        this.coups = coups;
        this.temps = temps;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCoups() {
        return coups;
    }

    public void setCoups(int coups) {
        this.coups = coups;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }
}
