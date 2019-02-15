package com.gz.mozixing.network.request;

import com.gz.mozixing.Constant;
import com.gz.mozixing.network.APIManagerUtil;
import com.gz.mozixing.network.model.NetWorkCallback;
import com.gz.mozixing.network.model.Response;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

/**
 * 退出登录接口
 *
 * @author Alex
 * @since 19/1/30
 */

public class Logout extends Response {

    private static final String url = Constant.API_Target_url + "parentUser/outUser";

    //请求数据
    public static void getResponse(Map<String, String> map, NetWorkCallback netWorkCallback) {

        APIManagerUtil.getInstance().startPostResponse(url, map, netWorkCallback, Logout.class);

    }

    //取消请求
    public static void cancelResponse() {
        RequestCall call = OkHttpUtils.get().url(url).build();
        call.cancel();
    }


}
