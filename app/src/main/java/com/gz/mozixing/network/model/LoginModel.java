package com.gz.mozixing.network.model;

import com.google.gson.annotations.SerializedName;
import com.gz.mozixing.Constant;
import com.gz.mozixing.network.APIManagerUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Map;

/**
 * 登录
 *
 * @author Alex
 * @since 19/1/30
 */

public class LoginModel extends Response {


    private static final String url = Constant.API_Target_url + "parentUser/login";
    /**
     * code : 1
     * msg : Success
     * data : {"result":{"parentId":"1","name":"黄才健","sex":"0","phone":"13652329425","birthday":"1991-06-21","qq":"1242330120"},"token":"5ca1b3c6d9"}
     */

    private String code;
    private String msg;
    private DataBean data;

    //请求数据
    public static void getResponse(Map<String, String> map, NetWorkCallback<LoginModel> netWorkCallback) {

        APIManagerUtil.getInstance().startPostResponse(url, map, netWorkCallback, LoginModel.class);

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
         * result : {"parentId":"1","name":"黄才健","sex":"0","phone":"13652329425","birthday":"1991-06-21","qq":"1242330120"}
         * token : 5ca1b3c6d9
         */

        @SerializedName("result")
        private ResultBean resultX;
        private String token;

        public ResultBean getResultX() {
            return resultX;
        }

        public void setResultX(ResultBean resultX) {
            this.resultX = resultX;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class ResultBean {
            /**
             * parentId : 1
             * name : 黄才健
             * sex : 0
             * phone : 13652329425
             * birthday : 1991-06-21
             * qq : 1242330120
             */

            private String parentId;
            private String name;
            private String sex;
            private String phone;
            private String birthday;
            private String qq;

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getQq() {
                return qq;
            }

            public void setQq(String qq) {
                this.qq = qq;
            }
        }
    }
}
