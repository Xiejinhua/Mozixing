package com.gz.mozixing.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gz.mozixing.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences 保存到本地的工具类
 *
 * @author davy
 * @since 2017/8/21
 */
public class SharedPreferencesUtils {

    Context c;
    String preferenceName;
    private static SharedPreferencesUtils sharedPreferencesUtils;

    public static SharedPreferencesUtils getInstance(Context context) {
        sharedPreferencesUtils = new SharedPreferencesUtils(context, Constant.defaultPreferenceName);
        return sharedPreferencesUtils;
    }

    public SharedPreferencesUtils(Context c, String preferenceName) {
        this.c = c;
        this.preferenceName = preferenceName;
    }

    //先传键，再传值
    public void setData(String key, String data) {
        SharedPreferences.Editor editor = c.getSharedPreferences(preferenceName, c.MODE_PRIVATE).edit();
        editor.putString(key, data);
        editor.commit();

    }

    //如果没有传空值出去
    public String getData(String key) {

        SharedPreferences prefs = c.getSharedPreferences(preferenceName, c.MODE_PRIVATE);
        String restoredText = prefs.getString(key, "");
        return restoredText;
    }

    //这个方法是传入key没有返回值时返回你想返回的数据
    public String getData(String key, String value) {

        SharedPreferences prefs = c.getSharedPreferences(preferenceName, c.MODE_PRIVATE);
        String restoredText = prefs.getString(key, value);
        return restoredText;
    }

    /**
     * 保存JsonList
     *
     * @param tag
     * @param datalist
     */
    public <T> void setJsonList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        SharedPreferences.Editor editor = c.getSharedPreferences(preferenceName, c.MODE_PRIVATE).edit();
        editor.putString(tag, strJson);
        editor.commit();
    }

    /**
     * 获取JsonList
     *
     * @param tag
     * @return
     */
    public <T> List<T> getJsonList(String tag) {
        List<T> datalist = new ArrayList<T>();
        SharedPreferences prefs = c.getSharedPreferences(preferenceName, c.MODE_PRIVATE);
        String strJson = prefs.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;
    }


}
