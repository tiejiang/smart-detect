package com.steven.Smartglass.FacePP;

import android.os.Handler;

import com.google.gson.Gson;
import com.steven.Smartglass.Baidutranslate.TransApi;
import com.steven.Smartglass.Baidutranslate.TransJsonDeco;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

import static com.steven.Smartglass.ResultActivity.FaceppMSGwhat;
import static com.steven.Smartglass.ResultActivity.ShibieMSGwhat;


public class Faceplusplus extends Thread {

    private File file;
    private String url;
    private String TrScen;
    private String TrObj;
    private String Trupper;
    private String Trlower;
    private TransApi transApi = new TransApi();
    private Handler handler;
    Gson gson = new Gson();

    public Faceplusplus(File file, String url, Handler handler) {
        this.file = file;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {

        String finalstr = null;
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "cbO8a7P4P6CLfXBLJcKf8ahE0OP1b0eK");
        map.put("api_secret", "_T-Zxi2xJa-G3W4lZLq7plfdY5dV04mI");
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/detect") {  //人脸识别
            map.put("return_attributes", "gender,age,ethnicity,smiling,glass");
        }
        if (url == "https://api-cn.faceplusplus.com/humanbodypp/beta/detect") { //人体识别
            map.put("return_attributes", "gender,cloth_color");
        }
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/search") { //人脸比对
            map.put("outer_id", "test_outer_id");
        }
        byteMap.put("image_file", buff);

        try {
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            System.out.println("服务器返回的json：" + str);

            if (url == "https://api-cn.faceplusplus.com/facepp/v3/search") { //人脸比对
                SearchjsonDeco searchjsonDeco = gson.fromJson(str, SearchjsonDeco.class);
                try {
                    if ((searchjsonDeco.getResults().size()) > 0) {
                        finalstr = searchjsonDeco.getResults().get(0).getUser_id();
                    }
                    handler.obtainMessage(ShibieMSGwhat, finalstr.toString()).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(ShibieMSGwhat, "并发数限制").sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/imagepp/beta/detectsceneandobject") {//图像识别处理
                String scenesvalue = null;
                String objectsvalue = null;
                PicjsonDeco picjsonDeco = gson.fromJson(str, PicjsonDeco.class);
                try {
                    if ((picjsonDeco.getScenes().size()) > 0) {
                        scenesvalue = picjsonDeco.getScenes().get(0).getValue();
                        TrScen = transApi.getTransResult(scenesvalue, "en", "zh");
                        TransJsonDeco transJsonDeco = gson.fromJson(TrScen, TransJsonDeco.class);
                        TrScen = transJsonDeco.getTrans_result().get(0).getDst();
                    }
                    if ((picjsonDeco.getObjects().size()) > 0) {
                        objectsvalue = picjsonDeco.getObjects().get(0).getValue();
                        TrObj = transApi.getTransResult(objectsvalue, "en", "zh");
                        TransJsonDeco transJsonDeco = gson.fromJson(TrObj, TransJsonDeco.class);
                        TrObj = transJsonDeco.getTrans_result().get(0).getDst();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(FaceppMSGwhat, "这个超出我的认知范围了,请再来一次吧").sendToTarget();
                }
                if (scenesvalue != null && objectsvalue != null) {
                    finalstr = "您所看到的场景是：" + TrScen + "\n" + "物体是：" + TrObj;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                }
                if (scenesvalue == null && objectsvalue != null) {
                    finalstr = "您所看到的物体是：" + TrObj;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                }
                if (scenesvalue != null && objectsvalue == null) {
                    finalstr = "您所看到的场景是：" + TrScen;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/humanbodypp/beta/detect") {//人体识别处理
                String gendervalue = null;
                String uppervalue = null;
                String lowervalue = null;
                BodyjsonDeco bodyjsonDeco = gson.fromJson(str, BodyjsonDeco.class);
                try {
                    if ((bodyjsonDeco.getHumanbodies().size()) > 0) {
                        gendervalue = bodyjsonDeco.getHumanbodies().get(0).getAttributes().getGender().getValue();
                        if (gendervalue.equals("Male")) {
                            gendervalue = "男性";
                        } else {
                            gendervalue = "女性";
                        }
                        uppervalue = bodyjsonDeco.getHumanbodies().get(0).getAttributes().getUpper_body_cloth_color();
                        lowervalue = bodyjsonDeco.getHumanbodies().get(0).getAttributes().getLower_body_cloth_color();
                        Trupper = transApi.getTransResult(uppervalue, "en", "zh");
                        TransJsonDeco upperJsonDeco = gson.fromJson(Trupper, TransJsonDeco.class);
                        Trupper = upperJsonDeco.getTrans_result().get(0).getDst();

                        Trlower = transApi.getTransResult(lowervalue, "en", "zh");
                        TransJsonDeco lowerJsonDeco = gson.fromJson(Trlower, TransJsonDeco.class);
                        Trlower = lowerJsonDeco.getTrans_result().get(0).getDst();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(FaceppMSGwhat, "没有检测到人体,请再来一次吧").sendToTarget();
                }
                if (uppervalue != null && lowervalue != null) {
                    finalstr = "你眼前是一位穿着" + Trupper + "衣服，" + Trlower + "裤子的" + gendervalue;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                }
                if (uppervalue == null && lowervalue != null) {
                    finalstr = "你眼前是一位穿着" + Trlower + "裤子的" + gendervalue;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                }
                if (uppervalue != null && lowervalue == null) {
                    finalstr = "你眼前是一位穿着" + Trupper + "衣服的" + gendervalue;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/imagepp/beta/recognizetext") {//文字识别处理

                //https://api-cn.faceplusplus.com/imagepp/beta/recognizetext
                //https://api-cn.faceplusplus.com/imagepp/v1/recognizetext
                String type = "";
                String textvalue = "";
                ArrayList<Textorder> textorder = new ArrayList<Textorder>();
                TextjsonDeco textjson = gson.fromJson(str, TextjsonDeco.class);
                try {
                    if ((textjson.getResult().size()) > 0) {
                        for (int i = 0; i < textjson.getResult().size(); i++) {
                            type = textjson.getResult().get(i).getType();
                            System.out.println("type:" + type);
                            if (type.equals("textline")) {
                                textorder.add(new Textorder(textjson.getResult().get(i).getValue(),
                                        textjson.getResult().get(i).getChildobjects().get(0).getPosition().get(0).getY()));
                            }
                        }
                    }
                    Collections.sort(textorder, new Comparator<Textorder>() {
                        public int compare(Textorder arg0, Textorder arg1) {
                            return arg0.getY() - arg1.getY(); //按照Y升序
                        }
                    });
                    for (Textorder e : textorder) {
                        System.out.println("Y :" + e.getY() + "   文字:" + e.getTextorder());
                        textvalue = textvalue + e.getTextorder();
                    }
                    finalstr = textvalue;
                    handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(FaceppMSGwhat, "没有检测到可识别的文字,请再来一次吧").sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/facepp/v3/detect") {//人脸识别处理
                String gendervalue = null;
                String face_tokens = null;
                int agevalue;
                double smile;
                String smiledf = null;
                String ethnicityvalue = null;
                String glass = null;
                //DecimalFormat df = new DecimalFormat(".##");
                FacejsonDeco facejsonDeco = gson.fromJson(str, FacejsonDeco.class);
                try {
                    if (facejsonDeco.getFaces().size() > 0) {
                        gendervalue = facejsonDeco.getFaces().get(0).getAttributes().getGender().getValue();
                        if (gendervalue.equals("Male")) {
                            gendervalue = "男性";
                        } else
                            gendervalue = "女性";
                        agevalue = facejsonDeco.getFaces().get(0).getAttributes().getAge().getValue();
                        ethnicityvalue = facejsonDeco.getFaces().get(0).getAttributes().getEthnicity().getValue();
                        if (ethnicityvalue.equals("Asian")) {
                            ethnicityvalue = "亚洲";
                        } else if (ethnicityvalue.equals("White")) {
                            ethnicityvalue = "白人";
                        } else
                            ethnicityvalue = "黑人";
                        smile = facejsonDeco.getFaces().get(0).getAttributes().getSmile().getValue();
                        if (smile < 20) {
                            smiledf = "微笑着的";
                        } else if (smile < 60) {
                            smiledf = "浅笑着的";
                        } else {
                            smiledf = "大笑着的";
                        }
                        glass = facejsonDeco.getFaces().get(0).getAttributes().getGlass().getValue();
                        if (glass.equals("None")) {
                            glass = "";
                        } else if (glass.equals("Dark")) {
                            glass = "佩戴黑框眼镜或墨镜,";
                        } else
                            glass = "佩戴普通眼镜,";
                        //blurdf = df.format(blur);
                        finalstr = "这是一位" + glass + "大概" + agevalue + "岁，" + smiledf + ethnicityvalue + gendervalue;
                        handler.obtainMessage(FaceppMSGwhat, finalstr).sendToTarget();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(FaceppMSGwhat, "没有检测到可识别的人脸,请再来一次吧").sendToTarget();
                }
            } else {
                handler.obtainMessage(FaceppMSGwhat, "传入的URL错误").sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Textorder {
        String textorder;
        int y;

        public Textorder(String textorder, int y) {
            this.textorder = textorder;
            this.y = y;
        }

        public String getTextorder() {
            return textorder;
        }

        public void setTextorder(String textorder) {
            this.textorder = textorder;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();

    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if (fileMap != null && fileMap.size() > 0) {
            Iterator fileIter = fileMap.entrySet().iterator();
            while (fileIter.hasNext()) {
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try {
            if (code == 200) {
                ins = conne.getInputStream();
            } else {
                ins = conne.getErrorStream();
            }
        } catch (SSLException e) {
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while ((len = ins.read(buff)) != -1) {
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }

    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}
