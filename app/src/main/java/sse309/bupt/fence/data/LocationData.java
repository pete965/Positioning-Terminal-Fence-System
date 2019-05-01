package sse309.bupt.fence.data;

import java.util.ArrayList;
import java.util.List;

import sse309.bupt.fence.bean.Point;

public class LocationData {
    private static List<Point> normal_inner = new ArrayList<>();
    public static List<Point> getNromalInnerData(){
        normal_inner.add(new Point(1099,1000)) ;
        normal_inner.add(new Point(1099.2,1000)) ;
        normal_inner.add(new Point(1099.3,1000)) ;
        normal_inner.add(new Point(1099.4,1000)) ;
        normal_inner.add(new Point(1099.5,1000)) ;
        normal_inner.add(new Point(1099.6,1000)) ;
        normal_inner.add(new Point(1099.7,1000)) ;
        normal_inner.add(new Point(1099.8,1000)) ;
        normal_inner.add(new Point(1099.9,1000)) ;
        normal_inner.add(new Point(1099.99,1000)) ;
        normal_inner.add(new Point(1098,1000)) ;
        normal_inner.add(new Point(1097,1000)) ;
        normal_inner.add(new Point(1000,1033)) ;
        normal_inner.add(new Point(1000,1077)) ;
        normal_inner.add(new Point(1000,1045)) ;
        normal_inner.add(new Point(1000,1044)) ;
        normal_inner.add(new Point(1000,1042)) ;
        normal_inner.add(new Point(1000,1011)) ;
        return normal_inner;
    }
}
