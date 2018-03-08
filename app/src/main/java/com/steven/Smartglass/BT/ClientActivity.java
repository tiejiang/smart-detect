package com.steven.Smartglass.BT;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.steven.Smartglass.BT.bluetoothUtil.BluetoothClientService;
import com.steven.Smartglass.BT.bluetoothUtil.BluetoothTools;
import com.steven.Smartglass.BT.bluetoothUtil.TransmitBean;
import com.steven.Smartglass.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ClientActivity extends Activity {
    private TextView serversText;
    private Button facedetect;
    private Button textrec;
    private Button scenerec;
    private Button stoptts;
    private List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

    //广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothTools.ACTION_NOT_FOUND_SERVER.equals(action)) {
                //未发现设备
                serversText.append("not found device\r\n");
            } else if (BluetoothTools.ACTION_FOUND_DEVICE.equals(action)) {
                //获取到设备对象
                BluetoothDevice device = (BluetoothDevice) intent.getExtras().get(BluetoothTools.DEVICE);
                serversText.append(device.getName() + "," + device.getAddress() + "\r\n");
                if (device.getAddress().equals("00:00:00:00:5A:AD")) {
                    deviceList.add(device);
                    //serversText.append(device.getName() + "," + device.getAddress() + "\r\n");
                    //停止搜索
                    BluetoothTools.stopDiscovery();
                    //连接设备
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent selectDeviceIntent = new Intent(BluetoothTools.ACTION_SELECTED_DEVICE);
                            selectDeviceIntent.putExtra(BluetoothTools.DEVICE, deviceList.get(0));
                            sendBroadcast(selectDeviceIntent);
                            serversText.setText("正在连接:" + deviceList.get(0).getName());
                        }
                    }, 1000);
                }
            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                //连接成功
                serversText.setText("连接成功");

            } else if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
                //接收数据
                TransmitBean data = (TransmitBean) intent.getExtras().getSerializable(BluetoothTools.DATA);
                String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
            }
        }
    };

    @Override
    protected void onStart() {
        //清空设备列表
        deviceList.clear();
        //开启后台service
        Intent startService = new Intent(ClientActivity.this, BluetoothClientService.class);
        startService(startService);
        //注册BoradcasrReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_NOT_FOUND_SERVER);
        intentFilter.addAction(BluetoothTools.ACTION_FOUND_DEVICE);
        intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        registerReceiver(broadcastReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);

        serversText = (TextView) findViewById(R.id.clientServersText);
        facedetect = (Button) findViewById(R.id.facedetect);
        textrec = (Button) findViewById(R.id.textrec);
        scenerec = (Button) findViewById(R.id.scenerec);
        stoptts = (Button) findViewById(R.id.stoptts);

        facedetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg("facedetect");
            }
        });
        textrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg("textrec");
            }
        });
        scenerec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg("scenerec");
            }
        });
        stoptts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg("stoptts");
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //开始搜索
                Intent startSearchIntent = new Intent(BluetoothTools.ACTION_START_DISCOVERY);
                sendBroadcast(startSearchIntent);
            }
        }, 1000);
    }


    public void sendmsg(String datemsg){
        TransmitBean data = new TransmitBean();
        data.setMsg(datemsg);
        Intent sendDataIntent = new Intent(BluetoothTools.ACTION_DATA_TO_SERVICE);
        sendDataIntent.putExtra(BluetoothTools.DATA, data);
        sendBroadcast(sendDataIntent);
    }

    @Override
    protected void onStop() {
        //关闭后台Service
        Intent startService = new Intent(BluetoothTools.ACTION_STOP_SERVICE);
        sendBroadcast(startService);
        unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
