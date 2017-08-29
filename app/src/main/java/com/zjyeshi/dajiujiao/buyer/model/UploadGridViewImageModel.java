package com.zjyeshi.dajiujiao.buyer.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;

import com.xuan.bigapple.lib.utils.BitmapUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.uuid.UUIDUtils;
import com.xuan.bigappleui.lib.album.BUAlbum;
import com.xuan.bigappleui.lib.album.entity.ImageItem;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.uploadMultImage.GridViewImageAdapter;
import com.zjyeshi.dajiujiao.buyer.widgets.uploadMultImage.GridViewImageWidget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/8/23.
 */
public class UploadGridViewImageModel {

    private Handler handler = new Handler();
    private List<String> dataList = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    /**去相机
     *
     */
    public static final int REQUEST_CODE_OF_CAMERA = 1;

    /**去相册
     *
     */
    public static final int REQUEST_CODE_OF_ALBUM = 2;

    protected Activity mActivity;

    public void init(View view , Context context , GridViewImageAdapter adapter){
        mActivity = (Activity) context;
        this.adapter = adapter;
        DGSingleSelectDialog d = new DGSingleSelectDialog.Builder(mActivity).setItemTextAndOnClickListener(new String[]{"从手机相册选择", "拍照"}, new View.OnClickListener[]{new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册中选择
                BUAlbum.gotoAlbumForMulti(mActivity, getRestNum(), REQUEST_CODE_OF_ALBUM);
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

    /**
     * 去拍照
     */
    public void gotoCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        FileUtil.checkParentFile(Constants.SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA)));
        mActivity.startActivityForResult(intent, REQUEST_CODE_OF_CAMERA);
    }

    /**
     * 处理返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data , List<String> imageList) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // 从相册获取照片
        if (REQUEST_CODE_OF_ALBUM == requestCode) {
            //图库选择处理
            List<ImageItem> selectedList = BUAlbum.getSelListAndClear();
            dealImageToEditForAlbum(selectedList , imageList);
            return;
        }

        // 从拍照获取图片
        if (REQUEST_CODE_OF_CAMERA == requestCode) {
            dealImageToEditForCamera(imageList);
            return;
        }
    }

    /**
     * 获取剩下可以选图片数量
     * @return
     */
    private int getRestNum() {
        return (GridViewImageWidget.MAX_IMAGE_SIZE - dataList.size());
    }

    /**
     * 把选择的图片压缩放倒编辑文件夹下
     */
    private void dealImageToEditForAlbum(final List<ImageItem> selectedList , final List<String> imageList) {
        if (Validators.isEmpty(selectedList)) {
            return;
        }

        final DGProgressDialog d = new DGProgressDialog(mActivity);
        d.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ImageItem imageItem : selectedList) {
                    dealImageToEdit(imageItem.imagePath , imageList);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d.dismiss();
                    }
                });
            }
        }).start();
    }

    private void dealImageToEdit(String filePathName , List<String> imageList){
        int degree = BitmapUtils.getBitmapDegree(filePathName);//旋转角度
        String editFileName = Constants.SDCARD_DJJBUYER_CIRCLE_EDIT + UUIDUtils.createId();
        FileUtil.checkParentFile(editFileName);//编辑时临时存放图片
        Bitmap b = BitmapUtils.changeOppositeSizeMayDegree(filePathName, editFileName, Constants.IMAGE_LIMIT_SIZE, Constants.IMAGE_LIMIT_SIZE, degree);
        if(null != b){
            dataList.add(editFileName);
            imageList.clear();
            imageList.addAll(dataList);
            refreshData();
        }
    }

    /**
     * 把拍照的图片放大编辑文件夹下
     */
    private void dealImageToEditForCamera(List<String> imageList) {
        dealImageToEdit(Constants.SDCARD_DJJBUYER_CIRCLE_TEMP_CAMREA , imageList);
    }

    public void refreshData(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

}
