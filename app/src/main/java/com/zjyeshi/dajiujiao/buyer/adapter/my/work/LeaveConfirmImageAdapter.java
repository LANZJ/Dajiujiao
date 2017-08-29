package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.view.photoview.app.BUViewImageUtils;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.AskForLeaveActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.work.BxFormActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.circle.ShowMultiImageActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.SquareImageView;
import com.zjyeshi.dajiujiao.R;

import java.io.File;
import java.util.List;

/**
 * Created by wuhk on 2016/6/21.
 */
public class LeaveConfirmImageAdapter extends MBaseAdapter {
    private Context context;
    private List<String> dataList;
    private int pos;
    public boolean isBx;
    private boolean card;

    public LeaveConfirmImageAdapter(Context context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public LeaveConfirmImageAdapter(Context context, List<String> dataList, boolean card) {
        this.context = context;
        this.dataList = dataList;
        this.card = card;
    }

    public LeaveConfirmImageAdapter(Context context, List<String> dataList, int pos) {
        this.context = context;
        this.dataList = dataList;
        this.pos = pos;
        isBx = true;
    }

    @Override
    public int getCount() {
        if (dataList.size() == AskForLeaveActivity.MAX_IMAGE) {
            return AskForLeaveActivity.MAX_IMAGE;
        } else {
            return dataList.size() + 1;
        }
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
//        if (null == view) {
//            view = LayoutInflater.from(context).inflate(R.layout.view_leave_image, null);
//        }
        view = LayoutInflater.from(context).inflate(R.layout.view_leave_image, null);

        SquareImageView imageView = (SquareImageView) view.findViewById(R.id.imageView);

        if (dataList.size() == position) {
            //最后一个+
            imageView.setImageResource(R.drawable.icon_add_pic);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBx) {
                        BxFormActivity.position = pos;
                    }
                    if (card) {
                        //打卡只能拍照上传
                        DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(new String[]{"拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //拍照
                                gotoCamera();
                            }
                        }}).create();
                        d.show();
                    } else {
                        DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(new String[]{"从手机相册选择", "拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //从相册中选择
                                BUAlbum.gotoAlbumForMulti((Activity) context, getRestNum(), AskForLeaveActivity.REQUEST_CODE_ALBUM);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //拍照
                                gotoCamera();
                            }
                        }}).create();
                        d.show();
                    }
                }
            });
        } else {
//            initImageViewDefault(imageView, dataList.get(position));
            GlideImageUtil.glidImage(imageView, dataList.get(position), R.drawable.default_img);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int size = dataList.size();
                    String[] images = (String[]) dataList.toArray(new String[size]);
                    //点击查看大图哦
//                    BUViewImageUtils.gotoViewImageActivityForUrls(getContext() , images , position ,null);
                    BUViewImageUtils.gotoViewImageActivity(context, images, position, ShowMultiImageActivity.LOADTYPE_CIRCLE, null, ShowMultiImageActivity.class);
                }
            });
        }
        return view;
    }

    /**
     * 获取剩下可以选图片数量
     *
     * @return
     */
    private int getRestNum() {
        return (AskForLeaveActivity.MAX_IMAGE - dataList.size());
    }

    /**
     * 去拍照
     */
    public void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        FileUtil.checkParentFile(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.SDCARD_DJJBUYER_WORK_TEMP_CAMREA)));
        ((Activity) context).startActivityForResult(intent, AskForLeaveActivity.REQUEST_CODE_CAMERA);
    }
}
