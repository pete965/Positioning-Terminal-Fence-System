package sse309.bupt.fence.bean;

public class LocationEntity {
    private String username;
    private double longtitude;
    private double latitude;

    public LocationEntity(String username, double longtitude, double latitude) {
        this.username = username;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
