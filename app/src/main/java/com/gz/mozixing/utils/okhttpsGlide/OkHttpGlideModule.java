package com.gz.mozixing.utils.okhttpsGlide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.InputStream;

/**
 * @author alex
 * @since 2017/9/7
 */

public class OkHttpGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //注意：new HTTPSUtils(context).getInstance()为已经通过认证的okhttpclient
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkHttpUtils.getInstance().getOkHttpClient()));
    }
}