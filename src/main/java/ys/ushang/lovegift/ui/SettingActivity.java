package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ys.ushang.lovegift.R;
import ys.ushang.lovegift.utils.PreferencesUtil;
import ys.ushang.lovegift.utils.UtilsConstans;


/**
 * Created by shao on 2015/10/14.
 */
public class SettingActivity extends Activity implements View.OnClickListener{

    private ImageButton capture,pick,recorder_start,recorder_stop;
    private MediaRecorder mediaRecorder;
    private String fileName;
    private Button postscript,saveButton;
    private EditText letter0,letter1,letter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        CrashReport.initCrashReport(this, "900012942", false);
        fileName=getExternalFilesDir("").getPath()+"/recorder/";

        capture=(ImageButton)findViewById(R.id.capture);
        capture.setOnClickListener(this);
        pick=(ImageButton)findViewById(R.id.pick);
        pick.setOnClickListener(this);
        recorder_start=(ImageButton)findViewById(R.id.recorder_start);
        recorder_start.setOnClickListener(this);
        recorder_stop=(ImageButton)findViewById(R.id.recorder_stop);
        recorder_stop.setOnClickListener(this);
        postscript=(Button)findViewById(R.id.postscript);
        postscript.setOnClickListener(this);
        saveButton=(Button)findViewById(R.id.save);
        saveButton.setOnClickListener(this);
        letter0=(EditText)findViewById(R.id.letter_0);
        letter1=(EditText)findViewById(R.id.letter_1);
        letter2=(EditText)findViewById(R.id.letter_2);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.capture:{
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                        .fromFile(new File(getExternalFilesDir("").getPath()+"/image/", "puzzle.jpg")));
                startActivityForResult(intent, 1);
                break;
            }
            case R.id.pick:{
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 2);
                break;
            }
            case R.id.recorder_start:{
                mediaRecorder=new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                mediaRecorder.setOutputFile(fileName+"love.amr");
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                try {
                    mediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
                recorder_start.setVisibility(View.GONE);
                recorder_stop.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.recorder_stop:{
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder=null;
                recorder_start.setVisibility(View.VISIBLE);
                recorder_stop.setVisibility(View.GONE);
                break;
            }
            case R.id.postscript:{
                startActivity(new Intent(this,PostscriptActivity.class));
                break;
            }
            case R.id.save:{
                PreferencesUtil pre=PreferencesUtil.getInstance(this);
                String temp0=letter0.getText().toString();
                String temp1=letter1.getText().toString();
                String temp2=letter2.getText().toString();
                if(!temp0.equals("")){
                    pre.saveOnlyParameters(UtilsConstans.loveGift,"letter0",temp0);
                }
                if(!temp1.equals("")){
                    pre.saveOnlyParameters(UtilsConstans.loveGift,"letter1",temp1);
                }
                if(!temp2.equals("")){
                    pre.saveOnlyParameters(UtilsConstans.loveGift,"letter2",temp2);
                }
                finish();
                break;
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){

            switch (requestCode){
                case 1:{
                    MainActivity.isChanged=true;
                    break;
                }
                case 2:{
                    MainActivity.isChanged=true;
                    if(data !=null){
                        Cursor cursor = getContentResolver().query(data.getData(),null, null, null, null);
                        cursor.moveToFirst();
                        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        String fromPath=cursor.getString(idx);
                        String path = getExternalFilesDir("").getPath() + "/image/puzzle.jpg";
                        copySdcardFile(fromPath,path);
                        cursor.close();
                        //bitmap.recycle();
                    }else {
                        Log.i("Setting","data = null");
                    }
                    break;
                }

            }

        }
    }

    public int copySdcardFile(String fromFile, String toFile)
    {
        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex)
        {
            return -1;
        }
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
