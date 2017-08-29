package com.zjyeshi.dajiujiao.buyer.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.circle.CircleFragment;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleData;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ChangeAvatorReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.my.ChangePicTask;
import com.zjyeshi.dajiujiao.buyer.task.my.UpLoadPhotoTask;
import com.zjyeshi.dajiujiao.buyer.utils.FileUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.xuan.bigdog.lib.model.DGUploadHeadModel;

import java.util.List;

/**
 * 上传头像
 *
 * Created by xuan on 15/11/25.
 */
public class UploadHeadModel extends DGUploadHeadModel {
    private String[] itemStrs;
    private View.OnClickListener[] ls;

    public UploadHeadModel(){
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
    protected void setIntentExtra(Intent intent) {
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
    }

    @Override
    protected void extraJudge() {
        CircleFragment.isBgChange = false;
    }

    @Override
    protected void onUploadHead2Server(Bitmap bigBitmap, Bitmap smallBitmap) {
        doUploadImage(getBigHeadFilename());
    }

    @Override
    protected void onShowDialog() {
        DGSingleSelectDialog dialog;
        if (mActivity == null){
            dialog = new DGSingleSelectDialog.Builder(mFragment.getActivity()).setItemTextAndOnClickListener(itemStrs, ls).create();
        }else{
            dialog = new DGSingleSelectDialog.Builder(mActivity).setItemTextAndOnClickListener(itemStrs, ls).create();
        }
        dialog.show();
    }

    @Override
    protected String onSaveFilePath() {
        return FileUtil.getLocalAvatorFilePath();
    }

    /**上传图片,并获取图片路径*/
    public void doUploadImage(String filePathName){
        final DGProgressDialog d;
        UpLoadPhotoTask upLoadPhotoTask;
        if (mActivity == null){
            d = new DGProgressDialog(mFragment.getActivity(), "正在玩命上传中...");
            upLoadPhotoTask = new UpLoadPhotoTask(mFragment.getActivity());
        }else{
            d = new DGProgressDialog(mActivity, "正在玩命上传中...");
            upLoadPhotoTask = new UpLoadPhotoTask(mActivity);
        }
        d.show();
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
                doChangeAvator(result.getValue().getPath(), new UploadHeadModel.Callback() {
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
    public void doChangeAvator(final String url, final UploadHeadModel.Callback callback){
        ChangePicTask changePicTask;
        if (mActivity == null){
            changePicTask = new ChangePicTask(mFragment.getActivity());
        }else{
            changePicTask = new ChangePicTask(mActivity);
        }
        changePicTask.setShowProgressDialog(false);
        changePicTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast("头像修改失败:" + result.getMessage());
                callback.callback(false);
            }
        });

        changePicTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                callback.callback(true);

                ToastUtil.toast("头像修改成功");

                //本人头像地址替换
                LoginedUser.getLoginedUser().setPic(url);
                LoginedUser.saveToFile();
                //聊天本人头像替换
                AddressUser addressUser = DaoFactory.getAddressUserDao().findUserById(LoginedUser.getLoginedUser().getId() , LoginedUser.getLoginedUser().getId());
                addressUser.setAvatar(url);
                DaoFactory.getAddressUserDao().replaceOrInsert(addressUser);
                //圈子里面的头像替换
                List<CircleData.Circle> circleList = DaoFactory.getCircleDao().findAll();
                for (CircleData.Circle circle : circleList){
                    if (circle.getMember().getId().equals(LoginedUser.getLoginedUser().getId())){
                        circle.getMember().setPic(LoginedUser.getLoginedUser().getPic());
                    }
                }
                DaoFactory.getCircleDao().replaceIntoBatch(circleList);

                //刷新酒友圈中的头像
                ChangeAvatorReceiver.notifyReceiver();
            }
        });
        changePicTask.execute(url);
    }

    private interface Callback{
        void callback(boolean isSuccess);
    }

    public void refreshAvator(ImageView headIv) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        Bitmap temp = BitmapFactory.decodeResource(App.instance.getResources(), R.drawable.default_tx);
        config.setLoadFailedBitmap(temp);
        config.setLoadingBitmap(temp);
        BPBitmapLoader.getInstance().display(headIv, LoginedUser.getLoginedUser().getPic(), config);
    }

}
