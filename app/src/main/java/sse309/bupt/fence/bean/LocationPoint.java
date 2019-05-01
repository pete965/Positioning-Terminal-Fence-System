package sse309.bupt.fence.bean;

import java.util.Date;

public class LocationPoint {
    private double x;
    private double y;
    private double speed;
    private Date date;

    public LocationPoint(double x, double y, double speed, Date date) {
        this.y = y;
        this.x = x;
        this.speed = speed;
        this.date = date;
    }


    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
