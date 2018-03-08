package com.steven.Smartglass.FacePP;

import android.os.Handler;

import com.google.gson.Gson;
import com.steven.Smartglass.Baidutranslate.TransApi;

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

import static com.steven.Smartglass.FacePP.Facesetting.Add_facesetMSGwhat;
import static com.steven.Smartglass.FacePP.Facesetting.ClearMSGwhat;
import static com.steven.Smartglass.FacePP.Facesetting.GainkeyMSGwhat;
import static com.steven.Smartglass.FacePP.Facesetting.Up_facesetMSGwhat;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public class Faceset extends Thread {

    private File file;
    private String url;
    private String face_tokens;
    private String user_id;
    private String TrScen;
    private String TrObj;
    private String Trupper;
    private String Trlower;
    private TransApi transApi = new TransApi();
    private Handler handler;
    Gson gson = new Gson();

    public Faceset(File file, String url, Handler handler, String face_tokens, String user_id) {
        this.file = file;
        this.url = url;
        this.handler = handler;
        this.face_tokens = face_tokens;
        this.user_id = user_id;
    }

    @Override
    public void run() {

        String finalstr = null;
        byte[] buff = getBytesFromFile(file);
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", "cbO8a7P4P6CLfXBLJcKf8ahE0OP1b0eK");
        map.put("api_secret", "_T-Zxi2xJa-G3W4lZLq7plfdY5dV04mI");
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/detect") {  //获取key
            byteMap.put("image_file", buff);
        }
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/faceset/create") { //上传FaceSet
            map.put("outer_id", "test_outer_id");
            map.put("face_tokens", face_tokens);
            map.put("force_merge", "1");
        }
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/face/setuserid") { //添加user_id
            map.put("face_token", face_tokens);
            map.put("user_id", user_id);
            System.out.println("---------user_id：" + user_id);
        }
        if (url == "https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface") { //清除
            map.put("outer_id", "test_outer_id");
            map.put("face_tokens", "RemoveAllFaceTokens");
        }

        try {
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);

            System.out.println("服务器返回的json：" + str);

            if (url == "https://api-cn.faceplusplus.com/facepp/v3/face/setuserid") { //添加user_id
                AuserjsonDeco auserjsonDeco = gson.fromJson(str, AuserjsonDeco.class);
                try {
                    if ((auserjsonDeco.getError_message().getBytes().length) > 0)
                        handler.obtainMessage(Add_facesetMSGwhat, "添加失败:" + auserjsonDeco.getError_message().getBytes()).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(Add_facesetMSGwhat, "添加成功").sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/facepp/v3/faceset/create") {//上传FaceSet
                FacesetjsonDeco facesetjsonDeco = gson.fromJson(str, FacesetjsonDeco.class);
                try {
                    if ((facesetjsonDeco.getError_message().getBytes().length) > 0)
                        handler.obtainMessage(Up_facesetMSGwhat, "上传失败:" + facesetjsonDeco.getError_message().getBytes()).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(Up_facesetMSGwhat, "上传成功").sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/facepp/v3/detect") {//获取key
                String face_tokens = null;
                FacejsonDeco facejsonDeco = gson.fromJson(str, FacejsonDeco.class);
                try {
                    if (facejsonDeco.getFaces().size() > 0) {
                        face_tokens = facejsonDeco.getFaces().get(0).getFace_token();
                        handler.obtainMessage(GainkeyMSGwhat, face_tokens).sendToTarget();
                    }else {
                        handler.obtainMessage(GainkeyMSGwhat, "没有检测到人脸").sendToTarget();
                    }
                } catch (Exception e) {
                    handler.obtainMessage(GainkeyMSGwhat, "并发数限制").sendToTarget();
                }
            } else if (url == "https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface") { //清除
                ClearjsonDeco clearjsonDeco = gson.fromJson(str, ClearjsonDeco.class);
                try {
                    if ((clearjsonDeco.getError_message().getBytes().length) > 0)
                        handler.obtainMessage(ClearMSGwhat, "清除失败:" + clearjsonDeco.getError_message().getBytes()).sendToTarget();
                } catch (Exception e) {
                    handler.obtainMessage(ClearMSGwhat, "清除成功").sendToTarget();
                }
            } else {
                handler.obtainMessage(GainkeyMSGwhat, "传入的URL错误").sendToTarget();
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

