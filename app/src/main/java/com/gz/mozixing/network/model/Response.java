package com.gz.mozixing.network.model;

/**
 * @author Alex
 * @since 19/1/30
 * 请求响应的基类，这里封装了所有请求都必须会响应的参数
 */

public class Response  {
    /**
     * 请求结果
     */
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
