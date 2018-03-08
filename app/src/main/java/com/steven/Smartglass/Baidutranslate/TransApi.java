package com.steven.Smartglass.Baidutranslate;

import java.util.HashMap;
import java.util.Map;

public class TransApi {
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private String appid = "20170417000045038";
    private String securityKey = "GughaFslOoAE6stL8DNz";

    public TransApi() {
    }

    public String getTransResult(String query, String from, String to){
        Map params = this.buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private Map<String, String> buildParams(String query, String from, String to) {
        HashMap params = new HashMap();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", appid);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = null; // 加密前的原文
        src = appid + query + salt + securityKey;
        params.put("sign", MD5.md5(src));

        return params;
    }
}