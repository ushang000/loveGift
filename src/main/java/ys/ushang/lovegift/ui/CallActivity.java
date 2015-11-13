package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import ys.ushang.lovegift.R;


/**
 * Created by shao on 2015/11/2.
 */
public class CallActivity extends Activity implements View.OnClickListener{

    private ImageView call_picture;
    private ImageButton call_answer,call_off;
    private TextView call_number,call_location;
    private MediaPlayer mediaPlayer,mediaPlayerAnswer;
    private String filePath;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        call_number=(TextView)findViewById(R.id.call_number);
        call_location=(TextView)findViewById(R.id.call_location);
        filePath=getExternalFilesDir("").getPath()+"/recorder/love.amr";
        Typeface face = Typeface.createFromAsset (getAssets() , "fonts/STCAIYUN.TTF" );
        Typeface face2 = Typeface.createFromAsset (getAssets() , "fonts/comicz.ttf" );
        call_number.setTypeface(face);
        call_location.setTypeface(face2);
        call_picture=(ImageView)findViewById(R.id.call_picture);
        call_picture.setImageBitmap(BitmapFactory.decodeFile(MainActivity.imgPath + "puzzle.jpg"));

        call_answer=(ImageButton)findViewById(R.id.call_answer);
        call_answer.setOnClickListener(this);
        call_off=(ImageButton)findViewById(R.id.call_off);
        call_off.setOnClickListener(this);

        dialog=new AlertDialog.Builder(CallActivity.this)
                .setTitle("说明")
                .setMessage("请按home键回到桌面，拨打电话5201314,里面有我想对你说的话")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                }).create();

        mediaPlayerAnswer=new MediaPlayer();

        mediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        try {
            mediaPlayerAnswer.setDataSource(filePath);
            mediaPlayerAnswer.prepare();
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayerAnswer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.show();
            }
        });
        mediaPlayer.start();
    }

    @Override
    public void onClick(View v) {

        if(v==call_answer){
            mediaPlayer.stop();
            mediaPlayerAnswer.start();
            call_answer.setVisibility(View.GONE);
            call_off.setVisibility(View.VISIBLE);
        }
        if(v==call_off){
            mediaPlayerAnswer.stop();
            //finish();
            dialog.show();


        }
    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
