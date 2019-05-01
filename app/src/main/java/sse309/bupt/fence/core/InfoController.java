package sse309.bupt.fence.core;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sse309.bupt.fence.Config;
import sse309.bupt.fence.bean.BreakInfo;
import sse309.bupt.fence.bean.User;
import sse309.bupt.fence.util.Util;

public class InfoController {


    public static List<BreakInfo> parseBreakFenceInfo(String json) {
        List breakFenceInfo = new ArrayList();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int userId = jsonObject.getInt("userId");
                int fenceId = jsonObject.getInt("fenceId");
                long breakTime = jsonObject.getLong("breakTime");
                BreakInfo brakInfo = new BreakInfo(userId, fenceId, breakTime);
                breakFenceInfo.add(brakInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return breakFenceInfo;
    }

    /**
     * 得到json文件中的内容
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName),"utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
