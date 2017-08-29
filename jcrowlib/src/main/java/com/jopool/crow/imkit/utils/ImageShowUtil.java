package com.jopool.crow.imkit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.CWChat;
import com.jopool.crow.R;
import com.jopool.crow.imkit.view.CWCircleImageView;
import com.jopool.crow.imlib.utils.CWValidator;
import com.jopool.crow.imlib.utils.bitmap.BPBitmapLoader;
import com.jopool.crow.imlib.utils.bitmap.BitmapDisplayConfig;

/**
 * 图片显示工具类
 *
 * Created by xuan on 16/9/19.
 */
public abstract class ImageShowUtil {
    private static int DEFAULT_HEADICON = R.drawable.cw_chat_default_user;

    /**
     * 显示头像
     *
     * @param imageView
     * @param url
     */
    public static void showHeadIcon(ImageView imageView, String url){
        imageView.setVisibility(View.VISIBLE);
        //
        if(imageView instanceof CWCircleImageView){
            ((CWCircleImageView)imageView).setIsWork(CWChat.getInstance().getCustomUIDelegate().isHeadIconCircleCorner());
        }
        //
        if(CWValidator.isEmpty(url)){
            imageView.setImageResource(DEFAULT_HEADICON);
        }else{
            Bitmap defaultBitmap = BitmapFactory.decodeResource(CWChat.getApplication().getResources(), DEFAULT_HEADICON);
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            config.setLoadFailedBitmap(defaultBitmap);
            config.setLoadingBitmap(defaultBitmap);
            BPBitmapLoader.getInstance().display(imageView, url, config);
        }
    }

}
