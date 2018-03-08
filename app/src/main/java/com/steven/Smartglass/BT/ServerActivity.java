package com.steven.Smartglass.BT;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.steven.Smartglass.BT.bluetoothUtil.BluetoothServerService;
import com.steven.Smartglass.BT.bluetoothUtil.BluetoothTools;
import com.steven.Smartglass.BT.bluetoothUtil.TransmitBean;
import com.steven.Smartglass.R;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ServerActivity extends Activity {
    private TextView serverStateTextView;
    private EditText msgEditText;

    //广播接收器
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothTools.ACTION_DATA_TO_GAME.equals(action)) {
                //接收数据
                TransmitBean data = (TransmitBean) intent.getExtras().getSerializable(BluetoothTools.DATA);
                //String msg = "from remote " + new Date().toLocaleString() + " :\r\n" + data.getMsg() + "\r\n";
                String msg = data.getMsg() + "\r\n";
                msgEditText.append(msg);

            } else if (BluetoothTools.ACTION_CONNECT_SUCCESS.equals(action)) {
                //连接成功
                serverStateTextView.setText("连接成功");
            }
        }
    };

    @Override
    protected void onStart() {
        //开启后台service
        Intent startService = new Intent(ServerActivity.this, BluetoothServerService.class);
        startService(startService);

        //注册BoradcasrReceiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothTools.ACTION_DATA_TO_GAME);
        intentFilter.addAction(BluetoothTools.ACTION_CONNECT_SUCCESS);
        registerReceiver(broadcastReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server);

        serverStateTextView = (TextView) findViewById(R.id.serverStateText);
        serverStateTextView.setText("正在连接...");

        msgEditText = (EditText) findViewById(R.id.serverEditText);
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
