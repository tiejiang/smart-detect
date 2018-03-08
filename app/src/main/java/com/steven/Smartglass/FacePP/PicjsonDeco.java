package com.steven.Smartglass.FacePP;

import java.util.List;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class PicjsonDeco {

    /**
     * time_used : 1417
     * scenes : [{"confidence":52.724,"value":"Office"}]
     * image_id : +SDbIQi/TsQKi2bbNDTTSQ==
     * objects : [{"confidence":25.159,"value":"Watering can"}]
     * request_id : 1492004052,1ab31f44-be77-472e-abb4-bb4547153f8e
     */

    private int time_used;
    private String image_id;
    private String request_id;
    private List<ScenesBean> scenes;
    private List<ObjectsBean> objects;

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public List<ScenesBean> getScenes() {
        return scenes;
    }

    public void setScenes(List<ScenesBean> scenes) {
        this.scenes = scenes;
    }

    public List<ObjectsBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectsBean> objects) {
        this.objects = objects;
    }

    public static class ScenesBean {
        /**
         * confidence : 52.724
         * value : Office
         */

        private double confidence;
        private String value;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class ObjectsBean {
        /**
         * confidence : 25.159
         * value : Watering can
         */

        private double confidence;
        private String value;

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
