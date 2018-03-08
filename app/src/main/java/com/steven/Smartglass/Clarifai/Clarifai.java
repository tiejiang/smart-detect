package com.steven.Smartglass.Clarifai;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.steven.Smartglass.Baidutranslate.TransApi;

import java.io.File;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;

import static com.steven.Smartglass.ResultActivity.ClarifaiMSGwhat;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class Clarifai extends Thread {

    private Gson gson;
    private Handler handler;
    private String str;
    private TransApi transApi = new TransApi();
    private String clientId = "EKaa_OfUMzozDovQUgMWSfgjczoAH7X7dYP6uRgN";
    private String clientSecret = "cBmTX_HrRm3tLR_EmJ11R1K-vQfYrQhh-8kropCV";

    public Clarifai(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            ClarifaiClient client = new ClarifaiBuilder(clientId, clientSecret).buildSync();
            Log.d("TAG", "Step1");
            File tempFile = new File("/sdcard/temp.jpeg");
            List<ClarifaiOutput<Concept>> predictionResults =
                    client.getDefaultModels().generalModel().predict()
                            .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(tempFile))).executeSync().get();

            str = predictionResults.get(0).data().get(0).name()+ "\n";
            for (int i = 1; i < 8; i++) {
                str = str + predictionResults.get(0).data().get(i).name() + "\n";
            }
        } catch (Exception e) {
            System.out.println("网络请求失败");
        }
        handler.obtainMessage(ClarifaiMSGwhat, str).sendToTarget();
    }
}
