package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.my.ShopData;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.my.GetShopByCodeTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 店铺码
 * Created by wuhk on 2016/10/17.
 */
public class ShopMaActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.myShareLayout)
    private RelativeLayout myShareLayout;//我的店铺吗
    @InjectView(R.id.inputMaLayout)
    private RelativeLayout inputMaLayout;//输入店铺码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_shop_ma);
        initWidgets();
    }

    private void initWidgets() {
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("店铺码");

        //我的分享
        myShareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShopMaActivity.this, MyMaActivity.class);
                startActivity(intent);
            }
        });

        //扫描店铺吗
        inputMaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ShopMaActivity.this, ScanMaActivity.class);
                startActivityForResult(intent, ScanMaActivity.SCAN_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String ma = "";
            if (requestCode == ScanMaActivity.SCAN_REQUEST_CODE) {
                ma = data.getStringExtra(ScanMaActivity.PARAM_SCAN_TEXT);
                if (Validators.isEmpty(ma)) {
                    ToastUtil.toast("没有到扫描店铺码");
                } else {
                    gotoShopByCode(ma);
                }
            }
        }
    }

    //根据输入的酒友码到商铺
    private void gotoShopByCode(String code) {
        GetShopByCodeTask getShopByCodeTask = new GetShopByCodeTask(ShopMaActivity.this);
        getShopByCodeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<ShopData>() {
            @Override
            public void failCallback(Result<ShopData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        getShopByCodeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<ShopData>() {
            @Override
            public void successCallback(Result<ShopData> result) {
                ShopData.Shop shop = result.getValue().getShop();
                if (null == shop) {
                    ToastUtil.toast("对不起,店铺不存在呦~");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(ShopDetailActivity.PARAM_SHOPID, shop.getId());
                    intent.setClass(ShopMaActivity.this, ShopDetailActivity.class);
                    startActivity(intent);
                }
            }
        });
        getShopByCodeTask.execute(code);
    }
}
