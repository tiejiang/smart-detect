package com.steven.Smartglass.Baidutranslate;


import java.util.List;

public class TransJsonDeco {

    /**
     * from : en
     * to : zh
     * trans_result : [{"src":"laptop","dst":"笔记本电脑"},{"src":"computer","dst":"计算机"},{"src":"keyboard","dst":"键盘"},{"src":"technology","dst":"技术"},{"src":"business","dst":"商业"},{"src":"no person","dst":"查无此人"},{"src":"electronics","dst":"电子学"},{"src":"internet","dst":"互联网"},{"src":"wireless","dst":"无线的"},{"src":"office","dst":"办公室"},{"src":"equipment","dst":"设备"},{"src":"machinery","dst":"机械"},{"src":"industry","dst":"行业"},{"src":"data","dst":"数据"},{"src":"modern","dst":"现代的"},{"src":"one","dst":"一"},{"src":"portable","dst":"便携式的"},{"src":"education","dst":"教育"},{"src":"work","dst":"工作"},{"src":"connection","dst":"连接"}]
     */

    private String from;
    private String to;
    private List<TransResultBean> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TransResultBean> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TransResultBean> trans_result) {
        this.trans_result = trans_result;
    }

    public static class TransResultBean {
        /**
         * src : laptop
         * dst : 笔记本电脑
         */

        private String src;
        private String dst;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getDst() {
            return dst;
        }

        public void setDst(String dst) {
            this.dst = dst;
        }
    }
}
