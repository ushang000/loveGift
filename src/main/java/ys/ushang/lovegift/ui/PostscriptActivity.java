package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import ys.ushang.lovegift.R;


/**
 * Created by shao on 2015/11/13.
 */
public class PostscriptActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postscript);
        CrashReport.initCrashReport(this, "900012942", false);
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
}
