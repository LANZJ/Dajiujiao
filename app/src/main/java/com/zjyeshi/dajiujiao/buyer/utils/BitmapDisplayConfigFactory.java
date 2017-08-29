package com.zjyeshi.dajiujiao.buyer.utils;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.R;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;

/**
 * 为了更流畅的加载图片,config配置很重要
 *
 * Created by xuan on 15/11/25.
 */
public class BitmapDisplayConfigFactory {
    public static BitmapDisplayConfigFactory instance;

    public BitmapDisplayConfig circleImageConfig;

    private BitmapDisplayConfigFactory(){
        circleImageConfig = new BitmapDisplayConfig();
        circleImageConfig.setLoadFailedBitmap(App.defaultBitmapGray);
        circleImageConfig.setLoadingBitmap(App.defaultBitmapGray);
        circleImageConfig.setBitmapMaxHeight((int) App.instance.getResources().getDimension(R.dimen.circle_multi_image_max_size));
        circleImageConfig.setBitmapMaxWidth((int) App.instance.getResources().getDimension(R.dimen.circle_multi_image_max_size));
    }

    public static BitmapDisplayConfigFactory getInstance(){
        return instance;
    }

    public static void init(){
        instance = new BitmapDisplayConfigFactory();
    }

    public BitmapDisplayConfig getCircleImageConfig(){
        return circleImageConfig;
    }


}
