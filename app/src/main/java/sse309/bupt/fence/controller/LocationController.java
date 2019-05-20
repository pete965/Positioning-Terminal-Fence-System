package sse309.bupt.fence.controller;

import java.util.Date;
import java.util.List;

import sse309.bupt.fence.Config;
import sse309.bupt.fence.activity.MainActivity;
import sse309.bupt.fence.bean.Fence;
import sse309.bupt.fence.bean.LocationPoint;
import sse309.bupt.fence.bean.Point;
import sse309.bupt.fence.bean.User;
import sse309.bupt.fence.core.IndexController;
import sse309.bupt.fence.util.Util;

public class LocationController {
    private double x;
    private double y;
    private double speed;
    private double riskFactor;
    private double historical;
    private double boundaryDistance;
    private double boundaryTime;
    private Fence fence;
    private Date lastStayTime; //上一次停留的时间
    private LocationPoint lastPoint;
    private boolean isInBoundaryLast;
    private boolean isInBoundary;
    private double boundaryTimeIndex;
    private double boundaryDistanceIndex;
    private double riskIndex;
    private List<Point> data;
    private boolean isRuning = false;
    private MainActivity.onGetlocateListenner onGetlocateListenner;

    public LocationController(){

    }

    public static boolean isStart() {
        return start;
    }

    public static void setStart(boolean start) {
        LocationController.start = start;
    }

    private static boolean start=false;

    /**
     * @param fence 电子围栏实实例
     */
    public LocationController(Fence fence) {
        this.fence = fence;
        init();
        initLocation();
    }

    private void initLocation() {

    }

    /**
     * 初始化
     */
    private void init() {
        //计算统计系数
        User.getInstance().setStatisticIndex(IndexController.cacalateStatisticIndex());
        //初始化危险系数
        riskIndex = IndexController.caculateRiskIndex(0.0,
                boundaryTimeIndex, boundaryDistanceIndex);

        //初始化阈值
        fence.setThreshouldSpace(IndexController.caculateThreshouldSpace(riskIndex));
        fence.setThreshouldSpeed(IndexController.caculateThreshouldSpeed(riskIndex));
    }

    /**
     * 计算边界距离
     */
    private double caculateBoundaryDistance() {

        boundaryDistance = fence.getRadius() -
                Math.sqrt(Math.pow(y - fence.getCenterPoint().getLongtitude(), 2) +
                        Math.pow(x - fence.getCenterPoint().getLatitude(), 2));
        if (boundaryDistance > Config.base_threshouldspace) {
            isInBoundary = false;
        } else {
            isInBoundary = true;
        }
        return boundaryDistance;
    }

    /**
     * 计算边界时间
     */
    private void caculateBoundaryTime() {
        if (!isInBoundaryLast && isInBoundary) {
            isInBoundaryLast = true;
            lastStayTime = new Date(System.currentTimeMillis());
        }
        if (isInBoundaryLast && !isInBoundary) {
            isInBoundaryLast = false;
            //计算boundaryTime
            boundaryTime = Util.caculateTime(new Date(System.currentTimeMillis()), lastStayTime);
        }
        if (isInBoundaryLast && isInBoundary) {
            //计算boundaryTime
            boundaryTime = Util.caculateTime(new Date(System.currentTimeMillis()), lastStayTime);
        }
    }

    public static double getSpeed(LocationPoint start, LocationPoint end, Fence fence) {
        //假设时间间隔为1s
        double time = 1;
        double speed = ((Math.sqrt(Math.pow(start.getY() - fence.getCenterPoint().getLongtitude(), 2) +
                Math.pow(start.getX() - fence.getCenterPoint().getLatitude(), 2)))
                - (Math.sqrt(Math.pow(end.getY() - fence.getCenterPoint().getLongtitude(), 2) +
                Math.pow(end.getX() - fence.getCenterPoint().getLatitude(), 2)))) / time;
        return speed;
    }

    public void start() {
        isRuning = true;
        setStart(false);
        //读取坐标，模拟定位
    }

    public void stop() {
        System.out.println("xxx stop");
        isRuning = false;
        setStart(false);
        //停止读取坐标
    }

    private void caculateCurrectIndex() {

    }

    public double getRiskFactor() {
        return riskFactor;
    }

    public void setRiskFactor(double riskFactor) {
        this.riskFactor = riskFactor;
    }

    public double getHistorical() {
        return historical;
    }

    public void setHistorical(double historical) {
        this.historical = historical;
    }

    public double getBoundaryDistance() {
        return boundaryDistance;
    }

    public void setBoundaryDistance(double boundaryDistance) {
        this.boundaryDistance = boundaryDistance;
    }

    public double getBoundaryTime() {
        return boundaryTime;
    }

    public void setBoundaryTime(double boundaryTime) {
        this.boundaryTime = boundaryTime;
    }

    public void setData(List<Point> data) {
        this.data = data;
    }

    public boolean isRuning() {
        return isRuning;
    }

    public void setRuning(boolean runing) {
        isRuning = runing;
    }

    public void setOnGetlocateListenner(MainActivity.onGetlocateListenner onGetlocateListenner) {
        this.onGetlocateListenner = onGetlocateListenner;
    }

    public Fence getFence() {
        return fence;
    }

    public void dealLocation(LocationPoint locationPoint) {
        x = locationPoint.getX();
        y = locationPoint.getY();
        speed = locationPoint.getSpeed();
        if (Util.isInFence(locationPoint, fence)) {
            //计算空间系数
            caculateBoundaryDistance();
            boundaryDistanceIndex = IndexController.caculateBoundaryDistanceIndex(boundaryDistance,
                    fence.getThreshouldSpace());
            //计算时间系数
            caculateBoundaryTime();
            boundaryTimeIndex = IndexController.caculateBoundaryTimeIndex(boundaryTime);
            //计算危险系数
            User.getInstance().setStatisticIndex(0.0);
            riskIndex = IndexController.caculateRiskIndex(User.getInstance().getStatisticIndex(),
                    boundaryDistanceIndex, boundaryTimeIndex);
            //计算阈值
            fence.setThreshouldSpace(IndexController.caculateThreshouldSpace(riskIndex));
            onGetlocateListenner.onGetSafeLocate();
        } else {
            onGetlocateListenner.onGetUnSafeLocate();
        }

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

    public LocationPoint getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(LocationPoint lastPoint) {
        this.lastPoint = lastPoint;
    }

    public double getSpeed() {
        return speed;
    }

    public double getBoundaryTimeIndex() {
        return boundaryTimeIndex;
    }

    public void setBoundaryTimeIndex(double boundaryTimeIndex) {
        this.boundaryTimeIndex = boundaryTimeIndex;
    }

    public double getBoundaryDistanceIndex() {
        return boundaryDistanceIndex;
    }

    public void setBoundaryDistanceIndex(double boundaryDistanceIndex) {
        this.boundaryDistanceIndex = boundaryDistanceIndex;
    }

    public double getRiskIndex() {
        return riskIndex;
    }

public boolean getIsInBoundary(){
    return this.isInBoundary;
}
public boolean getIsInBoundaryLast(){
        return this.isInBoundaryLast;
}
}

