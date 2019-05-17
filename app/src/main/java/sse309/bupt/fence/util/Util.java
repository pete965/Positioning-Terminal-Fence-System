package sse309.bupt.fence.util;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sse309.bupt.fence.bean.Fence;
import sse309.bupt.fence.bean.LocationPoint;

import static android.content.ContentValues.TAG;

public class Util {
    /**
     * @param start 开始时间
     * @param end   结束时间
     * @return 格式化时间差
     */
    public static double caculateTime(Date start, Date end) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = start.getTime() - end.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        double index_d = hour * (double) 1 / 24;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        double index_i = min * ((double) 1 / 24 / 60);
        //计算差多少秒
        long seconds = diff % nd % nh % nm / ns;
        double index_s = seconds * ((double) 1 / 24 / 60 / 60);

        return day + index_d + index_i + index_s;
    }

    /**
     * 判断是否在电子围栏
     *
     * @param point 某定位点
     * @param fence 电子围栏
     * @return
     */

    public static boolean isInFence(LocationPoint point,Fence fence){
        double distance=DistanceUtil.getDistance(new LatLng(point.getX(),point.getY()),new LatLng(fence.getCenterPoint().getLatitude(),fence.getCenterPoint().getLongtitude()));
        boolean isInSpace=distance<(fence.getRadius()+fence.getThreshouldSpace());
        BigDecimal shortDistance=new BigDecimal(distance+"");
        Log.i(TAG,"Boyang"+"pointLat:"+point.getX()+"pointLong:"+point.getY()+"centerLat:"+fence.getCenterPoint().getLatitude()+"centerLong:"+fence.getCenterPoint().getLongtitude());
        return isInSpace;
    }

    /**
     *
     * @param pre 上一个定位点
     * @param cur 当前定位点
     * @return 速度（m/s） 方向为接近边缘的方向
     */
    public static double getSpeed(LocationPoint pre, LocationPoint cur) {
        // 获得两个时间的毫秒时间差异
        long diff = cur.getDate().getTime() - pre.getDate().getTime();
        double seconds = diff / 1000;
        double distance = Math.sqrt(Math.pow(cur.getX(), 2) + Math.pow(cur.getY(), 2)) -
                Math.sqrt(Math.pow(pre.getX(), 2) + Math.pow(pre.getY(), 2));
        double speed = distance / seconds;
        return speed;
    }


}
