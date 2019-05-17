package sse309.bupt.fence.communication;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import sse309.bupt.fence.activity.LoginActivity;
import sse309.bupt.fence.activity.SignupActivity;
import sse309.bupt.fence.bean.LocationEntity;
import sse309.bupt.fence.bean.LoginEntity;

public class RequestHelper {
    String url="http://10.128.230.249:8080";
    static String output="";
    static String result="";
    private LoginActivity.onLoginListenner onLoginListenner;
    private SignupActivity.onSignupListener onSignupListener;

    public static void addInAlarm() {
    }

    public static void addOutAlarm() {
    }

    //登录方法，通过与后端的交互来进行登录成功的跳转或登录失败的提示
    public void login(final String username,final String password){
        //通过handler实现子进程与主进程的通信，在主进程中进行判断并触发回调方法
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("yes")){
                    LoginEntity.setUser(username);
                    onLoginListenner.onLoginSucceed();
                }else{
                    onLoginListenner.onLoginFailed();
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //封装并发送请求
                    LoginEntity loginEntity=new LoginEntity(password,username);
                    Gson gson=new Gson();
                    String json=gson.toJson(loginEntity);
                    OkHttpClient mOkHttpClient=new OkHttpClient();
                    RequestBody formBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
                    Request request = new Request.Builder().url(url+"/fence/interface/user/login").post(formBody).build();
                    //阻塞式接收response并处理response body
                    Response response=mOkHttpClient.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject1=new JSONObject(responseData);
                    JSONObject jsonObject2=jsonObject1.getJSONObject("content");
                    output=jsonObject2.getString("output");
                    //将返回值发送给主进程
                    Message message=handler.obtainMessage();
                    message.obj=output;
                    handler.sendMessage(message);
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        }).start();
    }
    //注册方法，通过与后端的交互来进行注册成功的跳转或注册失败的提示
    public void signUp(final String username, final String password){
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("1000")){
                    onSignupListener.onSignupSucceed();
                }else{
                    onSignupListener.onSignupFailed();
                }
                super.handleMessage(msg);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LoginEntity loginEntity=new LoginEntity(password,username);
                    Gson gson=new Gson();
                    String json=gson.toJson(loginEntity);
                    OkHttpClient mOkHttpClient=new OkHttpClient();
                    RequestBody formBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
                    Request request = new Request.Builder().url(url+"/fence/interface/user/add_user").post(formBody).build();
                    Response response=mOkHttpClient.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                    result=jsonObject.getString("result");
                    Message message=handler.obtainMessage();
                    message.obj=result;
                    handler.sendMessage(message);
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        }).start();
    }
    public void sendLocation(final double Lat, final double Lng, final String username){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LocationEntity locationEntity=new LocationEntity(username,Lng,Lat);
                    Gson gson=new Gson();
                    String json=gson.toJson(locationEntity);
                    OkHttpClient mOkHttpClient=new OkHttpClient();
                    RequestBody formBody=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
                    Request request = new Request.Builder().url(url+"/fence/interface/user/updateLocation").post(formBody).build();
                    Response response=mOkHttpClient.newCall(request).execute();
                    String responseData=response.body().string();
                    JSONObject jsonObject=new JSONObject(responseData);
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        }).start();
    }
    public void sendAlarm(String fenceId, String userId, Date date){

    }
    public void setOnLoginListenner(LoginActivity.onLoginListenner onLoginListenner) {
        this.onLoginListenner = onLoginListenner;
    }
    public void setOnSignupListener(SignupActivity.onSignupListener onSignupListener){
        this.onSignupListener=onSignupListener;
    }
}
