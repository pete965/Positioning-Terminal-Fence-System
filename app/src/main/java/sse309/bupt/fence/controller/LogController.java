package sse309.bupt.fence.controller;

import android.content.Context;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import sse309.bupt.fence.Config;
import sse309.bupt.fence.activity.MainActivity;
import sse309.bupt.fence.bean.User;

import static sse309.bupt.fence.activity.MainActivity.center_paint;

public class LogController {
    private JSONObject log = new JSONObject();  //整个log
    private JSONArray logInfos = new JSONArray();  //log条目
    private Context context;
    private LocationController mLocationController;
    private int num = 0;
    private String path;
    private static String name;

    public LogController(Context context, LocationController mLocationController) {
        this.context = context;
        this.mLocationController = mLocationController;
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                context.getApplicationContext().getPackageName();
        init();
    }

    private void init() {
        try {
            log.put("radius", MainActivity.radius_paint + "");
            log.put("latitude", center_paint.latitude + "");
            log.put("n_threshouldspace", Config.n_threshouldspace + "");
            log.put("n_threshouldspeed", Config.n_threshouldspeed + "");
            log.put("n_time", Config.n_time + "");
            log.put("n_boundary", Config.n_boundary + "");
            log.put("gravity_bdi", Config.gravity_bdi + "");
            log.put("gravity_bti", Config.gravity_bti + "");
            log.put("base_threshouldspace", Config.base_threshouldspace + "");
            log.put("base_threshouldspeed", Config.base_threshouldspeed + "");
            log.put("sigma", Config.sigma + "");
            log.put("miu", Config.miu + "");
            log.put("t_max", Config.t_max + "");

            log.put("logInfos", logInfos);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            name = new Date().getTime() + ".json";
            num = 0;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void addLog() {
        JSONObject info = new JSONObject();
        try {
            info.put("latitude", center_paint.latitude);
            info.put("longtitude", center_paint.longitude);
            info.put("x", mLocationController.getX());
            info.put("y", mLocationController.getY());
            info.put("distance", mLocationController.getBoundaryDistance());
          //  info.put("speed", mLocationController.getSpeed());
            info.put("thd", mLocationController.getFence().getThreshouldSpace());
            info.put("thv", mLocationController.getFence().getThreshouldSpeed());
            info.put("ri", mLocationController.getRiskIndex());
            info.put("si", User.getInstance().getStatisticIndex());
            info.put("bdi", mLocationController.getBoundaryDistanceIndex());
            info.put("bti", mLocationController.getBoundaryTimeIndex());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        logInfos.put(info);
        num++;
        if (num % 3 == 0) {
            try {
                writeToJson(path, log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            writeToJson(path, log);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getLog() {
        return log;
    }

    public void setLog(JSONObject log) {
        this.log = log;
    }

    public JSONArray getLogInfos() {
        return logInfos;
    }

    public void setLogInfos(JSONArray logInfos) {
        this.logInfos = logInfos;
    }

    /**
     * jason文件写入
     *
     * @param filePath
     * @param object
     * @throws IOException
     */
    private static void writeToJson(String filePath, JSONObject object) throws IOException {
        File file = new File(filePath+"/" + name);
        file.createNewFile();
        char[] stack = new char[1024];
        int top = -1;
        String string = object.toString();
        StringBuffer sb = new StringBuffer();
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if ('{' == c || '[' == c) {
                stack[++top] = c;
                sb.append("\n" + charArray[i] + "\n");
                for (int j = 0; j <= top; j++) {
                    sb.append("\t");
                }
                continue;
            }
            if ((i + 1) <= (charArray.length - 1)) {
                char d = charArray[i + 1];
                if ('}' == d || ']' == d) {
                    top--;
                    sb.append(charArray[i] + "\n");
                    for (int j = 0; j <= top; j++) {
                        sb.append("\t");
                    }
                    continue;
                }
            }
            if (',' == c) {
                sb.append(charArray[i] + "");
                for (int j = 0; j <= top; j++) {
                    sb.append("");
                }
                continue;
            }
            sb.append(c);
        }
        Writer write = new FileWriter(file);
        write.write(sb.toString());
        write.flush();
        write.close();
    }
}
