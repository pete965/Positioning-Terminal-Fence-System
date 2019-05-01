package sse309.bupt.fence;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class RadiusView extends Dialog {
    private Context mContext;
    private onClickSureListener mOnClickSureListener;

    public void setmOnClickSureListener(onClickSureListener mOnClickSureListener) {
        this.mOnClickSureListener = mOnClickSureListener;
    }

    public interface onClickSureListener{
        void onClick(double radius);
    }
    public RadiusView(@NonNull Context context,onClickSureListener mOnClickSureListener) {
        super(context);
        mContext = context;
        this.mOnClickSureListener = mOnClickSureListener;

    }

    public RadiusView(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    protected RadiusView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.radius_type_layout, null);
        setContentView(view);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        final EditText et_radius = findViewById(R.id.et_radius);
        Button bt_sure = findViewById(R.id.bt_sure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickSureListener.onClick(Double.valueOf(et_radius.getText().toString()));
            }
        });
    }
}
