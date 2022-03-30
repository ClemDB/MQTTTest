package com.example.mqtttest;

public class Monstre {
    public int x;
    public int y;
    public int sens;
    public int distanceMax;
    public int distance;
    public boolean flag = true;

    public Monstre(int x, int y, int sens, int distance) {
        this.x = x;
        this.y = y;
        this.sens = sens;
        this.distanceMax = distance;
        this.flag = true;
        this.distance =0;
    }

    @Override
    public String toString() {
        return "Monstre{" +
                "x=" + x +
                ", y=" + y +
                ", sens=" + sens +
                ", distanceMax=" + distanceMax +
                ", distance=" + distance +
                ", flag=" + flag +
                '}';
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

    public int getSens() {
        return sens;
    }

    public void setSens(int sens) {
        this.sens = sens;
    }

    public int getDistanceMax() {
        return distanceMax;
    }

    public void setDistanceMax(int distanceMax) {
        this.distanceMax = distanceMax;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
