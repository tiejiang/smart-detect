package com.steven.Smartglass.BT.bluetoothUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */

public class TransmitBean implements Serializable {

    private String msg = "";

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

}
