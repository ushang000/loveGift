package ys.ushang.lovegift.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by shao on 2015/11/12.
 */
public class MyTextView extends TextView {
    int textIndex = 0;

    String text;

    String subText;
    Context myContext;

    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private Paint mPaint;
    private int mViewWidth = 0;
    private int mTranslate = 0;
    private float mScale = 0.1f;
    private boolean mAnimating = true;

    public MyTextView(Context context) {
        super(context);
        text = "";
        myContext = context;
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        text = "";
        myContext = context;
    }

    Handler mHandler = new Handler() {

    };

    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (textIndex <= text.length()) {
                subText = text.substring(0, textIndex);
                textIndex++;
                mHandler.postDelayed(r, 200);
            }
        }
    };

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0) {
                mPaint = getPaint();
                mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                        new int[] { 0xffCD7F32, 0xff000000, 0xffCD7F32 },
                        new float[] { 0.0f, 0.5f, 1.0f }, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    public void mySetText(String text) {
        this.text = text;
        textIndex = 0;
        subText = "";
        mHandler.removeCallbacks(r);
        mHandler.post(r);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setText(subText);
        if (mAnimating && mGradientMatrix != null) {
            mScale += 0.005f;
            if (mScale > 1.5f) {
                mScale = 0.005f;
            }
            mGradientMatrix.setScale(mScale, mScale, mViewWidth / 2, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    super.onDraw(canvas);
    }
}
