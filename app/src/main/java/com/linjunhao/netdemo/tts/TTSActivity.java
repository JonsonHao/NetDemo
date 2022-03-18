package com.linjunhao.netdemo.tts;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.linjunhao.netdemo.R;

import java.util.Locale;

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/3/18
 * @desc:
 */
public class TTSActivity extends AppCompatActivity implements View.OnClickListener {


    private TextToSpeech mSpeech = null;//创建自带语音对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        findViewById(R.id.btn_play).setOnClickListener(this);
        initTTS();
    }

    private void initTTS() {
        //实例化自带语音对象
        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (mSpeech != null) {
                    int isSupportChinese = mSpeech.isLanguageAvailable(Locale.CHINESE);//是否支持中文
                    mSpeech.getMaxSpeechInputLength();//最大播报文本长度

                    if (isSupportChinese == TextToSpeech.LANG_AVAILABLE) {
                        int setLanRet = mSpeech.setLanguage(Locale.CHINESE);//设置语言
                        int setSpeechRateRet = mSpeech.setSpeechRate(1.0f);//设置语速
                        int setPitchRet = mSpeech.setPitch(1.0f);//设置音量
                        String defaultEngine = mSpeech.getDefaultEngine();//默认引擎
                        if (status == TextToSpeech.SUCCESS) {
                            //初始化TextToSpeech引擎成功，初始化成功后才可以play等
                        }
                    }
                } else {
                    Log.i("ljh", "初始化引擎失败");
                }
            }
        });
        mSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d("ljh", "onStart");
            }

            @Override
            public void onDone(String utteranceId) {
                Log.d("ljh", "onDone");
            }

            @Override
            public void onError(String utteranceId) {
                Log.d("ljh", "onError");
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play) {
            startAuto("今日天气晴，");
        }
    }

    private void startAuto(String data) {
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mSpeech.setPitch(1.0f);
        // 设置语速
        mSpeech.setSpeechRate(0.5f);
        mSpeech.speak(data,//输入中文，若不支持的设备则不会读出来
                TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpeech.stop(); // 不管是否正在朗读TTS都被打断
        mSpeech.shutdown(); // 关闭，释放资源
    }


}
