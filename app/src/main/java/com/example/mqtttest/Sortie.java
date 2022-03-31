package com.example.mqtttest;

public class Sortie {

    int x, y;
    boolean check;

    public Sortie(int x, int y) {
        this.x = x;
        this.y = y;
        this.check = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "Sortie{" +
                "x=" + x +
                ", y=" + y +
                ", check=" + check +
                '}';
    }
}
