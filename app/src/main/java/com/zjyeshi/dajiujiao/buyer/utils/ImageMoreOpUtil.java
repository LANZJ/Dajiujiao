package com.zjyeshi.dajiujiao.buyer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Looper;
import android.view.View;

import com.xuan.bigapple.lib.http.BPHttpUtils;
import com.xuan.bigapple.lib.http.callback.BPHttpDownloadListener;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.activity.my.MyContactActivity;

/**
 * Created by wuhk on 2016/9/8.
 */
public abstract class ImageMoreOpUtil {

    public static void creatMoreImageOp(final Context context, final String url, final String fromId) {
        String[] itemStr = {"发送给朋友", "保存到手机", "收藏"};
        View.OnClickListener[] ls = {new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送给朋友
                Intent intent = new Intent();
                intent.setClass(context, MyContactActivity.class);
                ((Activity) context).startActivityForResult(intent, MyContactActivity.SELECTMEMBER);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存到手机
                downLoadImage(url, Constants.SDCARD_DJJBUYER_CIRCLE + "save/" + UUIDUtils.createId() + ".jpg"
                        , new ImageDownloadSuccess() {
                            @Override
                            public void downloadSuccess(String filePath) {
                                MediaScannerConnection.scanFile(context, new String[]{filePath}, null, null);
                                ToastUtil.toast("图片已保存到系统相册");
                            }
                        });

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收藏
                CircleUtil.doCollect(context, CircleUtil.getCollectWithPic(fromId, url));
            }
        }};
        DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(itemStr, ls).create();
        dialog.show();
    }

    /**下载图片
     *
     * @param url
     * @param filePath
     * @param imageDownloadSuccess
     */
    public static void downLoadImage(final String url, final String filePath, final ImageDownloadSuccess imageDownloadSuccess) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    BPHttpUtils.postDowload(url, null, filePath, new BPHttpDownloadListener() {
                        @Override
                        public void callBack(long count, long current, boolean isFinish) {
                            if (isFinish) {
                                imageDownloadSuccess.downloadSuccess(filePath);
                            }
                        }
                    });
                } catch (Exception e) {
                    LogUtils.e(e.getMessage(), e);
                }
                Looper.loop();
            }
        }).start();
    }

    public interface ImageDownloadSuccess {
        void downloadSuccess(String filePath);
    }
}
