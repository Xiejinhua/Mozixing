package com.gz.mozixing.network.model;

import com.google.gson.annotations.SerializedName;
import com.gz.mozixing.Constant;
import com.gz.mozixing.network.APIManagerUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.List;
import java.util.Map;

/**
 * @author Alex
 * @since 19/1/30
 */

public class ChildrenInfoModel extends Response {


    private static final String url = Constant.API_Target_url + "childrenUser/childrenInfo";
    /**
     * code : 1
     * msg : Success
     * data : {"result":[{"time":"6","temperature":"37.5"}],"photo":"图片地址","name":"黄筱荷","temperature":"37.5","state":"1"}
     */

    private String code;
    private String msg;
    private DataBean data;

    //请求数据
    public static void getResponse(Map<String, String> map, NetWorkCallback<ChildrenInfoModel> netWorkCallback) {

        APIManagerUtil.getInstance().startMainPostResponse(url, map, netWorkCallback, ChildrenInfoModel.class);

    }

    //取消请求
    public static void cancelResponse() {
        RequestCall call = OkHttpUtils.get().url(url).build();
        call.cancel();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * result : [{"time":"6","temperature":"37.5"}]
         * photo : 图片地址
         * name : 黄筱荷
         * temperature : 37.5
         * state : 1
         */

        private String photo;
        private String name;
        private String temperature;
        private int state;
        private String time;

        @SerializedName("result")
        private List<ResultBean> resultX;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public List<ResultBean> getResultX() {
            return resultX;
        }

        public void setResultX(List<ResultBean> resultX) {
            this.resultX = resultX;
        }

        public static class ResultBean {
            /**
             * time : 6
             * temperature : 37.5
             */

            private String time;
            private String temperature;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }
        }
    }
}
