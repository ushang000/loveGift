package ys.ushang.lovegift.view;

/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Field;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import ys.ushang.lovegift.R;

public class VideoControll extends android.widget.MediaController {

    private Activity mActivity;

    private View mView;

    public VideoControll(Activity activity) {
        super(activity);
        mActivity = activity;
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);
        mView = LayoutInflater.from(getContext()).inflate(
                R.layout.video_controll, null);
        try {
            SeekBar sb = (SeekBar) mView.findViewById(R.id.progress);
            Field mRoot = android.widget.MediaController.class
                    .getDeclaredField("mRoot");
            mRoot.setAccessible(true);
            //mRoot.set(this,mView);
            ViewGroup mRootVg = (ViewGroup) mRoot.get(this);
            ViewGroup vg = findSeekBarParent(mRootVg);
           // ViewGroup ib=find
            mRootVg.removeViewAt(0);
            ImageButton close=(ImageButton)mView.findViewById(R.id.close);
            close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
            ImageButton pauseButton=(ImageButton)mView.findViewById(R.id.pause);
            TextView time=(TextView)mView.findViewById(R.id.time);
            TextView current_time=(TextView)mView.findViewById(R.id.time_current);
            //vg.removeViewAt(index);
            vg.removeAllViews();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            //vg.addView;
            vg.addView(mView);
            Field mPauseButton=android.widget.MediaController.class
                    .getDeclaredField("mPauseButton");
            Field mEndTime=android.widget.MediaController.class
                    .getDeclaredField("mEndTime");
            Field mCurrentTime=android.widget.MediaController.class
                    .getDeclaredField("mCurrentTime");
            Field mProgress = android.widget.MediaController.class
                    .getDeclaredField("mProgress");
            mProgress.setAccessible(true);
            mProgress.set(this, sb);
            /*Field mFfwdButton=android.widget.MediaController.class
                    .getDeclaredField("mFfwdButton");
            Field mRewButton=android.widget.MediaController.class
                    .getDeclaredField("mRewButton");
            mFfwdButton.setAccessible(false);

            mRewButton.setAccessible(false);*/
            mPauseButton.setAccessible(true);
            mPauseButton.set(this,pauseButton);
            mEndTime.setAccessible(true);
            mEndTime.set(this,time);
            mCurrentTime.setAccessible(true);
            mCurrentTime.set(this,current_time);
            Field mPauseListener= android.widget.MediaController.class
                    .getDeclaredField("mPauseListener");
            mPauseListener.setAccessible(true);
            pauseButton.setOnClickListener((OnClickListener)mPauseListener.get(this));
            Field mSeekListener = android.widget.MediaController.class
                    .getDeclaredField("mSeekListener");
            mSeekListener.setAccessible(true);
            sb.setOnSeekBarChangeListener((OnSeekBarChangeListener) mSeekListener
                    .get(this));
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ViewGroup findSeekBarParent(ViewGroup vg) {
        ViewGroup viewGroup = null;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View view = vg.getChildAt(i);
            if (view instanceof SeekBar) {
                viewGroup = (ViewGroup) view.getParent();
                break;
            } else if (view instanceof ViewGroup) {
                viewGroup = findSeekBarParent((ViewGroup) view);
            } else {
                continue;
            }
        }
        return viewGroup;
    }

    @Override
    public void show(int timeout) {
        super.show(timeout);
        /*((ViewGroup) mActivity.findViewById(android.R.id.content))
                .removeView(mView);
        ((ViewGroup) mActivity.findViewById(android.R.id.content))
                .addView(mView);*/
    }

    @Override
    public void hide() {
        super.hide();
        /*((ViewGroup) mActivity.findViewById(android.R.id.content))
                .removeView(mView);*/
    }

}
