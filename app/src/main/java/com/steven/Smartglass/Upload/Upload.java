package com.steven.Smartglass.Upload;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static com.steven.Smartglass.ResultActivity.FaceppMSGwhat;
import static com.steven.Smartglass.ResultActivity.UploadMSGwhat;


public class Upload extends Thread {

    private static final String TAG = "upload：";
    private static final int TIME_OUT = 100000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private File file;
    private Handler handler;


    public Upload(File file, Handler handler) {
        this.file = file;
        this.handler = handler;
    }

    @Override
    public void run() {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String RequestURL = "http://stevennnn.imwork.net/Testupload/servlet/Uploadservlet2";
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);

            if (file != null) {   //如果文件不为空，构造请求实体，上传文件
                OutputStream outputSteam = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();

                //res  获取服务器响应代码，200代表成功
                int res;
                //Message msg = uploadhandler.obtainMessage();
                String resstr = "";
                try {
                    res = conn.getResponseCode();
                    if (res == 200) {
                        Log.e(TAG, "upload success");
                        resstr = "上传成功，图片可在以下网址查看:\n" + "http://stevennnn.imwork.net/Testupload";
                    } else {
                        Log.e(TAG, "upload failure,response code is:" + res);
                        resstr = "上传失败，错误代码：" + res;
                    }
                } catch (Exception e) {
                    resstr = "上传失败，服务器内部错误";
                }
                //msg.obj = resstr;
                //uploadhandler.sendMessage(msg);
                handler.obtainMessage(UploadMSGwhat, resstr).sendToTarget();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
