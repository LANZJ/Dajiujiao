package com.zjyeshi.dajiujiao.buyer.activity.store;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xuan.bigapple.lib.utils.StringUtils;
import com.xuan.bigappleui.lib.view.photoview.BUPhotoViewAttacher;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageActivity;
import com.xuan.bigappleui.lib.view.photoview.app.handler.BUViewImageBaseHandler;
import com.xuan.bigappleui.lib.view.photoview.app.viewholder.WraperFragmentView;
import com.zjyeshi.dajiujiao.buyer.entity.good.AllGoodInfo;
import com.zjyeshi.dajiujiao.buyer.receiver.GoodsInfoReveiver;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wuhk on 2016/12/14.
 */
public class ShowGoodsMultiActivity extends BUViewImageActivity {
    public static final String LOADTYPE_CIRCLE = "loadtype.circle";

    @Override
    protected void onInitViewImageHandler(HashMap<String, BUViewImageBaseHandler> handlerMap) {
        addViewImageHandler(new MyViewImageHandler());
    }

    class MyViewImageHandler extends BUViewImageBaseHandler {
        @Override
        public String getLoadType() {
            return LOADTYPE_CIRCLE;
        }

        @Override
        public void onHandler(final String url, final WraperFragmentView wraperFragmentView, Activity activity, final Object[] datas) {
            String[] params = StringUtils.split(url , ",");
            final String showUrl = params[0];
            final String position = params[1];

            Configuration config = getResources().getConfiguration();
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //切换为竖屏
                wraperFragmentView.photoView.setScaleType(null);
            } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //切换为横屏
                wraperFragmentView.photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            //点击取消
            wraperFragmentView.photoView
                    .setOnPhotoTapListener(new BUPhotoViewAttacher.OnPhotoTapListener() {
                        @Override
                        public void onPhotoTap(View view, float x, float y) {
                            String jsonStr  = (String) datas[0];
                            List<AllGoodInfo> dataList = JSONArray.parseArray(jsonStr , AllGoodInfo.class);
                            AllGoodInfo allGoodInfo = dataList.get(Integer.parseInt(position));

                            GoodsInfoReveiver.notifyReceiver(JSON.toJSONString(allGoodInfo));

                            finish();
                        }
                    });

            GlideImageUtil.loadBigImageViewTarget(wraperFragmentView.photoView , showUrl);
        }

    }
}
