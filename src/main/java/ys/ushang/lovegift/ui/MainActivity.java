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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import ys.ushang.lovegift.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent= new Intent(Intent.ACTION_DIAL);
        //puzzle=(ImageView)findViewById(R.id.puzzle);
        /*mediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        try {
            //mediaPlayer.setDataSource("/mnt/sdcard/Music/hjbj.mp3");
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        // intent.setClassName("com.android.contacts","com.android.contacts.DialtactsActivity");
        registerReceiver(phoneReceiver, new IntentFilter("android.intent.action.NEW_OUTGOING_CALL"));
        // startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isChanged) {
            gamePintuLayout.nextLevel();
        }

        // puzzle.setImageBitmap(BitmapFactory.decodeFile(imgPath+"puzzle.jpg"));
        //Log.i(TAG,"the image getTag = "+puzzle.getTag());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    BroadcastReceiver phoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                //intent.getExtras();

                Log.i("MainActivity", "the data is = " + getResultData());
                if (getResultData().equals("5201314")) {

                    //mediaPlayer.start();
                    startActivity(new Intent(MainActivity.this, TheWordActivity.class));
                }
                setResultData(null);
            }

        }
    };

    private void CopyAssets(String dir, String fileName) {
        //String[] files;
        File mWorkingPath = new File(dir);
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
                Log.e("--CopyAssets--", "cannot create directory.");
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
    public void onClick(View v) {

        if (v == originalButton) {
            startActivity(new Intent(MainActivity.this, OriginalActivity.class));
        }

    }

}
