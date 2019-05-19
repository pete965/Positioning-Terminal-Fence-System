package sse309.bupt.fence.outdoorMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.Date;

import sse309.bupt.fence.R;
import sse309.bupt.fence.activity.MainActivity;
import sse309.bupt.fence.bean.Fence;
import sse309.bupt.fence.bean.LocationPoint;
import sse309.bupt.fence.bean.LoginEntity;
import sse309.bupt.fence.communication.RequestHelper;
import sse309.bupt.fence.controller.LocationController;
import sse309.bupt.fence.util.Util;

import static sse309.bupt.fence.activity.MainActivity.radiusView;

/**
 * Created by jason on 12/03/2018.
 */

public class BaiduMapHelper {
    private static BaiduMapHelper mBaiduMapHelper = new BaiduMapHelper();
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private BDAbstractLocationListener myListener;
    private float direction = 0;
    private static BDLocation location;
    private boolean isFirstLoc = true;
    private LatLng latLng;
    private MapType mapType = MapType.DISPLAYMODE;
    private LocationController locationController;


    public enum MapType {
        EDITMODE, DISPLAYMODE
    }

    public interface OnAirportClickListener {
        void jumpToInnerMap();
    }

    public static BaiduMapHelper getInstance() {
        return mBaiduMapHelper;
    }

    public void initMap(Context applicationContext, MapView mMapView) {

        mBaiduMap = mMapView.getMap();

        // 开启定位图层1
        mBaiduMap.setMyLocationEnabled(true);

        //禁止显示比例尺
        mMapView.showScaleControl(false);

        //禁止缩放条
        mMapView.showZoomControls(false);

        /*设置地图控件*/
        UiSettings mUiSettings = mBaiduMap.getUiSettings();

        //禁止3D旋转
        mUiSettings.setOverlookingGesturesEnabled(false);

        mLocationClient = new LocationClient(applicationContext);

        //声明LocationClient类
        myListener = new MyOutdoorLocationListener(mBaiduMap);
        mLocationClient.registerLocationListener(myListener);

        //注册监听函数
        initLocationOption();

        mBaiduMap.setOnMapClickListener(listener);
    }

    BaiduMap.OnMapClickListener listener = new BaiduMap.OnMapClickListener() {
        /**
         * 地图单击事件回调函数
         * @param point 点击的地理坐标
         */
        public void onMapClick(LatLng point) {
            if (mapType == MapType.EDITMODE) {
                if (MainActivity.center_paint.longitude == 0 && MainActivity.center_paint.latitude == 0) {
                    MainActivity.center_paint = point;
                    SharedPreferences.Editor editor = MainActivity.sp.edit();
                    editor.putFloat("center_longtitude", (float) point.longitude);
                    editor.putFloat("center_latitude", (float) point.latitude);
                    editor.commit();
                    mBaiduMap.clear();
                    //绘制marker
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.outline_add_location_black_18dp);

                    //构建MarkerOption，用于在地图上添加Marker
                    OverlayOptions option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);

                    //在地图上添加Marker，并显示
                    mBaiduMap.addOverlay(option);

                } else {
                    radiusView.show();
                }
            }

        }

        /**
         * 地图内 Poi 单击事件回调函数
         * @param poi 点击的 poi 信息
         */
        public boolean onMapPoiClick(MapPoi poi) {
            return true;
        }
    };

    public void setCursorDirection(MyOrientationListener myOrientationListener) {
        //为系统的方向传感器注册监听器

        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                direction = x;
                Log.i("Direction", "Direction=" + x + "");
            }
        });

    }

    public void setCurrentPositionButton(View mLocationButton) {
        //定位按钮
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义地图状态
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(latLng)
                        .zoom(15)
                        .rotate(0)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
            }
        });

    }

    private void initLocationOption() {
        Log.i("outdoor_location", "initLocationOption");
        LocationClientOption option = new LocationClientOption();
        //设置定位模式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，设置返回经纬度坐标类型，默认gcj02
        option.setCoorType("bd09ll");

        //可选，设置发起定位请求的间隔，int类型，单位ms
        option.setScanSpan(1000);

        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);

        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setLocationNotify(true);

        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setIgnoreKillProcess(false);

        //可选，设置是否收集Crash信息，默认收集，即参数为false
        option.SetIgnoreCacheException(false);

        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
        option.setWifiCacheTimeOut(5 * 60 * 1000);

        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
        option.setEnableSimulateGps(false);


        mLocationClient.setLocOption(option);

    }

    public class MyOutdoorLocationListener extends BDAbstractLocationListener {
        private boolean isFirstLoc = true;
        private BaiduMap mBaiduMap;

        public MyOutdoorLocationListener(BaiduMap mBaiduMap) {
            this.mBaiduMap = mBaiduMap;
        }

        @Override
        public void onReceiveLocation(BDLocation loc) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果

            //获取经纬度信息
            double latitude = loc.getLatitude();
            double longtitude = loc.getLongitude();
            String username= LoginEntity.getUser();
            Date date=new Date();
            RequestHelper requestHelper=new RequestHelper();
            if(new Fence().isOnline()){requestHelper.sendLocation(latitude,longtitude,username);}
            latLng = new LatLng(latitude, longtitude);
            Log.i("point", "latitude = " + latitude + "longitude = " + longtitude);

            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = loc.getCoorType();

            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = loc.getLocType();

            //地图显示定位
            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatus mMapStatus = new MapStatus.Builder()
                        .target(latLng)
                        .zoom(15)
                        .build();
                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                //改变地图状态
                mBaiduMap.animateMapStatus(mMapStatusUpdate);
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(loc.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-306
                    .direction(direction).latitude(loc.getLatitude())
                    .longitude(loc.getLongitude()).build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL, true, null));
            if (locationController.isRuning()) {
                double speed;
                LocationPoint locationPoint = new LocationPoint(latitude, longtitude, 0, date);
                if (locationController.getLastPoint() == null) {
                    speed = 0;
                } else {
                    speed = Util.getSpeed(locationController.getLastPoint(), locationPoint);
                    locationPoint.setSpeed(speed);
                }
                //TODO 在这里持久化 发送
                locationController.dealLocation(locationPoint);
                locationController.setLastPoint(locationPoint);
            }
        }
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public MapType getMapType() {
        return mapType;
    }

    public void setLocationController(LocationController locationController) {
        this.locationController = locationController;
    }

    public void stopLocation() {
        if (mLocationClient.isStarted() == true) {
            mLocationClient.stop();
        }
    }

    public void startLocation() {
        if (mLocationClient.isStarted() == false) {
            mLocationClient.start();
        }

    }

    public BaiduMap getBaiduMap() {
        return mBaiduMap;
    }
}
