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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

import static com.steven.Smartglass.ResultActivity.FaceppMSGwhat;
import static com.steven.Smartglass.ResultActivity.FaceclothesMSGwhat;
import static com.steven.Smartglass.ResultActivity.ShibieMSGwhat;

public class FaceAndColthes extends Thread {

    private File file;
    private String TrScen;
    private String TrObj;
    private String Trupper;
    private String Trlower;
    private TransApi transApi = new TransApi();
    private Handler handler;
    private Gson gson = new Gson();
    private String finalstr = null;
    private String url1 = "https://api-cn.faceplusplus.com/facepp/v3/detect";  //人脸识别
    String gendervalue = null;
    int agevalue = 0;
    double smile;
    String smiledf = null;
    String ethnicityvalue = null;
    String glass = null;
    private String url2 = "https://api-cn.faceplusplus.com/humanbodypp/beta/detect";//人体识别
    String uppervalue = null;
    String lowervalue = null;
    private String url3 = "https://api-cn.faceplusplus.com/facepp/v3/search";
    double thresholds3;
    double confidence;

    public FaceAndColthes(File file, Handler handler) {
        this.file = file;
        this.handler = handler;
    }

    @Override
    public void run() {
        facesearch();
        if (finalstr.equals("此人不存在")) {
            face();
            clothes();
            if (agevalue != 0) {
                finalstr = "这是一位" + glass + "大概" + agevalue + "岁，" + "穿着" + Trupper + "衣服,"
                        + smiledf + ethnicityvalue + gendervalue;
            } else {
                finalstr = "没有检测到人哦，请再来一次吧";
            }
            handler.obtainMessage(FaceclothesMSGwhat, finalstr).sendToTarget();
        } else if(finalstr.equals("没有检测到人脸哦")) {
            handler.obtainMessage(FaceclothesMSGwhat, "没有检测到人脸哦").sendToTarget();
        }else {
            handler.obtainMessage(ShibieMSGwhat, finalstr.toString()).sendToTarget();
        }
    }

    public void facesearch() {

        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "cbO8a7P4P6CLfXBLJcKf8ahE0OP1b0eK");
        map.put("api_secret", "_T-Zxi2xJa-G3W4lZLq7plfdY5dV04mI");
        map.put("outer_id", "test_outer_id");
        byteMap.put("image_file", buff);

        byte[] bacd = new byte[0];
        try {
            bacd = post(url3, map, byteMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = new String(bacd);
        System.out.println("服务器返回的json：" + str);
        SearchjsonDeco searchjsonDeco = gson.fromJson(str, SearchjsonDeco.class);
        try {
            if ((searchjsonDeco.getResults().size()) > 0) {
                confidence = searchjsonDeco.getResults().get(0).getConfidence();
                thresholds3 = searchjsonDeco.getThresholds().get_$1e3();
                if (confidence < thresholds3) {
                    finalstr = "此人不存在";
                } else {
                    finalstr = searchjsonDeco.getResults().get(0).getUser_id();
                }
            }
        } catch (Exception e) {
            finalstr = "没有检测到人脸哦";
        }
    }

    public void face() {
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "cbO8a7P4P6CLfXBLJcKf8ahE0OP1b0eK");
        map.put("api_secret", "_T-Zxi2xJa-G3W4lZLq7plfdY5dV04mI");
        map.put("return_attributes", "gender,age,ethnicity,smiling,glass");
        byteMap.put("image_file", buff);

        byte[] bacd = new byte[0];
        try {
            bacd = post(url1, map, byteMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = new String(bacd);
        System.out.println("服务器返回的json：" + str);


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
            }
        } catch (Exception e) {
            handler.obtainMessage(FaceclothesMSGwhat, "没有检测到可识别的人脸,请再来一次吧").sendToTarget();
        }

    }

    public void clothes() {

        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "cbO8a7P4P6CLfXBLJcKf8ahE0OP1b0eK");
        map.put("api_secret", "_T-Zxi2xJa-G3W4lZLq7plfdY5dV04mI");
        map.put("return_attributes", "gender,cloth_color");
        byteMap.put("image_file", buff);

        byte[] bacd = new byte[0];
        try {
            bacd = post(url2, map, byteMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = new String(bacd);
        System.out.println("服务器返回的json：" + str);
        BodyjsonDeco bodyjsonDeco = gson.fromJson(str, BodyjsonDeco.class);
        try {
            if ((bodyjsonDeco.getHumanbodies().size()) > 0) {
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
            handler.obtainMessage(FaceclothesMSGwhat, "没有检测到人体,请再来一次吧").sendToTarget();
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