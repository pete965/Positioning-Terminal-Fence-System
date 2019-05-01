package sse309.bupt.fence.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import sse309.bupt.fence.R;
import sse309.bupt.fence.communication.RequestHelper;

public class SignupActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final int RC_CAMERA_AND_WIFI = 123;
    private Button bt_signup;
    private Button bt_back;
    private EditText username;
    private EditText password;
    public interface onSignupListener{
        void onSignupSucceed();
        void onSignupFailed();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);
        username=findViewById(R.id.username1);
        password=findViewById(R.id.password1);
        initToolrbar();
        jump();
    }

    private void jump() {
        bt_signup = findViewById(R.id.bt_signup);
        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RequestHelper requestHelper=new RequestHelper();
                        requestHelper.setOnSignupListener(new onSignupListener() {
                            @Override
                            public void onSignupSucceed() {
                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onSignupFailed() {
                                Toast.makeText(getApplicationContext(), "注册失败请重试", Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestHelper.signUp(username.getText().toString(),password.getText().toString());
                    }
                });

            }
        });
        bt_back = findViewById(R.id.bt_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });
    }

    private void initToolrbar() {
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        toolbar.setTitle("注册");
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
