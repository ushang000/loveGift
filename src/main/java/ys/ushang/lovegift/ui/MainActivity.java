package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import ys.ushang.lovegift.R;
import ys.ushang.lovegift.utils.PreferencesUtil;
import ys.ushang.lovegift.utils.UtilsConstans;
import ys.ushang.lovegift.view.GamePintuLayout;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView puzzle;
    public static String imgPath;
    private String TAG = "MainActivity";
    private GamePintuLayout gamePintuLayout;
    public static boolean isChanged = false;
    private Button originalButton;
    private MediaPlayer mediaPlayer;
    private String fileName;
    private String [] letter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashReport.initCrashReport(this, "900012942", false);
        initData();

        registerReceiver(phoneReceiver, new IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isChanged) {
            gamePintuLayout.nextLevel();
        }
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(phoneReceiver);
    }

    public void initData(){
        fileName=getExternalFilesDir("").getPath()+"/recorder/";
        File file2=new File(fileName);
        if(!file2.exists()){
            file2.mkdirs();
        }
        if (!new File(fileName + "love.amr").exists()) {
            CopyAssets(fileName,"love.amr");
        }
        if (!new File(fileName + "kisstherain.mp3").exists()) {
            CopyAssets(fileName,"kisstherain.mp3");
        }

        PreferencesUtil preferencesUtil=PreferencesUtil.getInstance(this);
        letter=new String[]{getString(R.string.childhood),getString(R.string.meet),getString(R.string.ageing)};
        for (int i=0;i<letter.length;i++){
            String name=preferencesUtil.getParameterSharePreference("letter"+i,UtilsConstans.loveGift);
            if (name==null){
                preferencesUtil.saveOnlyParameters(UtilsConstans.loveGift,"letter"+i,letter[i]);
            }
        }

        imgPath = getExternalFilesDir("").getPath() + "/image/";
        originalButton = (Button) findViewById(R.id.original);
        originalButton.setOnClickListener(this);
        originalButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return false;
            }

        });
        File file = new File(imgPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!new File(imgPath + "puzzle.jpg").exists()) {
            CopyAssets(imgPath, "puzzle.jpg");
        }

        gamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gameview);
    }

    BroadcastReceiver phoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                String phoneNum = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.i(TAG," phoneNum : "+phoneNum);
                if (phoneNum.equals("5201314")) {
                    this.setResultData(null);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setAction("android.intent.action.theword");
                    startActivity(intent);
                }

            }

        }
    };

    private void CopyAssets(String dir, String fileName) {
        //String[] files;
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
            }
        }
        try {
            InputStream in = this.getResources().getAssets().open(fileName);
            System.err.println("");
            File outFile = new File(mWorkingPath, fileName);
            OutputStream out = new FileOutputStream(outFile);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);

    }

    @Override
    public void onClick(View v) {

        if (v == originalButton) {
            startActivity(new Intent(MainActivity.this, OriginalActivity.class));
        }

    }

}
