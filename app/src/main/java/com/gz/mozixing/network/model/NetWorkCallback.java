package com.gz.mozixing.network.model;

/**
 * @author Alex
 * @since 19/1/30
 * 网络请求响应的回调接口
 */

public interface NetWorkCallback<T extends Object> {
    void onResponse(T response);

    void onFailure(String message);
}
