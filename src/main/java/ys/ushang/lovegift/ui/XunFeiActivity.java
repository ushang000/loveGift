package ys.ushang.lovegift.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.UnderstanderResult;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ys.ushang.lovegift.R;
import ys.ushang.lovegift.utils.HanziToPinyin;
import ys.ushang.lovegift.utils.JsonParser;

/**
 * Created by shao on 2016/1/5.
 */
public class XunFeiActivity extends Activity implements View.OnClickListener {
    @InjectView(R.id.understand)
    Button understand;
    private SpeechSynthesizer mTts;
    private EditText xun_text;
    private Button iat, tts, pin;
    private Toast mToast;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    //语义理解
    private SpeechUnderstander mSpeechUnderstander;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunfei);
        ButterKnife.inject(this);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=568a2e2d");
        xun_text = (EditText) findViewById(R.id.xun_text);
        iat = (Button) findViewById(R.id.iat);
        iat.setOnClickListener(this);
        tts = (Button) findViewById(R.id.tts);
        tts.setOnClickListener(this);
        pin = (Button) findViewById(R.id.pin);
        pin.setOnClickListener(this);
        understand.setOnClickListener(this);
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(this, mInitListener);
        xun_text.setText("们");
        mToast=Toast.makeText(this,"",Toast.LENGTH_SHORT);

    }

    public void iatRecognize() {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无�?创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资�?

        setParam();
        initRecognizerDialog();
    }

    public void speechInit(String text, String voicer) {
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
//2.合成参数设置，详见�?�科大讯飞MSC API手册(Android)》SpeechSynthesizer �?
        mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);//设置发音�?
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语�??
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范�?0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//设置合成音频保存位置（可自定义保存位置），保存在�?./sdcard/iflytek.pcm�?
//保存在SD卡需要在AndroidManifest.xml添加写SD卡权�?
//如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, getExternalFilesDir("").getPath()+"/iflytek.pcm");
//3.�?始合�?
        String path = Environment.getExternalStorageDirectory() + "/tts.pcm";
        //int code = mTts.synthesizeToUri(text, path, mSynListener);
        int code = mTts.startSpeaking(text, mSynListener);
        Log.i("speechInit", " code = " + code);
        if (code != ErrorCode.SUCCESS) {
            Log.i("speech", "语音合成失败,错误�?: " + code);
        }
    }

    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进�?0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息�??
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            Log.i("shao", " percent = " + percent);
        }

        //�?始播�?
        public void onSpeakBegin() {

        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进�?0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

    @Override
    public void onClick(View v) {
        if (v == iat) {
            mIatResults.clear();
            iatRecognize();
        }
        if (v == tts) {
            String text = xun_text.getText().toString();
            Log.i("tts", " text = " + text);
            if (!text.equals("")) {
                speechInit(text, "xiaoyu");
            } else {
                Toast.makeText(this, "请输入文字?", Toast.LENGTH_SHORT).show();
            }
        }
        if(v==understand){
            int ret =0;
            xun_text.setText("");
            //initRecognizerDialog();
            // 设置参数
            setUnderParam();
            if(mSpeechUnderstander.isUnderstanding()){// 开始前检查状态
                mSpeechUnderstander.stopUnderstanding();
            }else {
                ret = mSpeechUnderstander.startUnderstanding(speechUnderstandListener);
                if(ret != 0){
                    showTip("语义理解失败,错误码:"	+ ret);
                }else {
                    showTip("开始");
                }
            }
        }
        if (v == pin) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(xun_text.getText());
            String pin = getPinYin(stringBuffer.toString());
            //String pin=HanziToPinyin.getInstance().transliterate(stringBuffer.toString());
            //String pin=getPin(stringBuffer.toString());
            stringBuffer.append("\n转拼音：").append(pin);
            xun_text.setText(stringBuffer.toString());
            xun_text.setSelection(xun_text.length());
        }
    }

    private void initRecognizerDialog(){
        mIatDialog = new RecognizerDialog(this, null);
        mIatDialog.setListener(mRecognizerDialogListener);
       // mIatDialog.setParameter(SpeechConstant.DOMAIN,"iat");
        //mIatDialog.setParameter(SpeechConstant.RESULT_TYPE,"json");
       // mIatDialog.setParameter(SpeechConstant.NLP_VERSION,"2.0");
        //mIatDialog.setParameter(SpeechConstant.PARAMS,"sch=1");
        //SpeechUnderstand speechUnderstand=new Spee
        //SpeechUnderstander speechUnderstander=SpeechUnderstander.createUnderstander(this,mInitListener);
        //speechUnderstander.setParameter(SpeechConstant.DOMAIN, "iat");
        mIatDialog.show();
    }

    /**
     * 初始化监听器�?
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.i("shao", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.i("shao", "初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        /*String lag = mSharedPreferences.getString("iat_language_preference",
                "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }*/

        // 设置语音前端�?:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端�?:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置�?"0"返回结果无标�?,设置�?"1"返回结果有标�?
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记�?要更新版本才能生�?
        /*mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
*/
        // 设置听写结果是否结果动�?�修正，为�??1”则在听写过程中动�?��?�增地返回结果，否则只在听写结束之后返回�?终结�?
        // 注：该参数暂时只对在线听写有�?
        mIat.setParameter(SpeechConstant.ASR_DWA, "0");
    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        Log.i("shao", " text = " + text);
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        xun_text.setText(resultBuffer.toString());
        xun_text.setSelection(xun_text.length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSpeechUnderstander.isUnderstanding()){
            mSpeechUnderstander.cancel();
            mSpeechUnderstander.destroy();
        }

    }

    /**
     * 听写UI监听�?
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.i("shao"," results : "+results.getResultString());
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Log.i("shao", " error = " + error.getPlainDescription(true));
        }

    };

    /**
     * 汉字转换拼音，字母原样返回，都转换为小写
     *
     * @param input
     * @return
     */
    public static String getPinYin(String input) {
        ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(input);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (HanziToPinyin.Token token : tokens) {
                if (token.type == HanziToPinyin.Token.PINYIN) {
                    sb.append(token.target).append(" ");
                } else {
                    sb.append(token.source).append(" ");
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    /*public String getPin(String input){
        ArrayList<Token> tokens = HanziToPinyin.getInstance().getTokens(input);
        final int tokenCount = tokens.size();
        final StringBuilder keyPinyin = new StringBuilder();
        final StringBuilder keyInitial = new StringBuilder();
        // There is no space among the Chinese Characters, the variant name
        // lookup key wouldn't work for Chinese. The keyOriginal is used to
        // build the lookup keys for itself.
        final StringBuilder keyOriginal = new StringBuilder();
        for (int i = tokenCount - 1; i >= 0; i--) {
            final Token token = tokens.get(i);
            if (Token.UNKNOWN == token.type) {
                continue;
            }
            if (Token.PINYIN == token.type) {
                keyPinyin.insert(0, token.target);
                keyInitial.insert(0, token.target.charAt(0));
            } else if (Token.LATIN == token.type) {
                // Avoid adding space at the end of String.
                if (keyPinyin.length() > 0) {
                    keyPinyin.insert(0, ' ');
                }
                if (keyOriginal.length() > 0) {
                    keyOriginal.insert(0, ' ');
                }
                keyPinyin.insert(0, token.source);
                keyInitial.insert(0, token.source.charAt(0));
            }
            keyOriginal.insert(0, token.source);
        }
        return keyPinyin.toString();
    }*/

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setUnderParam() {
        /*String lag = mSharedPreferences.getString("understander_language_preference", "mandarin");
        if (lag.equals("en_us")) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lag);
        }*/
        // 设置语言
        mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

        mSpeechUnderstander.setParameter(SpeechConstant.DOMAIN, "iat");
        //mSpeechUnderstander.setParameter(SpeechConstant.NLP_VERSION, "2.0");
        //mSpeechUnderstander.setParameter(SpeechConstant.RESULT_TYPE, "json");
        /*// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("understander_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("understander_vadeos_preference", "1000"));

        // 设置标点符号，默认：1（有标点）
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("understander_punc_preference", "1"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/sud.wav");*/
    }

    /**
     * 语义识别回调。
     */
    private SpeechUnderstanderListener speechUnderstandListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (null != result) {
                        // 显示
                        String text = result.getResultString();
                        if (!TextUtils.isEmpty(text)) {
                            //String resultString=JsonParser.parseUnderJson(text);
                            xun_text.setText(text);
                            //speechInit(resultString, "xiaoyu");
                            Log.i("shao"," 语义："+text);
                        }
                    } else {
                        showTip("识别结果不正确。");
                    }
                }
            });
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            Log.i("shao", error.getPlainDescription(true));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    private void showTip(final String str)
    {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }
}
