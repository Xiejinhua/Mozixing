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

public class ChildrenModel extends Response {


    private static final String url = Constant.API_Target_url + "test";
    /**
     * code : 1
     * msg : Success
     * data : {"result":[{"childrenId":"1"},{"childrenId":"2"},{"childrenId":"3"}]}
     */

    private String code;
    private String msg;
    private DataBean data;

    //请求数据
    public static void getResponse(Map<String, String> map, NetWorkCallback netWorkCallback) {

        APIManagerUtil.getInstance().startPostResponse(url, map, netWorkCallback, ChildrenModel.class);

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
        @SerializedName("result")
        private List<ResultBean> resultX;

        public List<ResultBean> getResultX() {
            return resultX;
        }

        public void setResultX(List<ResultBean> resultX) {
            this.resultX = resultX;
        }

        public static class ResultBean {
            /**
             * childrenId : 1
             */

            private String childrenId;

            public String getChildrenId() {
                return childrenId;
            }

            public void setChildrenId(String childrenId) {
                this.childrenId = childrenId;
            }
        }
    }
}
