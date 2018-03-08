package com.steven.Smartglass.XunFei;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class Xunfei_TTS {

    private Context context;
    private SpeechSynthesizer mTts;
    private String msg;
    private Handler handler;
    private WakeuperListener mWakeuperListener;

    public Xunfei_TTS(final Context context, SpeechSynthesizer mTts, String msg, Handler handler, WakeuperListener mWakeuperListener) {
        this.context = context;
        this.mTts = mTts;
        this.msg = msg;
        this.handler = handler;
        this.mWakeuperListener = mWakeuperListener;

        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "85");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "90");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端

        /*  离线合成2017.6.15
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL); //设置云端
        mTts.setParameter(ResourceUtil.TTS_RES_PATH,getResourcePath());*/

        if (msg.equals("我在,请说")) {
            mTts.startSpeaking(msg, mSynListener);
        } else {
            mTts.startSpeaking(msg, mSynListener1);
        }
    }

    //获取发音人资源路径
    private String getResourcePath(){
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "tts/xiaoyan.jet"));
        return tempBuffer.toString();
    }

    //合成监听器
    SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
            Xunfei_Tingxie Tingxiethread = new Xunfei_Tingxie(context, handler, mWakeuperListener);
            Tingxiethread.start();
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };


    //合成监听器
    SynthesizerListener mSynListener1 = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
            //开始唤醒
            final VoiceWakeuper mIvw = VoiceWakeuper.createWakeuper(context, null);
            mIvw.startListening(mWakeuperListener);
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {

        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };


}


