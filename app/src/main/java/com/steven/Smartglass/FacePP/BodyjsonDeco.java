package com.steven.Smartglass.FacePP;

import java.util.List;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class BodyjsonDeco {

    /**
     * image_id : 7OO7N1dYiJjszvV38oKVpw==
     * request_id : 1491569448,de5a441f-6c6f-4955-896c-37b8bb2d4197
     * time_used : 915
     * humanbodies : [{"attributes":{"gender":{"confidence":56.277,"value":"Female"},"upper_body_cloth_color":"white","lower_body_cloth_color":"white"},"humanbody_rectangle":{"width":456,"top":0,"height":500,"left":0},"confidence":99.905}]
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<HumanbodiesBean> humanbodies;

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

    public List<HumanbodiesBean> getHumanbodies() {
        return humanbodies;
    }

    public void setHumanbodies(List<HumanbodiesBean> humanbodies) {
        this.humanbodies = humanbodies;
    }

    public static class HumanbodiesBean {
        /**
         * attributes : {"gender":{"confidence":56.277,"value":"Female"},"upper_body_cloth_color":"white","lower_body_cloth_color":"white"}
         * humanbody_rectangle : {"width":456,"top":0,"height":500,"left":0}
         * confidence : 99.905
         */

        private AttributesBean attributes;
        private HumanbodyRectangleBean humanbody_rectangle;
        private double confidence;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public HumanbodyRectangleBean getHumanbody_rectangle() {
            return humanbody_rectangle;
        }

        public void setHumanbody_rectangle(HumanbodyRectangleBean humanbody_rectangle) {
            this.humanbody_rectangle = humanbody_rectangle;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public static class AttributesBean {
            /**
             * gender : {"confidence":56.277,"value":"Female"}
             * upper_body_cloth_color : white
             * lower_body_cloth_color : white
             */

            private GenderBean gender;
            private String upper_body_cloth_color;
            private String lower_body_cloth_color;

            public GenderBean getGender() {
                return gender;
            }

            public void setGender(GenderBean gender) {
                this.gender = gender;
            }

            public String getUpper_body_cloth_color() {
                return upper_body_cloth_color;
            }

            public void setUpper_body_cloth_color(String upper_body_cloth_color) {
                this.upper_body_cloth_color = upper_body_cloth_color;
            }

            public String getLower_body_cloth_color() {
                return lower_body_cloth_color;
            }

            public void setLower_body_cloth_color(String lower_body_cloth_color) {
                this.lower_body_cloth_color = lower_body_cloth_color;
            }

            public static class GenderBean {
                /**
                 * confidence : 56.277
                 * value : Female
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

        public static class HumanbodyRectangleBean {
            /**
             * width : 456
             * top : 0
             * height : 500
             * left : 0
             */

            private int width;
            private int top;
            private int height;
            private int left;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }
        }
    }
}
