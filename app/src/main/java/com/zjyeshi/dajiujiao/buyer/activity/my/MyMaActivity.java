package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.zxing.ZxConfig;
import com.xuan.bigdog.lib.zxing.ZxingUtils;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.my.DrinkingCodeData;
import com.zjyeshi.dajiujiao.buyer.task.my.GetMyDrinkingCodeTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.ScreenshotUtils;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 我的酒友码
 * Created by wuhk on 2015/9/30.
 */
public class MyMaActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;

    @InjectView(R.id.shareLayout)
    private LinearLayout shareLayout;

    @InjectView(R.id.shootLayout)
    private LinearLayout shootLayout;

    @InjectView(R.id.erCodeIv)
    private ImageView erCodeIv;

    @InjectView(R.id.myMaTv)
    private TextView myMaTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_ma);
        initWidgets();
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    private void initWidgets() {
        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleLayout.configTitle("我的店铺码");
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });

        showMa();
    }

    //获取我的酒友码
    private void getMyCode(boolean show){
        GetMyDrinkingCodeTask getMyDrinkingCodeTask = new GetMyDrinkingCodeTask(MyMaActivity.this);
        getMyDrinkingCodeTask.setShowProgressDialog(show);
        getMyDrinkingCodeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<DrinkingCodeData>() {
            @Override
            public void failCallback(Result<DrinkingCodeData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getMyDrinkingCodeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<DrinkingCodeData>() {
            @Override
            public void successCallback(Result<DrinkingCodeData> result) {
               String code = BPPreferences.instance().getString("drinkCode" , "no");
                myMaTv.setText(code);
                ZxConfig config = new ZxConfig();
                String saveFilePath = Constants.SDCARD_DJJBUYER_ERCODEIMAGE;
                config.setSaveFileName(saveFilePath);
                config.setBitmapHeight(800);
                config.setBitmapWidth(800);
                config.setBgColor(0xFFF8F8F8);
                ZxingUtils.encodeToBitmap(code, config);

                Bitmap b = BitmapFactory.decodeFile(saveFilePath);
                erCodeIv.setImageBitmap(b);

            }
        });

        getMyDrinkingCodeTask.execute();
    }

    //显示酒友码
    private void showMa(){
        String code = BPPreferences.instance().getString("drinkCode" , "no");
        if (code.equals("no")){
            getMyCode(true);
        }else{
            myMaTv.setText(code);
            Bitmap b = BitmapFactory.decodeFile( Constants.SDCARD_DJJBUYER_ERCODEIMAGE);
            erCodeIv.setImageBitmap(b);
            getMyCode(false);
        }
    }
    //SHARESDK
    private void showShare() {
        ShareSDK.initSDK(this);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("这是我的酒友码，快快输入酒友码来我的店铺吧");
        ScreenshotUtils.shotView(shootLayout, Constants.SDCARD_DJJBUYER_SHOTSCREEN);
        oks.setImagePath(Constants.SDCARD_DJJBUYER_SHOTSCREEN);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //网络图片的url：所有平台 你在分享的时候是获取网络的图片，分享的时候是需要相应的权限。而且，这个调用的接口是高级接口，你没有这个权限，这个是需要你去申请的
//        oks.setImageUrl("http://7sby7r.com1.z0.glb.clouddn.com/CYSJ_02.jpg");//网络图片rul

        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("https://www.baidu.com");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
}
