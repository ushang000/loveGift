package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ys.ushang.lovegift.R;
import ys.ushang.lovegift.view.MyTextView;
import ys.ushang.lovegift.view.RotateDownPageTransformer;


/**
 * Created by shao on 2015/10/19.
 */
public class TheWordActivity extends Activity implements View.OnClickListener{

    private ViewPager viewPager;
    private String [] letter;
    private int[] image;
    private MyTextView[] textViews=new MyTextView[3];
    //private View view;
    private List<View> letter_layout=new ArrayList<View>();
    private MediaPlayer bgMusic;
    private String musicPath;
    private ImageButton music_start,music_close;
    private boolean isPlaying=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        letter=new String[]{getString(R.string.childhood),getString(R.string.meet),getString(R.string.ageing)};
        image=new int[]{R.mipmap.children,R.mipmap.meet,R.mipmap.ageing};
        bgMusic=new MediaPlayer();
        musicPath=getExternalFilesDir("").getPath()+"/recorder/kisstherain.mp3";
        getViewPager();
        try {
            bgMusic.setDataSource(musicPath);
            bgMusic.prepare();
            bgMusic.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bgMusic.start();
        music_start=(ImageButton)findViewById(R.id.music_start);
        music_start.setOnClickListener(this);
        music_close=(ImageButton)findViewById(R.id.music_close);
        music_close.setOnClickListener(this);
        viewPager.setAdapter(new MyAdapter());
        viewPager.setPageTransformer(true,new RotateDownPageTransformer());
        viewPager.setOnPageChangeListener(new PageChangeListener());
    }

    private void getViewPager(){
        for(int i=0;i<letter.length;i++){
            LayoutInflater inflater=LayoutInflater.from(this);
            View view=inflater.inflate(R.layout.wrod_info,null);
            ImageView imageView=(ImageView)view.findViewById(R.id.image);
            imageView.setImageResource(image[i]);
            textViews[i]=(MyTextView)view.findViewById(R.id.letter);

            Typeface face = Typeface.createFromAsset (getAssets() , "fonts/comic.ttf" );
            textViews[i].setTypeface(face);
            letter_layout.add(view);
        }
        textViews[0].mySetText(letter[0]);

    }

    @Override
    protected void onDestroy() {
        bgMusic.stop();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        bgMusic.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(isPlaying){
            bgMusic.start();
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if(v==music_start){
            bgMusic.pause();
            music_close.setVisibility(View.VISIBLE);
            music_start.setVisibility(View.GONE);
            isPlaying=false;
        }
        if(v==music_close){
            bgMusic.start();
            isPlaying=true;
            music_start.setVisibility(View.VISIBLE);
            music_close.setVisibility(View.GONE);
        }
    }

    class  MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return letter_layout.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(letter_layout.get(position),0);
            return letter_layout.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(letter_layout.get(position));
        }
    }

    class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i("TheWord","the position = "+position);
            textViews[position].mySetText(letter[position]);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
