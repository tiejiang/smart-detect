package com.steven.Smartglass.Clarifai;

import android.os.Handler;

import com.google.gson.Gson;
import com.steven.Smartglass.Baidutranslate.TransApi;
import com.steven.Smartglass.Baidutranslate.TransJsonDeco;

import static com.steven.Smartglass.ResultActivity.ClarifaitrMSGwhat;

/**
 * Created by Administrator on 2017/4/27 0027.
 */

public class Clarifaitr extends Thread {

    private TransApi transApi = new TransApi();
    private String str;
    private String str1 = "";
    private Handler handler;
    private Gson gson = new Gson();

    public Clarifaitr(String str, Handler handler) {
        this.str = str;
        this.handler = handler;
    }

    @Override
    public void run() {
        str1 = transApi.getTransResult(str, "en", "zh");
        System.out.println("-----------------翻译JSON：" + str1);
        //try {
        if (!str1.equals("null")) {
            TransJsonDeco clarifaitrans = gson.fromJson(str1, TransJsonDeco.class);
            str1 = clarifaitrans.getTrans_result().get(0).getDst() + "\n";
            for (int i = 1; i < clarifaitrans.getTrans_result().size(); i++) {
                str1 = str1 + clarifaitrans.getTrans_result().get(i).getDst() + "\n";
            }
            handler.obtainMessage(ClarifaitrMSGwhat, str1).sendToTarget();
        } else {
            System.out.println("-------------------Str1 is null");
        }
        // } catch (Exception e) {
        //   System.out.println("-------------------翻译解析出错");
        //}
    }

}
