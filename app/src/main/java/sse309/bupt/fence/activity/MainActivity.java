package sse309.bupt.fence.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.List;

import sse309.bupt.fence.Config;
import sse309.bupt.fence.R;
import sse309.bupt.fence.RadiusView;
import sse309.bupt.fence.bean.Fence;
import sse309.bupt.fence.bean.Point;
import sse309.bupt.fence.bean.User;
import sse309.bupt.fence.controller.LocationController;
import sse309.bupt.fence.controller.LogController;
import sse309.bupt.fence.core.InfoController;
import sse309.bupt.fence.data.LocationData;
import sse309.bupt.fence.outdoorMap.BaiduMapHelper;
import sse309.bupt.fence.outdoorMap.MyOrientationListener;

public class MainActivity extends Activity implements View.OnTouchListener {
    private Fence mFence;
    private LocationController mLocationController;
    private MyOrientationListener myOrientationListener;
    private MapView mMapView;
    private Button bt_start;
    private Button bt_edit;
    private LinearLayout line;
    private TextView tv_longti;
    private TextView tv_lati;
    private TextView tv_distance;
    private TextView tv_speed;
    private TextView tv_time;
    private TextView tv_thd;
    private TextView tv_thv;
    private TextView tv_ri;
    private TextView tv_si;
    private TextView tv_bdi;
    private TextView tv_bti;
    private Button bt_config;
    private LinearLayout linearLayout_displayMode;
    private LinearLayout linearLayout_editMode;
    public static RadiusView radiusView;
    public static double radius_paint;
    public static LatLng center_paint;
    public static SharedPreferences sp;
    public static JSONArray array = new JSONArray();
    private LogController logController;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("CLICK", "x = " + motionEvent.getX() + "y = " + motionEvent.getY());
                break;
        }
        return false;
    }


    public interface onGetlocateListenner {
        void onGetSafeLocate();
        void onGeetUnSafeLocate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initInfos();
        initFence();
        initLogController();

    }

    private void initLogController() {
        logController = new LogController(this,mLocationController);
    }

    private void initInfos() {
        String breakData = InfoController.getJson(this, "breakData.json");
        User.getInstance().setBreakInfos(InfoController.parseBreakFenceInfo(breakData));
    }

    private void initData() {
        sp = getSharedPreferences("USERDATA", Activity.MODE_PRIVATE);
        double longtitude = sp.getFloat("center_longtitude", 0);
        double latitude = sp.getFloat("center_latitude", 0);
        center_paint = new LatLng(latitude, longtitude);
        radius_paint = sp.getFloat("radius", 0);
        Config.n_threshouldspace = sp.getInt("n_threshouldspace", 2);
        Config.n_threshouldspeed = sp.getInt("n_threshouldspeed", 2);
        Config.n_time = sp.getInt("n_time", 2);
        Config.n_boundary = sp.getInt("n_boundary", 2);
        Config.gravity_bdi = sp.getInt("gravity_bdi", 1);
        Config.gravity_bti = sp.getInt("gravity_bti", 1);
        Config.base_threshouldspace = Double.valueOf(sp.getString("base_threshouldspace", "10"));
        Config.base_threshouldspeed = Double.valueOf(sp.getString("base_threshouldspeed", "1"));
        Config.sigma = Double.valueOf(sp.getString("sigma", "30"));
        Config.miu = Double.valueOf(sp.getString("miu", "0"));
        Config.t_max = Double.valueOf(sp.getString("t_max", "0.5"));

    }

    private void initFence() {
        Point start = new Point(0, 0);
        mFence = new Fence(start, radius_paint, Config.base_threshouldspace, Config.base_threshouldspeed, Fence.FenceType.circleFence);
        mLocationController = new LocationController(mFence);
        mLocationController.setData(LocationData.getNromalInnerData());
        mLocationController.setOnGetlocateListenner(new onGetlocateListenner() {
            @Override
            public void onGetSafeLocate() {
                tv_longti.setText("LONGTI:" + mLocationController.getY());
                tv_lati.setText("LATI:" + mLocationController.getX());
                tv_distance.setText("DIS:" + mLocationController.getBoundaryDistance());
                tv_speed.setText("SPE:" + mLocationController.getSpeed());
                tv_time.setText("TIME:" + mLocationController.getBoundaryTime());
                tv_thd.setText("THD:" + mLocationController.getFence().getThreshouldSpace());
                tv_thv.setText("THV:" + mLocationController.getFence().getThreshouldSpeed());
                tv_ri.setText("RI:" + mLocationController.getRiskIndex());
                tv_si.setText("SI:" + User.getInstance().getStatisticIndex());
                tv_bdi.setText("BDI:" + mLocationController.getBoundaryDistanceIndex());
                tv_bti.setText("BTI:" + mLocationController.getBoundaryTimeIndex());
                paintBoundary();
                logController.addLog();
            }

            @Override
            public void onGeetUnSafeLocate() {
                //停止定位
                mLocationController.stop();
                //显示已走出围栏
                Toast.makeText(getApplicationContext(), mLocationController.getX()
                        + " " +  mLocationController.getY() + "出围栏", Toast.LENGTH_SHORT).show();
                //TODO 更新历史信息
                logController.save();
            }

        });
        BaiduMapHelper.getInstance().setLocationController(mLocationController);
        if (hasFenceData()) {
            paintBoundary();
        }
    }


    private void initView() {
        //百度地图
        mMapView = (MapView) findViewById(R.id.mmap);
        mMapView.setOnTouchListener(this);
        BaiduMapHelper.getInstance().initMap(getApplicationContext(), mMapView);
        //绑定光标方向
        myOrientationListener = new MyOrientationListener(this);
        BaiduMapHelper.getInstance().setCursorDirection(myOrientationListener);
        // BaiduMapHelper.getInstance().setCurrentPositionButton(getView().findViewById(R.id.iv_current_location));

        //参数显示区
        line = findViewById(R.id.line);
        tv_longti = findViewById(R.id.tv_longti);
        tv_lati = findViewById(R.id.tv_lati);
        tv_distance = findViewById(R.id.tv_distance);
        tv_speed = findViewById(R.id.tv_speed);
        tv_time = findViewById(R.id.tv_time);
        tv_ri = findViewById(R.id.tv_ri);
        tv_si = findViewById(R.id.tv_si);
        tv_bdi = findViewById(R.id.tv_bdi);
        tv_bti = findViewById(R.id.tv_bti);
        tv_thd = findViewById(R.id.tv_thd);
        tv_thv = findViewById(R.id.tv_thv);

        //按扭区
        linearLayout_displayMode = findViewById(R.id.linearLayout_displayMode);
        linearLayout_editMode = findViewById(R.id.linearLayout_editMode);

        //按钮，开始定位
        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mLocationController.isRuning()) {
                    line.setVisibility(View.GONE);
                    mLocationController.stop();
                    initFence();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            paintBoundary();
                        }
                    });
                    bt_start.setText("开 始");
                    bt_start.setBackgroundColor(Color.parseColor("#aaee4863"));

                } else {
                    line.setVisibility(View.GONE);

                    mLocationController.start();
                    bt_start.setText("停 止");
                    bt_start.setBackgroundColor(Color.parseColor("#aa61649f"));

                }
            }
        });
        bt_edit = findViewById(R.id.bt_edit);
        //按钮，开始编辑
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaiduMapHelper.getInstance().setMapType(BaiduMapHelper.MapType.EDITMODE);
                linearLayout_displayMode.setVisibility(View.GONE);
                linearLayout_editMode.setVisibility(View.VISIBLE);
                bt_config.setVisibility(View.GONE);
            }
        });
        Button bt_sure = findViewById(R.id.bt_sure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaiduMapHelper.getInstance().setMapType(BaiduMapHelper.MapType.DISPLAYMODE);
                linearLayout_displayMode.setVisibility(View.VISIBLE);
                linearLayout_editMode.setVisibility(View.GONE);
                bt_config.setVisibility(View.VISIBLE);
                SharedPreferences.Editor editor = MainActivity.sp.edit();
                editor.putFloat("radius", (float) radius_paint);
                editor.commit();
                //重新初始化Fence

                initFence();
            }
        });

        Button bt_clear = findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaiduMapHelper.getInstance().getBaiduMap().clear();
                center_paint = new LatLng(0, 0);
                radius_paint = 0;
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

            }
        });

        bt_config = findViewById(R.id.bt_config);
        bt_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });


        //半径输入框
        radiusView = new RadiusView(this, new RadiusView.onClickSureListener() {
            @Override
            public void onClick(final double radius) {
                radius_paint = radius;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        radiusView.cancel();
                        BaiduMapHelper.getInstance().getBaiduMap().clear();
                        paintNormalFence();
                    }
                });

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
            BaiduMapHelper.getInstance().stopLocation();
        }
        if (myOrientationListener != null) {
            myOrientationListener.stop();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
            BaiduMapHelper.getInstance().startLocation();
        }
        if (myOrientationListener != null) {
            myOrientationListener.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        mMapView = null;
    }

    public static boolean hasFenceData() {
        if (center_paint.latitude != 0 && center_paint.longitude != 0 && radius_paint != 0) {
            return true;
        }
        return false;
    }

    public void paintNormalFence() {
        BaiduMapHelper.getInstance().getBaiduMap().clear();
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x88ee4866)
                .center(center_paint).radius((int) radius_paint);
        BaiduMapHelper.getInstance().getBaiduMap().addOverlay(ooCircle);
    }

    public void paintBoundary() {
        BaiduMapHelper.getInstance().getBaiduMap().clear();

        //最外
        OverlayOptions b = new CircleOptions().fillColor(0x000000FF).center(center_paint).
                stroke(new Stroke(2, 0xAA2F90B9)).radius((int) ((int) mFence.getRadius() + Config.base_threshouldspace));
        BaiduMapHelper.getInstance().getBaiduMap().addOverlay(b);
        //绘制外圆
        OverlayOptions ooCircle = new CircleOptions().fillColor(0x88ee4866)
                .center(center_paint).radius((int) mFence.getRadius() + (int) mFence.getThreshouldSpace());
        BaiduMapHelper.getInstance().getBaiduMap().addOverlay(ooCircle);
        //原本大小
        OverlayOptions a = new CircleOptions().fillColor(0x000000FF)
                .center(center_paint).stroke(new Stroke(2,0xAACDD1D5)).radius((int) mFence.getRadius());
        BaiduMapHelper.getInstance().getBaiduMap().addOverlay(a);
        //绘制内圆
        OverlayOptions outerCircle = new CircleOptions().fillColor(0x88f1f0ed)
                .center(center_paint).radius((int) mFence.getRadius() - (int) Config.base_threshouldspace);
        BaiduMapHelper.getInstance().getBaiduMap().addOverlay(outerCircle);


    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * <p>
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initFence();
    }

}
