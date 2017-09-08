package com.zjyeshi.dajiujiao.wxapi;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xuan.bigdog.lib.utils.BDActivityManager;
import com.zjyeshi.dajiujiao.buyer.activity.order.MyOrderNewActivity;
import com.zjyeshi.dajiujiao.buyer.activity.seller.ProductBuyActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BuyCarActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.ShopDetailActivity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.LoginEnum;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.frame.FrameActivity;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.pay.MyWxPayConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;

	private TextView resultTv;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wxpay_result);

        resultTv = (TextView)findViewById(R.id.resultTv);

    	api = WXAPIFactory.createWXAPI(this, new MyWxPayConfig().getAppId());
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			BDActivityManager.removeAndFinishIncludes(MyOrderNewActivity.class.getSimpleName());

			MyOrderNewActivity.startOrderActivity(WXPayEntryActivity.this , LoginEnum.BURER.toString() , resp.errCode);

			BDActivityManager.removeAndFinishIncludes(BalanceAccountsActivity.class.getSimpleName(),BuyCarActivity.class.getSimpleName(), ShopDetailActivity.class.getSimpleName(), ProductBuyActivity.class.getSimpleName());
			finish();
			//FrameActivity.tab3Checked = true;
		}
	}
}