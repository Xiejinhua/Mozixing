package com.gz.mozixing.network.model;

import com.gz.mozixing.Constant;
import com.gz.mozixing.network.APIManagerUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Map;

/**
 * 修改儿童头像的接口
 *
 * @author Alex
 * @since 19/1/30
 */

public class UpdateChildrenPhotoModel extends Response {


    private static final String url = Constant.API_Target_url + "childrenUser/updateChildrenPhoto";
    /**
     * code : 1
     * msg : Success
     * data : {}
     */

    private String code;
    private String msg;
    private DataBean data;

    //请求数据
    public static void getResponse(Map<String, String> map, NetWorkCallback<UpdateChildrenPhotoModel> netWorkCallback) {

        APIManagerUtil.getInstance().startMainPostResponse(url, map, netWorkCallback, UpdateChildrenPhotoModel.class);

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
    }
}
