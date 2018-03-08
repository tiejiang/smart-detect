package com.steven.Smartglass.FacePP;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class TextjsonDeco {

    /**
     * image_id : KOUY9bqza9jQgM3Hh8qsdA==
     * result : [{"child-objects":[{"child-objects":[],"position":[{"y":456,"x":134},{"y":466,"x":154},{"y":487,"x":144},{"y":476,"x":123}],"type":"character","value":"C"}],"position":[],"type":"textline","value":"C"},{"child-objects":[{"child-objects":[],"position":[{"y":412,"x":255},{"y":415,"x":275},{"y":434,"x":273},{"y":432,"x":253}],"type":"character","value":"G"}],"position":[],"type":"textline","value":"G"},{"child-objects":[{"child-objects":[],"position":[{"y":411,"x":193},{"y":412,"x":201},{"y":420,"x":200},{"y":419,"x":192}],"type":"character","value":"-"}],"position":[],"type":"textline","value":"-"},{"child-objects":[{"child-objects":[],"position":[{"y":446,"x":470},{"y":443,"x":483},{"y":456,"x":486},{"y":459,"x":473}],"type":"character","value":"-"}],"position":[],"type":"textline","value":"-"},{"child-objects":[{"child-objects":[],"position":[{"y":457,"x":68},{"y":463,"x":75},{"y":470,"x":68},{"y":463,"x":62}],"type":"character","value":"-"}],"position":[],"type":"textline","value":"-"}]
     * request_id : 1492076622,4cacd307-fc24-49f6-a765-ede0c749028a
     * time_used : 4991
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<ResultBean> result;

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

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * child-objects : [{"child-objects":[],"position":[{"y":456,"x":134},{"y":466,"x":154},{"y":487,"x":144},{"y":476,"x":123}],"type":"character","value":"C"}]
         * position : []
         * type : textline
         * value : C
         */

        private String type;
        private String value;
        @SerializedName("child-objects")
        private List<ChildobjectsBean> childobjects;
        private List<?> position;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public List<ChildobjectsBean> getChildobjects() {
            return childobjects;
        }

        public void setChildobjects(List<ChildobjectsBean> childobjects) {
            this.childobjects = childobjects;
        }

        public List<?> getPosition() {
            return position;
        }

        public void setPosition(List<?> position) {
            this.position = position;
        }

        public static class ChildobjectsBean {
            /**
             * child-objects : []
             * position : [{"y":456,"x":134},{"y":466,"x":154},{"y":487,"x":144},{"y":476,"x":123}]
             * type : character
             * value : C
             */

            private String type;
            private String value;
            @SerializedName("child-objects")
            private List<?> childobjects;
            private List<PositionBean> position;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public List<?> getChildobjects() {
                return childobjects;
            }

            public void setChildobjects(List<?> childobjects) {
                this.childobjects = childobjects;
            }

            public List<PositionBean> getPosition() {
                return position;
            }

            public void setPosition(List<PositionBean> position) {
                this.position = position;
            }

            public static class PositionBean {
                /**
                 * y : 456
                 * x : 134
                 */

                private int y;
                private int x;

                public int getY() {
                    return y;
                }

                public void setY(int y) {
                    this.y = y;
                }

                public int getX() {
                    return x;
                }

                public void setX(int x) {
                    this.x = x;
                }
            }
        }
    }
}
