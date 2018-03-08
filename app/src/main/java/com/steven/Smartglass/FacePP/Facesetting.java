package com.steven.Smartglass.FacePP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.steven.Smartglass.R;
import com.steven.Smartglass.ResultActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class Facesetting extends Activity {

    private Button gainkey;
    private Button add_faceset;
    private Button up_faceset;
    private Button clear;
    private Button back;
    private ImageView picview;
    private EditText sthEdit;
    private String face_tokens;
    private String user_id;
    private String nd = "\n";
    private static Handler handler;
    private Context context = this;
    public static final int GainkeyMSGwhat = 0;
    public static final int Add_facesetMSGwhat = 1;
    public static final int Up_facesetMSGwhat = 2;
    public static final int ClearMSGwhat = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facesetting);

        sthEdit = (EditText) findViewById(R.id.sthEdit);

        File tempFile = new File("/sdcard/temp.jpeg");
        String path = tempFile.getAbsolutePath();
        picview = (ImageView) findViewById(R.id.picview);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        picview.setImageBitmap(bitmap);

        handler = new

                Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        String TTSmsg = msg.obj.toString();
                        switch (msg.what) {
                            case GainkeyMSGwhat:
                                face_tokens = TTSmsg;
                                Toast.makeText(context, TTSmsg, Toast.LENGTH_SHORT).show();
                                break;
                            case Add_facesetMSGwhat:
                                Toast.makeText(context, TTSmsg, Toast.LENGTH_SHORT).show();
                                break;
                            case Up_facesetMSGwhat:
                                Toast.makeText(context, TTSmsg, Toast.LENGTH_SHORT).show();
                                break;
                            case ClearMSGwhat:
                                Toast.makeText(context, TTSmsg, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

        clear = (Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface";
                Faceset faceset = new Faceset(null, url, handler, null, null);
                faceset.start();
            }
        });

        gainkey = (Button) findViewById(R.id.gainkey);
        gainkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
                Faceset faceset = new Faceset(file, url, handler, null, null);
                faceset.start();
            }
        });


        add_faceset = (Button) findViewById(R.id.add_faceset);
        add_faceset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api-cn.faceplusplus.com/facepp/v3/face/setuserid";
                user_id = sthEdit.getText().toString();
                try {
                    user_id = URLDecoder.decode(user_id, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Faceset faceset = new Faceset(null, url, handler, face_tokens, user_id);
                faceset.start();
            }
        });

        up_faceset = (Button) findViewById(R.id.up_faceset);
        up_faceset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
                Faceset faceset = new Faceset(null, url, handler, face_tokens, null);
                faceset.start();
            }
        });


        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Facesetting.this, ResultActivity.class);
                startActivity(intent);
                Facesetting.this.finish();
            }
        });


    }


}
