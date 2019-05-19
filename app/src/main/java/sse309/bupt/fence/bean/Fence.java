package sse309.bupt.fence.bean;

import sse309.bupt.fence.controller.LocationController;

public class Fence {


    public enum FenceType {
        circleFence,
        polygonFence
    }

    private Point centerPoint;
    private double radius;
    private double threshouldSpace;
    private double threshouldSpeed;
    private FenceType type;
    private LocationController locationController;
    private static Point center;
    private static double r;
    private static boolean online;

    public static boolean isOnline() {
        return online;
    }

    public static void setOnline(boolean online) {
        Fence.online = online;
    }



    public static Point getCenter() {
        return center;
    }

    public static void setCenter(Point center) {
        Fence.center = center;
    }

    public static double getR() {
        return r;
    }

    public static void setR(double r) {
        Fence.r = r;
    }


    public static String getFenceName() {
        return fenceName;
    }

    public static void setFenceName(String fenceName) {
        Fence.fenceName = fenceName;
    }

    private static String fenceName;

    public Fence(){

    }

    public Fence(Point centerPoint, double radius, double threshouldSpace, double threshouldSpeed, FenceType type) {
        this.centerPoint = centerPoint;
        this.radius = radius;
        this.threshouldSpace = threshouldSpace;
        this.threshouldSpeed = threshouldSpeed;
        this.type = type;
    }


    private void initialLocationController() {
        locationController = new LocationController(this);
    }

    public Point getCenterPoint() {
        return centerPoint;
    }

    public void setCenterPoint(Point centerPoint) {
        this.centerPoint = centerPoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getThreshouldSpace() {
        return threshouldSpace;
    }

    public void setThreshouldSpace(double threshouldSpace) {
        this.threshouldSpace = threshouldSpace;
    }

    public double getThreshouldSpeed() {
        return threshouldSpeed;
    }

    public void setThreshouldSpeed(double threshouldSpeed) {
        this.threshouldSpeed = threshouldSpeed;
    }

    public LocationController getLocationController() {
        return locationController;
    }

    public void setLocationController(LocationController locationController) {
        this.locationController = locationController;
    }

    public FenceType getType() {
        return type;
    }

    public void setType(FenceType type) {
        this.type = type;
    }


}
