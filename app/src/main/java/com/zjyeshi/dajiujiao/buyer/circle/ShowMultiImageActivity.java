package com.zjyeshi.dajiujiao.buyer.circle;

import android.app.Activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.activity.my.MyContactActivity;
import com.zjyeshi.dajiujiao.buyer.chat.ChatManager;

import com.xuan.bigappleui.lib.view.photoview.BUPhotoViewAttacher;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageActivity;
import com.xuan.bigappleui.lib.view.photoview.app.handler.BUViewImageBaseHandler;
import com.xuan.bigappleui.lib.view.photoview.app.viewholder.WraperFragmentView;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ImageMoreOpUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.CheckUserCallback;

import java.io.File;
import java.util.HashMap;

public class ShowMultiImageActivity extends BUViewImageActivity{
    public static final String LOADTYPE_CIRCLE = "loadtype.circle";
    private String imageUrl;

    @Override
    protected void onInitViewImageHandler(
            HashMap<String, BUViewImageBaseHandler> handlerMap) {
        addViewImageHandler(new MyViewImageHandler());
    }

    class MyViewImageHandler extends BUViewImageBaseHandler {
        @Override
        public String getLoadType() {
            return LOADTYPE_CIRCLE;
        }

        @Override
        public void onHandler(final String url, final WraperFragmentView wraperFragmentView, Activity activity, Object[] datas) {
            imageUrl = url;

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
                            finish();
                        }
                    });
            if (null != datas){
                final String fromId = (String)datas[0];
                wraperFragmentView.photoView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ImageMoreOpUtil.creatMoreImageOp(ShowMultiImageActivity.this , url , fromId);
                        return true;
                    }
                });
            }

            GlideImageUtil.loadBigImageViewTarget(wraperFragmentView.photoView , url);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode){
            return;
        }

        if(requestCode == MyContactActivity.SELECTMEMBER){
            final String id = data.getStringExtra(MyContactActivity.TOMEMBERID);
            ImageMoreOpUtil.downLoadImage(imageUrl, Constants.SDCARD_DJJBUYER_CIRCLE_PIC, new ImageMoreOpUtil.ImageDownloadSuccess() {
                @Override
                public void downloadSuccess(final String filePath) {

                    File file = new File(filePath);
                    if (file.exists()){
                        LogUtil.e("cunzai");
                    }else{
                        LogUtil.e("bucunzai");
                    }
                    ChatManager.getInstance().checkUser(ShowMultiImageActivity.this, id, new CheckUserCallback() {
                        @Override
                        public void checkOk() {
                            CWChat.getInstance().getSendMsgDelegate().sendImageMessage(ShowMultiImageActivity.this ,
                                    id , filePath , CWConversationType.USER);
                        }
                    });
                    ToastUtil.toast("转发成功");
                }
            });
        }
    }

}
