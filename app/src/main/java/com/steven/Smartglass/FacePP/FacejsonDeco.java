package com.steven.Smartglass.FacePP;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class FacejsonDeco {

    /**
     * image_id : YlUn7gz/k6ajsbDrSCJjjw==
     * request_id : 1492095964,e7787f2e-5b49-40ae-ab32-c3f627f02f28
     * time_used : 325
     * faces : [{"attributes":{"gender":{"value":"Male"},"age":{"value":28},"glass":{"value":"None"},"headpose":{"yaw_angle":4.54550528175019,"pitch_angle":2.215107357106983,"roll_angle":-1.6589646000237046},"blur":{"blurness":{"threshold":50,"value":98.696},"motionblur":{"threshold":50,"value":98.696},"gaussianblur":{"threshold":50,"value":98.696}},"smile":{"threshold":30.1,"value":9.326},"ethnicity":{"value":"Asian"}},"face_rectangle":{"width":208,"top":370,"left":225,"height":208},"face_token":"2966301cf523e8199e794c41e43c9719"}]
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<FacesBean> faces;

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

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public static class FacesBean {
        /**
         * attributes : {"gender":{"value":"Male"},"age":{"value":28},"glass":{"value":"None"},"headpose":{"yaw_angle":4.54550528175019,"pitch_angle":2.215107357106983,"roll_angle":-1.6589646000237046},"blur":{"blurness":{"threshold":50,"value":98.696},"motionblur":{"threshold":50,"value":98.696},"gaussianblur":{"threshold":50,"value":98.696}},"smile":{"threshold":30.1,"value":9.326},"ethnicity":{"value":"Asian"}}
         * face_rectangle : {"width":208,"top":370,"left":225,"height":208}
         * face_token : 2966301cf523e8199e794c41e43c9719
         */

        private AttributesBean attributes;
        private FaceRectangleBean face_rectangle;
        private String face_token;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public FaceRectangleBean getFace_rectangle() {
            return face_rectangle;
        }

        public void setFace_rectangle(FaceRectangleBean face_rectangle) {
            this.face_rectangle = face_rectangle;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class AttributesBean {
            /**
             * gender : {"value":"Male"}
             * age : {"value":28}
             * glass : {"value":"None"}
             * headpose : {"yaw_angle":4.54550528175019,"pitch_angle":2.215107357106983,"roll_angle":-1.6589646000237046}
             * blur : {"blurness":{"threshold":50,"value":98.696},"motionblur":{"threshold":50,"value":98.696},"gaussianblur":{"threshold":50,"value":98.696}}
             * smile : {"threshold":30.1,"value":9.326}
             * ethnicity : {"value":"Asian"}
             */

            private GenderBean gender;
            private AgeBean age;
            private GlassBean glass;
            private HeadposeBean headpose;
            private BlurBean blur;
            private SmileBean smile;
            private EthnicityBean ethnicity;

            public GenderBean getGender() {
                return gender;
            }

            public void setGender(GenderBean gender) {
                this.gender = gender;
            }

            public AgeBean getAge() {
                return age;
            }

            public void setAge(AgeBean age) {
                this.age = age;
            }

            public GlassBean getGlass() {
                return glass;
            }

            public void setGlass(GlassBean glass) {
                this.glass = glass;
            }

            public HeadposeBean getHeadpose() {
                return headpose;
            }

            public void setHeadpose(HeadposeBean headpose) {
                this.headpose = headpose;
            }

            public BlurBean getBlur() {
                return blur;
            }

            public void setBlur(BlurBean blur) {
                this.blur = blur;
            }

            public SmileBean getSmile() {
                return smile;
            }

            public void setSmile(SmileBean smile) {
                this.smile = smile;
            }

            public EthnicityBean getEthnicity() {
                return ethnicity;
            }

            public void setEthnicity(EthnicityBean ethnicity) {
                this.ethnicity = ethnicity;
            }

            public static class GenderBean {
                /**
                 * value : Male
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class AgeBean {
                /**
                 * value : 28
                 */

                private int value;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class GlassBean {
                /**
                 * value : None
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class HeadposeBean {
                /**
                 * yaw_angle : 4.54550528175019
                 * pitch_angle : 2.215107357106983
                 * roll_angle : -1.6589646000237046
                 */

                private double yaw_angle;
                private double pitch_angle;
                private double roll_angle;

                public double getYaw_angle() {
                    return yaw_angle;
                }

                public void setYaw_angle(double yaw_angle) {
                    this.yaw_angle = yaw_angle;
                }

                public double getPitch_angle() {
                    return pitch_angle;
                }

                public void setPitch_angle(double pitch_angle) {
                    this.pitch_angle = pitch_angle;
                }

                public double getRoll_angle() {
                    return roll_angle;
                }

                public void setRoll_angle(double roll_angle) {
                    this.roll_angle = roll_angle;
                }
            }

            public static class BlurBean {
                /**
                 * blurness : {"threshold":50,"value":98.696}
                 * motionblur : {"threshold":50,"value":98.696}
                 * gaussianblur : {"threshold":50,"value":98.696}
                 */

                private BlurnessBean blurness;
                private MotionblurBean motionblur;
                private GaussianblurBean gaussianblur;

                public BlurnessBean getBlurness() {
                    return blurness;
                }

                public void setBlurness(BlurnessBean blurness) {
                    this.blurness = blurness;
                }

                public MotionblurBean getMotionblur() {
                    return motionblur;
                }

                public void setMotionblur(MotionblurBean motionblur) {
                    this.motionblur = motionblur;
                }

                public GaussianblurBean getGaussianblur() {
                    return gaussianblur;
                }

                public void setGaussianblur(GaussianblurBean gaussianblur) {
                    this.gaussianblur = gaussianblur;
                }

                public static class BlurnessBean {
                    /**
                     * threshold : 50.0
                     * value : 98.696
                     */

                    private double threshold;
                    private double value;

                    public double getThreshold() {
                        return threshold;
                    }

                    public void setThreshold(double threshold) {
                        this.threshold = threshold;
                    }

                    public double getValue() {
                        return value;
                    }

                    public void setValue(double value) {
                        this.value = value;
                    }
                }

                public static class MotionblurBean {
                    /**
                     * threshold : 50.0
                     * value : 98.696
                     */

                    private double threshold;
                    private double value;

                    public double getThreshold() {
                        return threshold;
                    }

                    public void setThreshold(double threshold) {
                        this.threshold = threshold;
                    }

                    public double getValue() {
                        return value;
                    }

                    public void setValue(double value) {
                        this.value = value;
                    }
                }

                public static class GaussianblurBean {
                    /**
                     * threshold : 50.0
                     * value : 98.696
                     */

                    private double threshold;
                    private double value;

                    public double getThreshold() {
                        return threshold;
                    }

                    public void setThreshold(double threshold) {
                        this.threshold = threshold;
                    }

                    public double getValue() {
                        return value;
                    }

                    public void setValue(double value) {
                        this.value = value;
                    }
                }
            }

            public static class SmileBean {
                /**
                 * threshold : 30.1
                 * value : 9.326
                 */

                private double threshold;
                private double value;

                public double getThreshold() {
                    return threshold;
                }

                public void setThreshold(double threshold) {
                    this.threshold = threshold;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }

            public static class EthnicityBean {
                /**
                 * value : Asian
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }

        public static class FaceRectangleBean {
            /**
             * width : 208
             * top : 370
             * left : 225
             * height : 208
             */

            private int width;
            private int top;
            private int left;
            private int height;

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

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
