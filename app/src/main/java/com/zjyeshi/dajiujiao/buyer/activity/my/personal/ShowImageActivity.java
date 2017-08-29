package com.zjyeshi.dajiujiao.buyer.activity.my.personal;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.enums.CWConversationType;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.chat.ChatManager;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.CheckUserCallback;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.MyContactActivity;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigappleui.lib.view.photoview.BUPhotoView;
import com.xuan.bigappleui.lib.view.photoview.BUPhotoViewAttacher;
import com.zjyeshi.dajiujiao.buyer.utils.ImageMoreOpUtil;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;

import java.io.File;

/**
 * 大图界面
 * Created by wuhk on 2015/11/25.
 */
public class ShowImageActivity extends BaseActivity {

    @InjectView(R.id.photoView)
    private BUPhotoView photoView;

    public static final String FROME_ID = "from_id";
    private String imageUrl;
    private String fromId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_image);
        imageUrl = getIntent().getStringExtra(PassConstans.IMAGEURL);
        initWidgets();
    }

    private void initWidgets() {

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //切换为竖屏
            photoView.setScaleType(null);
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //切换为横屏
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        fromId = getIntent().getStringExtra(FROME_ID);
        GlideImageUtil.loadBigImageViewTarget(photoView, imageUrl);
        if (!Validators.isEmpty(fromId)) {
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ImageMoreOpUtil.creatMoreImageOp(ShowImageActivity.this, imageUrl, fromId);
                    return true;
                }
            });
        } else {
        }
        // 点击关闭
        photoView.setOnPhotoTapListener(new BUPhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float arg1, float arg2) {
                finish();
            }
        });
    }

    /**
     * 普通的启动
     *
     * @param context
     * @param url
     */
    public static void startActivity(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra(PassConstans.IMAGEURL, url);
        intent.setClass(context, ShowImageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 能够保存，收藏等
     *
     * @param context
     * @param url
     * @param fromId
     */
    public static void startActivity(Context context, String url, String fromId) {
        Intent intent = new Intent();
        intent.putExtra(PassConstans.IMAGEURL, url);
        intent.putExtra(FROME_ID, fromId);
        intent.setClass(context, ShowImageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK != resultCode) {
            return;
        }

        if (requestCode == MyContactActivity.SELECTMEMBER) {
            final String id = data.getStringExtra(MyContactActivity.TOMEMBERID);
            ImageMoreOpUtil.downLoadImage(imageUrl, Constants.SDCARD_DJJBUYER_CIRCLE_PIC, new ImageMoreOpUtil.ImageDownloadSuccess() {
                @Override
                public void downloadSuccess(final String filePath) {

                    File file = new File(filePath);
                    if (file.exists()) {
                        LogUtil.e("cunzai");
                    } else {
                        LogUtil.e("bucunzai");
                    }
                    ;
                    ChatManager.getInstance().checkUser(ShowImageActivity.this, id, new CheckUserCallback() {
                        @Override
                        public void checkOk() {
                            CWChat.getInstance().getSendMsgDelegate().sendImageMessage(ShowImageActivity.this,
                                    id, filePath, CWConversationType.USER);
                        }
                    });
                    ToastUtil.toast("转发成功");
                }
            });
        }
    }
}
