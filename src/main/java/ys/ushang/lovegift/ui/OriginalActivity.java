package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import ys.ushang.lovegift.R;


/**
 * Created by shao on 2015/10/16.
 */
public class OriginalActivity extends Activity implements View.OnClickListener{

    private ImageView originalView;
    private Button know,look;
    private RelativeLayout remind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original);
        CrashReport.initCrashReport(this, "900012942", false);
        originalView=(ImageView)findViewById(R.id.original);
        originalView.setImageBitmap(BitmapFactory.decodeFile(MainActivity.imgPath+"puzzle.jpg"));
        know=(Button)findViewById(R.id.know);
        know.setOnClickListener(this);
        look=(Button)findViewById(R.id.look);
        look.setOnClickListener(this);
        remind=(RelativeLayout)findViewById(R.id.remind);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    public void onClick(View v) {
        if(v==know){
            finish();
        }
        if(v==look){
            remind.setVisibility(View.GONE);
        }
    }
}
