package com.steven.Smartglass.Detect;

import android.os.Handler;

import com.google.gson.Gson;
import com.steven.Smartglass.Baidutranslate.TransApi;
import com.steven.Smartglass.FacePP.FacejsonDeco;
import com.steven.Smartglass.FacePP.SearchjsonDeco;

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

import static com.steven.Smartglass.ResultActivity.DetectFaceppMSGwhat;
import static com.steven.Smartglass.ResultActivity.DetectShibieMSGwhat;


public class FaceDetect extends Thread {

    private File file;
    private String url;
    private String TrScen;
    private String TrObj;
    private String Trupper;
    private String Trlower;
    private TransApi transApi = new TransApi();
    private Handler handler;
    Gson gson = new Gson();

    public FaceDetect(File file, String url, Handler handler) {
        this.file = file;
        this.url = url;
        this.handler = handler;
    }

    @Override
    public void run() {

        String finalstr = null;
        double thresholds3;
        double thresholds4;
        double thresholds5;
        double confidence;
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "cbO8a7P4P6CLfXBLJcKf8ahE0OP1b0eK");
        map.put("api_secret", "_T-Zxi2xJa-G3W4lZLq7plfdY5dV04mI");
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/detect") { //人脸识别
            map.put("return_attributes", "gender,age,ethnicity,smiling,glass");
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
                        confidence = searchjsonDeco.getResults().get(0).getConfidence();
                        thresholds3 = searchjsonDeco.getThresholds().get_$1e3();
                        if (confidence < thresholds3) {
                            finalstr = "此人不存在";
                        } else {
                            finalstr = searchjsonDeco.getResults().get(0).getUser_id();
                        }
                        handler.obtainMessage(DetectShibieMSGwhat, finalstr.toString()).sendToTarget();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(DetectShibieMSGwhat, "没有检测到人脸或并发数限制").sendToTarget();
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
                        handler.obtainMessage(DetectFaceppMSGwhat, finalstr).sendToTarget();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(DetectFaceppMSGwhat, "没有检测到可识别的人脸,请再来一次吧").sendToTarget();
                }
            } else {
                handler.obtainMessage(DetectFaceppMSGwhat, "传入的URL错误").sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
