package com.gz.mozixing.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gz.mozixing.BuildConfig;
import com.gz.mozixing.Constant;
import com.gz.mozixing.R;


/**
 * @author Alex
 * @since 19/1/30
 */
public class LoadingImageUtil {
    //正常的图
    public static void loading(ImageView image, String url) {
        Glide.with(ActivityUtil.getActivity())
                .load(BuildConfig.request_Url + url)
                .skipMemoryCache(false)
                .into(image);
    }

    //图片加载错误的时候添加占位图
    public static void loadingPlaceholder(ImageView image, String url) {
        Glide.with(ActivityUtil.getActivity())
                .load(BuildConfig.request_Url + url)
                .error(R.mipmap.login_user_icon)
                .skipMemoryCache(false)
                .into(image);
    }

    //裁剪成圆形的图
    public static void loadingCircle(ImageView image, String url) {

        Glide.with(ActivityUtil.getActivity())
                .load(BuildConfig.request_Url + url)
                .bitmapTransform(new CropCircleTransformation(ActivityUtil.getActivity()))
                .placeholder(R.mipmap.login_user_icon)
                .error(R.mipmap.login_user_icon)
                .skipMemoryCache(false)//false是缓存头像
                .into(image);
    }

    //裁剪成圆角的图
    public static void loadingFillet(ImageView image, String url) {

        Glide.with(ActivityUtil.getActivity())
                .load(BuildConfig.request_Url + url)
                .bitmapTransform(new GlideRoundTransform(ActivityUtil.getActivity(), 10))
                .dontAnimate()
                .into(image);
    }
}
