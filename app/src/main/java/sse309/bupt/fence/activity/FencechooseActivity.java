package sse309.bupt.fence.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sse309.bupt.fence.R;
import sse309.bupt.fence.bean.Fence;
import sse309.bupt.fence.bean.Point;
import sse309.bupt.fence.communication.RequestHelper;

public class FencechooseActivity extends Activity {
    RequestHelper requestHelper=new RequestHelper();
    ArrayList<String> list=requestHelper.getFences();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fencechoose_layout);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(FencechooseActivity.this,android.R.layout.simple_list_item_1,list);
        ListView listView=(ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String fenceName=list.get(position);
                double r = requestHelper.getR(fenceName);
                Point center=requestHelper.getCenter(fenceName);
                Intent intent=new Intent(FencechooseActivity.this,MainActivity.class);
                Fence fence=new Fence();
                fence.setFenceName(fenceName);
                fence.setR(r);
                fence.setCenter(center);
                fence.setOnline(true);
                startActivity(intent);
                finish();
            }
        });
    }
}
