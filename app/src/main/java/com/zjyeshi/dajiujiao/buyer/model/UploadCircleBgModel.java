package com.zjyeshi.dajiujiao.buyer.model;

import android.graphics.Bitmap;
import android.view.View;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.model.DGUploadHeadModel;
import com.zjyeshi.dajiujiao.buyer.circle.CircleFragment;
import com.zjyeshi.dajiujiao.buyer.circle.receiver.OnlyNotifyReceiver;
import com.zjyeshi.dajiujiao.buyer.circle.task.ChangeCircleBgPicTask;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.my.UpLoadPhotoTask;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by wuhk on 2016/8/15.
 */
public class UploadCircleBgModel extends DGUploadHeadModel {
    private String[] itemStrs;
    private View.OnClickListener[] ls;

    public UploadCircleBgModel(){
        itemStrs = new String[]{"拍照","从手机相册选择"};
        ls = new View.OnClickListener[]{new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //拍照
                gotoCamera();
            }
        },new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //从相册获取
                gotoAlbum();
            }
        }};
    }

    @Override
    protected void extraJudge() {
        CircleFragment.isBgChange = true;
    }

    @Override
    protected void onUploadHead2Server(Bitmap bigBitmap, Bitmap smallBitmap) {
        doUploadImage(getBigHeadFilename());
    }

    @Override
    protected void onShowDialog() {
        DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(mFragment.getActivity()).setItemTextAndOnClickListener(itemStrs, ls).create();
        dialog.show();
    }

    @Override
    protected String onSaveFilePath() {
        return FileUtil.getCirlceBgFilePath();
    }

    /**上传图片,并获取图片路径*/
    public void doUploadImage(String filePathName){
        final DGProgressDialog d = new DGProgressDialog(mFragment.getActivity(), "正在玩命上传中...");
        d.show();
        UpLoadPhotoTask upLoadPhotoTask = new UpLoadPhotoTask(mFragment.getActivity());
        upLoadPhotoTask.setShowProgressDialog(false);
        upLoadPhotoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
            @Override
            public void failCallback(Result<GetUpLoadFileData> result) {
                //图片上传失败
                d.dismiss();
                ToastUtil.toast(result.getMessage());
            }
        });

        upLoadPhotoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
            @Override
            public void successCallback(Result<GetUpLoadFileData> result) {
                doChangeAvator(result.getValue().getPath(), new UploadCircleBgModel.Callback(){
                    @Override
                    public void callback(boolean isSuccess) {
                        d.dismiss();
                    }
                });
            }
        });
        upLoadPhotoTask.execute(getBigHeadFilename());
    }

    //修改头像地址
    public void doChangeAvator(final String url, final UploadCircleBgModel.Callback callback){
        ChangeCircleBgPicTask changeCircleBgPicTask = new ChangeCircleBgPicTask(mFragment.getActivity());
        changeCircleBgPicTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast("头像修改失败:" + result.getMessage());
                callback.callback(false);
            }
        });

        changeCircleBgPicTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                callback.callback(true);
                //改变背景图片
                LoginedUser.getLoginedUser().setCircleBackgroundPic(url);
                LoginedUser.saveToFile();
                OnlyNotifyReceiver.notifyReceiver();
            }
        });

        changeCircleBgPicTask.execute(url);
    }

    private interface Callback{
        void callback(boolean isSuccess);
    }
}
