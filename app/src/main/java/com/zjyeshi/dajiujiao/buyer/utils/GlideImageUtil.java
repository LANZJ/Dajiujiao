package com.zjyeshi.dajiujiao.buyer.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;
import com.xuan.bigappleui.lib.view.photoview.BUPhotoView;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.circle.CircleImageView;
import com.zjyeshi.dajiujiao.R;

/**
 * Glide图片加载工具类
 * Created by wuhk on 2016/10/20.
 */
public abstract class GlideImageUtil {

    /**
     * Glide设置图片
     *
     * @param imageView
     * @param url
     * @param defaultResId
     */
    public static void glidImage(final ImageView imageView, String url, final int defaultResId) {
        Glide
                .with(App.instance)
                .load(url).asBitmap()
                .centerCrop()
                .placeholder(defaultResId)
                .error(defaultResId)
                .into(imageView);
    }


    /**
     * photoView大图加载
     *
     * @param photoView
     * @param url
     */
    public static void loadBigImageViewTarget(final BUPhotoView photoView, String url) {
        photoView.setImageDrawable(App.instance.getResources().getDrawable(R.drawable.default_img));
        ViewTarget<BUPhotoView, GlideDrawable> viewTarget = new ViewTarget<BUPhotoView, GlideDrawable>(photoView) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                photoView.setImageDrawable(resource.getCurrent());
            }
        };

        Glide
                .with(App.instance) // safer!
                .load(url)
                .placeholder(R.drawable.default_img)
                .into(viewTarget);
    }

    /**
     * 酒友圈单个图片加载
     *
     * @param circleImageView
     * @param url
     */
    public static void loadCircleSingleImageViewTarget(final CircleImageView circleImageView, String url) {
        circleImageView.setImageResource(R.drawable.default_img);
        SimpleTarget<Bitmap> target = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                circleImageView.setImageBitmap(resource);
            }
        };

        Glide
                .with(App.instance) // safer!
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_img)
                .into(target);
    }
}
