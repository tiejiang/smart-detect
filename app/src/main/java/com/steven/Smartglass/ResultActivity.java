package com.steven.Smartglass;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.intchip.media.SDLNative;
import com.steven.Smartglass.BT.bluetoothUtil.BluetoothTools;
import com.steven.Smartglass.BT.bluetoothUtil.TransmitBean;
import com.steven.Smartglass.Baidutranslate.TransApi;
import com.steven.Smartglass.Clarifai.Clarifai;
import com.steven.Smartglass.Clarifai.Clarifaitr;
import com.steven.Smartglass.Detect.Detect_TTS;
import com.steven.Smartglass.Detect.FaceDetect;
import com.steven.Smartglass.DetectCamera.activity.CameraActivity;
import com.steven.Smartglass.FacePP.FaceAndColthes;
import com.steven.Smartglass.FacePP.Faceplusplus;
import com.steven.Smartglass.FacePP.Facesetting;
import com.steven.Smartglass.Upload.Upload;
import com.steven.Smartglass.XunFei.Xunfei_TTS;
import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.TuringManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.content.ContentValues.TAG;


public class ResultActivity extends Activity {

    private Button voice;
    private Button takepic;
    private Button facepic;
    private Button detect;
    private Button button;
    private Button bluetoothstart;
    private Button bluetoothstop;
    private ImageView imageView;
    private TextView tv;
    private TextView turingtv;
    private static Handler handler;
    private Context context = this;
    private newCamera newCamera = null;
    private SurfaceHolder holder;
    private Gson gson;
    private SoundPool soundPool;
    private TransApi transApi = new TransApi();
    private WifiManager mWifiManager = null;
    public WifiAutoConnectManager mWifiAutoConnectManager;
    public static final int TuringMSGwhat = 0;
    public static final int TingxieMSGwhat = 1;
    public static final int FaceppMSGwhat = 2;
    public static final int UploadMSGwhat = 3;
    public static final int ShibieMSGwhat = 4;
    public static final int ClarifaiMSGwhat = 5;
    public static final int ClarifaitrMSGwhat = 6;
    public static final int FaceclothesMSGwhat = 7;
    public static final int DetectShibieMSGwhat = 8;
    public static final int DetectFaceppMSGwhat = 9;
    public static final int DetectTuringMSGwhat = 10;
    //
    public StreamMadiaPlayer mStreamMadiaPlayer;
    public SurfaceView mVideoSurfaceView;
    public static ResultActivity mResActivityInstance;
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static   String storagePath = "";
    private static final String DST_FOLDER_NAME = "CameraPic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mResActivityInstance = this;
//        mStreamMadiaPlayer = (StreamMadiaPlayer)findViewById(com.intchip.media.R.id.main_surface);
        mStreamMadiaPlayer = (StreamMadiaPlayer)findViewById(com.steven.Smartglass.R.id.main_surface);
        initPath();
        //初始化讯飞语音
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5a256161");
        final SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
        final VoiceWakeuper mIvw = VoiceWakeuper.createWakeuper(context, null);
        mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiAutoConnectManager = new WifiAutoConnectManager(mWifiManager);
//        mWifiAutoConnectManager.connect("a.intchip:56:7B", "", WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS);

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(context, R.raw.takepic, 1);
        imageView = (ImageView) findViewById(R.id.pic);
        button = (Button) findViewById(R.id.btn_take_pic);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDLNative.snapshot(ResultActivity.initPath()+"snapshot_pic.jpg");
//                Intent BTintent = new Intent(ResultActivity.this, ClientActivity.class);
//                startActivity(BTintent);
            }
        });

//        bluetoothstart = (Button) findViewById(R.id.bluetoothstart);
//        bluetoothstart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //开启后台service
//                Intent startService = new Intent(ResultActivity.this, BluetoothServerService.class);
//                startService(startService);
//                //注册BoradcastReceiver
//                IntentFilter intentFilter = new IntentFilter();
//                intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
//                intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
//                registerReceiver(broadcastReceiver, intentFilter);
//                tv.setText("正在连接...");
//            }
//        });
//
//        bluetoothstop = (Button) findViewById(R.id.bluetoothstop);
//        bluetoothstop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //关闭后台Service
//                Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
//                sendBroadcast(startService);
//                unregisterReceiver(broadcastReceiver);
//                tv.setText("蓝牙已关闭");
//            }
//        });

        detect = (Button) findViewById(R.id.detect);
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, CameraActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        facepic = (Button) findViewById(R.id.facepic);
        facepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCamera = (com.steven.Smartglass.newCamera) findViewById(R.id.newCamera);
                newCamera.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
                turingtv.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        newCamera.takePicture();
                        //soundPool.play(1,1,1,0,0,1);
                        new Xunfei_TTS(context, mTts, "正在识别，请稍等", handler, mWakeuperListener);

                    }
                }, 1000);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        newCamera.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(ResultActivity.this, Facesetting.class);
                        startActivity(intent);
                        ResultActivity.this.finish();
                        facepic.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.VISIBLE);
                        turingtv.setVisibility(View.VISIBLE);
                    }
                }, 2000);
            }
        });

        takepic = (Button) findViewById(R.id.takepic); //xml中设置了不可见
        takepic.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   newCamera = (com.steven.Smartglass.newCamera) findViewById(R.id.newCamera);
                   newCamera.setVisibility(View.VISIBLE);
                   tv.setVisibility(View.INVISIBLE);
                   turingtv.setVisibility(View.INVISIBLE);
                   new Handler().postDelayed(new Runnable() {
                       public void run() {
                           newCamera.takePicture();
                       }
                   }, 1000);
                   new Handler().postDelayed(new Runnable() {
                       public void run() {
                           newCamera.setVisibility(View.INVISIBLE);
                           File tempFile = new File("/sdcard/temp.jpeg");
                           String path = tempFile.getAbsolutePath();
                           Bitmap bitmap = BitmapFactory.decodeFile(path);
                           imageView.setImageBitmap(bitmap);
                           facepic.setVisibility(View.VISIBLE);
                           tv.setVisibility(View.VISIBLE);
                           turingtv.setVisibility(View.VISIBLE);
                           //soundPool.play(1,1,1,0,0,1);
                           new Xunfei_TTS(context, mTts, "正在识别，请稍等", handler, mWakeuperListener);
                       }
                   }, 2000);
               }
           }

        );


        tv = (TextView) findViewById(R.id.textView);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        turingtv = (TextView) findViewById(R.id.turing);
        turingtv.setMovementMethod(ScrollingMovementMethod.getInstance());

        handler = new

                Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        System.out.println("-----------msg.what:" + msg.what);
                        try {
                            String TTSmsg = msg.obj.toString();
                            System.out.println("-----------TTSmsg:" + TTSmsg);
                            switch (msg.what) {
                                case TingxieMSGwhat:
                                    tv.setText(TTSmsg);
                                    //VoiceContorl(TTSmsg);
                                    Turing(context, TTSmsg);
                                    break;
                                case TuringMSGwhat:
                                    turingtv.setText(TTSmsg);
                                    VoiceContorl(TTSmsg);
                                    break;
                                case FaceppMSGwhat:
                                    tv.setText(TTSmsg);
                                    new Xunfei_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
                                    break;
                                case UploadMSGwhat:
                                    tv.setText(TTSmsg);
                                    new Xunfei_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
                                    break;
                                case ShibieMSGwhat:
                                    Turing(context, TTSmsg);
                                    Toast.makeText(context, TTSmsg, Toast.LENGTH_SHORT).show();
                                    break;
                                case ClarifaiMSGwhat:
                                    try {
                                        Clarifaitr clarifaitr = new Clarifaitr(TTSmsg, handler);
                                        clarifaitr.start();
                                        break;
                                    } catch (Exception e) {
                                        System.out.println("-----------transJsonDeco is null:");
                                        Clarifai clarifai = new Clarifai(handler);
                                        clarifai.start();
                                        break;
                                    }
                                case ClarifaitrMSGwhat:
                                    tv.setText(TTSmsg);
                                    new Xunfei_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
                                    break;
                                case FaceclothesMSGwhat:
                                    tv.setText(TTSmsg);
                                    new Xunfei_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
                                    break;
                                case DetectFaceppMSGwhat:
                                    tv.setText(TTSmsg);
                                    new Detect_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
                                    break;
                                case DetectShibieMSGwhat:
                                    turingtv.setText(TTSmsg);
                                    if (TTSmsg.equals("此人不存在")) {
                                        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                                        FaceDetect faceDetect = new FaceDetect(file, "https://api-cn.faceplusplus.com/facepp/v3/detect", handler);
                                        faceDetect.start();
                                    } else
                                        DetectTuring(context, TTSmsg);
                                    break;
                                case DetectTuringMSGwhat:
                                    tv.setText(TTSmsg);
                                    new Detect_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
                                    break;
                            }
                        } catch (Exception e) {
                            System.out.println("-------------TTSmsg is null");
                        }
                    }
                };


        voice = (Button) findViewById(R.id.voice);
        voice.setOnClickListener(new View.OnClickListener()

             {
                 @Override
                 public void onClick(View v) {
                     takepic.performClick();
//                     new Xunfei_TTS(context, mTts, "我在,请说", handler, mWakeuperListener);
                 }
             }
        );

        //语音唤醒测试
        //1.加载唤醒词资源，resPath为唤醒资源路径
        StringBuffer param = new StringBuffer();
        String resPath = ResourceUtil.generateResourcePath(context, ResourceUtil.RESOURCE_TYPE.assets, "ivw/5a256161.jet");
        param.append(ResourceUtil.IVW_RES_PATH + "=" + resPath);
        param.append("," + ResourceUtil.ENGINE_START + "=" + SpeechConstant.ENG_IVW);
        SpeechUtility.getUtility().setParameter(ResourceUtil.ENGINE_START, param.toString());
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + "我前面是谁");
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "1:" + "我在哪里");
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "2:" + "读给我听");
        //设置当前业务类型为唤醒
        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
        //设置唤醒一直保持，直到调用stopListening，传入0则完成一次唤醒后，会话立即结束（默认0）
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
        //4.开始唤醒
        mIvw.startListening(mWakeuperListener);
    }

    public static ResultActivity getResActivityInstance(){

        return mResActivityInstance;
    }

    public StreamMadiaPlayer getStreamMadiaPlayerInstance(){

        return mStreamMadiaPlayer;
    }
    @Override
    protected void onStart() {
        super.onStart();
//        Log.d("TIEJIANG", "mStreamMadiaPlayer= "+mStreamMadiaPlayer);
        mStreamMadiaPlayer.onNetworkConnected();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStreamMadiaPlayer.onDestory();
//        isOnLine = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStreamMadiaPlayer.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStreamMadiaPlayer.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                System.out.println("执行检测完识别");
                File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                FaceDetect faceDetect = new FaceDetect(file, "https://api-cn.faceplusplus.com/facepp/v3/search", handler);
                faceDetect.start();
                Toast.makeText(context, "正在识别，请稍等...", Toast.LENGTH_SHORT).show();

                SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
                new Xunfei_TTS(context, mTts, "正在识别，请稍等", handler, mWakeuperListener);
                break;
            default:
                break;
        }
    }

    public static String initPath(){
        if(storagePath.equals("")){
            storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if(!f.exists()){
                f.mkdir();
            }
        }
        return storagePath;
    }

    //听写监听器
    public WakeuperListener mWakeuperListener = new WakeuperListener() {
        public void onResult(WakeuperResult result) {
            String text = result.getResultString();
            System.out.println("----------------语音唤醒:" + text);
            Toast.makeText(context, "语音唤醒开启", Toast.LENGTH_SHORT).show();
            //RecDeco recDeco = gson.fromJson(text, RecDeco.class);
            try {
                JSONObject textjson = new JSONObject(text);
                switch (Integer.parseInt(String.valueOf(textjson.get("id")))) {
                    case 0:
                        try {
                            /*CameraActivity.Detectfinish.finish();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(context, CameraActivity.class);
                                    startActivityForResult(intent, 0);
                                }
                            }, 500);*/
                            Log.d("TIEJIANG", "ResultActivity---WakeuperListener"+" wake up");
                            takepic.performClick(); //代码主动调用按键的点击事件（的方法）
                            //断开wifi/获得图像-->保持4G网络正常-->
//                            if (mWifiManager.isWifiEnabled()) {
//                                mWifiManager.setWifiEnabled(false);
//                            } else {
//                                mWifiManager.setWifiEnabled(true);
//                            }

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                                    FaceAndColthes FaceAndColthes = new FaceAndColthes(file, handler);
                                    FaceAndColthes.start();
                                    Toast.makeText(context, "正在识别，请稍等...", Toast.LENGTH_SHORT).show();
                                }
                            }, 3000);
                        } catch (Exception e) {

                        }
                        break;
                    case 2:
                        try {
                            CameraActivity.Detectfinish.finish();
                        } catch (Exception e) {
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                xIntent("https://api-cn.faceplusplus.com/imagepp/beta/recognizetext");
                            }
                        }, 500);
                        break;
                    case 1:
                        try {
                            CameraActivity.Detectfinish.finish();
                        } catch (Exception e) {
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                takepic.performClick();
                            }
                        }, 500);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Clarifai clarifai = new Clarifai(handler);
                                clarifai.start();
                                Toast.makeText(context, "正在识别，请稍等...", Toast.LENGTH_SHORT).show();
                            }
                        }, 3000);
                        break;
                    case 3:
                        try {
                            SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
                            mTts.stopSpeaking();
                            mTts.destroy();
                            break;
                        } catch (Exception e) {
                            break;
                        }
                    case 4:
                        break;
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }


        public void onError(SpeechError error) {

        }

        public void onBeginOfSpeech() {
        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            if (SpeechEvent.EVENT_IVW_RESULT == eventType) {
                //当使用唤醒+识别功能时获取识别结果
                //arg1:是否最后一个结果，1:是，0:否。
                RecognizerResult reslut = ((RecognizerResult) obj.get(SpeechEvent.KEY_EVENT_IVW_RESULT));
                System.out.println("========================唤醒+识别reslut：" + reslut);
            }
        }

        @Override
        public void onVolumeChanged(int i) {
        }
    };


    public void facepp(String url) {
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
        if (!file.exists()) {
            System.out.println("rcPic pic not exist:" + file.getAbsolutePath());
            return;
        } else {
            System.out.println("rcPic pic dir is:" + file.getAbsolutePath());
        }
        Faceplusplus faceplusplus = new Faceplusplus(file, url, handler);
        faceplusplus.start();
    }

    public void upload() {
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
        if (!file.exists()) {
            System.out.println("pic not exist:" + file.getAbsolutePath());
            return;
        } else
            System.out.println("pic dir is:" + file.getAbsolutePath());
        Upload uploadthread = new Upload(file, handler);
        uploadthread.start();
    }


    public void Turing(Context context, String text) {
        TuringManager mTuringManager;
        String TURING_APIKEY = "cbf002b72f5f47d991a13bfd87f27172";
        String TURING_SECRET = "6a01b96f4d898ab5";
        mTuringManager = new TuringManager(context, TURING_APIKEY, TURING_SECRET);
        mTuringManager.setHttpRequestListener(myHttpConnectionListener);
        mTuringManager.requestTuring(text);
    }

    //网络请求回调
    HttpRequestListener myHttpConnectionListener = new HttpRequestListener() {

        @Override
        public void onSuccess(String result) {
            if (result != null) {
                try {
                    Log.d(TAG, "result" + result);
                    JSONObject result_obj = new JSONObject(result);
                    if (result_obj.has("text")) {
                        Log.d(TAG, result_obj.get("text").toString());
                        handler.obtainMessage(TuringMSGwhat, result_obj.get("text").toString()).sendToTarget();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException:" + e.getMessage());
                }
            }
        }

        @Override
        public void onFail(int code, String error) {
            Log.d(TAG, "onFail code:" + code + "|error:" + error);
        }
    };

    public void DetectTuring(Context context, String text) {
        TuringManager mTuringManager;
        String TURING_APIKEY = "cbf002b72f5f47d991a13bfd87f27172";
        String TURING_SECRET = "6a01b96f4d898ab5";
        mTuringManager = new TuringManager(context, TURING_APIKEY, TURING_SECRET);
        mTuringManager.setHttpRequestListener(myHttpConnectionListenerDetect);
        mTuringManager.requestTuring(text);
    }

    //网络请求回调
    HttpRequestListener myHttpConnectionListenerDetect = new HttpRequestListener() {

        @Override
        public void onSuccess(String result) {
            if (result != null) {
                try {
                    Log.d(TAG, "result" + result);
                    JSONObject result_obj = new JSONObject(result);
                    if (result_obj.has("text")) {
                        Log.d(TAG, result_obj.get("text").toString());
                        handler.obtainMessage(DetectTuringMSGwhat, result_obj.get("text").toString()).sendToTarget();
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException:" + e.getMessage());
                }
            }
        }

        @Override
        public void onFail(int code, String error) {
            Log.d(TAG, "onFail code:" + code + "|error:" + error);
        }
    };

    public void Voicestop() {
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
        mTts.stopSpeaking();
        mTts.destroy();
        VoiceWakeuper mIvw = VoiceWakeuper.createWakeuper(context, null);
        mIvw.startListening(mWakeuperListener);
    }


    public void VoiceContorl(String TTSmsg) {

        if (TTSmsg.equals("图像识别")) {
            takepic.performClick();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Clarifai clarifai = new Clarifai(handler);
                    clarifai.start();
                    Toast.makeText(context, "正在识别，请稍等...", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        } else if (TTSmsg.equals("人脸识别")) {
            //xIntent("https://api-cn.faceplusplus.com/facepp/v3/detect");
            takepic.performClick();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                    FaceAndColthes FaceAndColthes = new FaceAndColthes(file, handler);
                    FaceAndColthes.start();
                    Toast.makeText(context, "正在识别，请稍等...", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        } else if (TTSmsg.equals("文字识别")) {
            xIntent("https://api-cn.faceplusplus.com/imagepp/beta/recognizetext");
        } else if (TTSmsg.equals("人体识别")) {
            xIntent("https://api-cn.faceplusplus.com/humanbodypp/beta/detect");
        } else if (TTSmsg.equals("人脸对比")) {
            xIntent("https://api-cn.faceplusplus.com/facepp/v3/search");
        } else if (TTSmsg.equals("开启人脸检测")) {
            Intent intent = new Intent(ResultActivity.this, CameraActivity.class);
            startActivityForResult(intent, 0);
        } else if (TTSmsg.equals("关闭人脸检测")) {
            CameraActivity.Detectfinish.finish();
        } else if (TTSmsg.equals("上传")) {
            takepic.performClick();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    upload();
                    Toast.makeText(context, "正在上传，请稍等...", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        } else if (TTSmsg.equals("语音停止")) {
            Voicestop();
        } else {
            SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
            new Xunfei_TTS(context, mTts, TTSmsg, handler, mWakeuperListener);
        }

    }

    public void xIntent(final String url) {
        takepic.performClick();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                facepp(url);
                Toast.makeText(context, "正在识别，请稍等...", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }


    //广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
                //接收数据
                TransmitBean data = (TransmitBean) intent.getExtras().getSerializable(BluetoothTools.DATA);
                //String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
                String msg = data.getMsg();
                turingtv.setText(msg);
                if (msg.equals("facedetect")) {
                    try {
                        CameraActivity.Detectfinish.finish();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(ResultActivity.this, CameraActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        }, 500);
                    } catch (Exception e) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent(ResultActivity.this, CameraActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        }, 500);
                    }
                } else if (msg.equals("textrec")) {
                    try {
                        CameraActivity.Detectfinish.finish();
                    } catch (Exception e) {
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            xIntent("https://api-cn.faceplusplus.com/imagepp/beta/recognizetext");
                        }
                    }, 500);
                } else if (msg.equals("scenerec")) {
                    try {
                        CameraActivity.Detectfinish.finish();
                    } catch (Exception e) {
                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            takepic.performClick();
                        }
                    }, 500);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Clarifai clarifai = new Clarifai(handler);
                            clarifai.start();
                        }
                    }, 3000);
                } else if (msg.equals("stoptts")) {
                    try {
                        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
                        mTts.stopSpeaking();
                        mTts.destroy();
                    } catch (Exception e) {
                    }
                } else {
                }

            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                //连接成功
                tv.setText("连接成功");
            }
        }
    };
}







