package sse309.bupt.fence.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.zip.Inflater;

import sse309.bupt.fence.Config;
import sse309.bupt.fence.R;

public class ConfigActivity extends Activity {
    private EditText base_threshouldspace;
    private EditText n_threshouldspace;
    private EditText base_threshouldspeed;
    private EditText n_threshouldspeed;
    private EditText sigma;
    private EditText miu;
    private EditText n_boundary;
    private EditText n_time;
    private EditText t_max;
    private EditText gravity_bdi;
    private EditText gravity_bti;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_layout);
        initView();
    }

    private void initView() {
        base_threshouldspace = findViewById(R.id.base_threshouldspace);
        n_threshouldspace = findViewById(R.id.n_threshouldspace);
        base_threshouldspeed = findViewById(R.id.base_threshouldspeed);
        n_threshouldspeed = findViewById(R.id.n_threshouldspeed);
        sigma = findViewById(R.id.sigma);
        miu = findViewById(R.id.miu);
        n_boundary = findViewById(R.id.n_boundary);
        n_time = findViewById(R.id.n_time);
        t_max = findViewById(R.id.t_max);
        gravity_bdi = findViewById(R.id.gravity_bdi);
        gravity_bti = findViewById(R.id.gravity_bti);

        base_threshouldspace.setText(Config.base_threshouldspace + "");
        n_threshouldspace.setText(Config.n_threshouldspace + "");
        base_threshouldspeed.setText(Config.base_threshouldspeed + "");
        n_threshouldspeed.setText(Config.n_threshouldspeed + "");
        sigma.setText(Config.sigma + "");
        miu.setText(Config.miu + "");
        n_boundary.setText(Config.n_boundary + "");
        n_time.setText(Config.n_time + "");
        t_max.setText(Config.t_max + "");
        gravity_bdi.setText(Config.gravity_bdi + "");
        gravity_bti.setText(Config.gravity_bti + "");

        Button bt_sure = findViewById(R.id.bt_sure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        modifyData();
                        startActivityForResult(new Intent(ConfigActivity.this, MainActivity.class), 1);
                        finish();
                    }
                });
            }
        });

    }

    private void modifyData() {
        Config.n_threshouldspace = Integer.valueOf(n_threshouldspace.getText().toString());
        Config.n_threshouldspeed = Integer.valueOf(n_threshouldspeed.getText().toString());
        Config.n_boundary = Integer.valueOf(n_boundary.getText().toString());
        Config.n_time = Integer.valueOf(n_time.getText().toString());
        Config.gravity_bdi = Integer.valueOf(gravity_bdi.getText().toString());
        Config.gravity_bti = Integer.valueOf(gravity_bti.getText().toString());
        Config.base_threshouldspace = Double.valueOf(base_threshouldspace.getText().toString());
        Config.base_threshouldspeed = Double.valueOf(base_threshouldspeed.getText().toString());
        Config.sigma = Double.valueOf(sigma.getText().toString());
        Config.miu = Double.valueOf(miu.getText().toString());
        Config.t_max = Double.valueOf(t_max.getText().toString());

        SharedPreferences.Editor editor = MainActivity.sp.edit();
        editor.putInt("n_threshouldspace", Integer.valueOf(n_threshouldspace.getText().toString()));
        editor.putInt("n_threshouldspeed", Integer.valueOf(n_threshouldspeed.getText().toString()));
        editor.putInt("n_boundary", Integer.valueOf(n_boundary.getText().toString()));
        editor.putInt("n_time", Integer.valueOf(n_time.getText().toString()));
        editor.putInt("gravity_bdi", Integer.valueOf(gravity_bdi.getText().toString()));
        editor.putInt("gravity_bti", Integer.valueOf(gravity_bti.getText().toString()));
        editor.putString("base_threshouldspace", base_threshouldspace.getText().toString());
        editor.putString("base_threshouldspeed", base_threshouldspeed.getText().toString());
        editor.putString("sigma", sigma.getText().toString());
        editor.putString("miu", miu.getText().toString());
        editor.putString("t_max", t_max.getText().toString());
        editor.commit();
    }
}
