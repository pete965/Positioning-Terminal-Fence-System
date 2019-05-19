package sse309.bupt.fence.bean;

import java.util.ArrayList;

public class SelectFence {
    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    private String fenceName;
    private double r;
    private Point center;

    public static ArrayList<SelectFence> getFences() {
        return fences;
    }

    public static void setFences(ArrayList<SelectFence> fences) {
        SelectFence.fences = fences;
    }

    private static ArrayList<SelectFence> fences=new ArrayList<SelectFence>();

    public SelectFence(double r,Point center,String fenceName){
        this.r=r;
        this.center=center;
        this.fenceName=fenceName;
    }

    public SelectFence(){

    }

    public void storeNewFence(SelectFence selectFence){
        fences.add(selectFence);
    }

    public void initFences() {
        fences.clear();
    }
}
