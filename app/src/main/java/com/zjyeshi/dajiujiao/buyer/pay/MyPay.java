package com.zjyeshi.dajiujiao.buyer.pay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.pay.aliutils.AliPayConfig;
import com.zjyeshi.dajiujiao.buyer.pay.wxutils.WxPayConfig;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.AppReqData;
import com.zjyeshi.dajiujiao.buyer.activity.store.BalanceAccountsActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.receiver.pay.AliPayReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.pay.WxPayData;
import com.zjyeshi.dajiujiao.buyer.task.pay.WxPayPrepareTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * 支付
 * Created by wuhk on 2015/12/18.
 */
public class MyPay {
    private BalanceAccountsActivity activity;
    private AliPayConfig aliPayConfig;
    private WxPayConfig wxPayConfig;

    public MyPay(Context context , AliPayConfig aliPayConfig) {
        this.aliPayConfig = aliPayConfig;
        activity = (BalanceAccountsActivity)context;
    }
    public MyPay(Context context , WxPayConfig wxPayConfig) {
        this.wxPayConfig = wxPayConfig;
        activity = (BalanceAccountsActivity)context;
    }

    /**微信支付
     *
     */
    public void wxPay(String orderId , String addressId , String totalFee  , Context context){

        ToastUtil.toast("获取订单中...");
        WxPayPrepareTask wxPayPrepareTask = new WxPayPrepareTask(context);
        wxPayPrepareTask.setShowProgressDialog(true);
        wxPayPrepareTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<WxPayData>() {
            @Override
            public void successCallback(Result<WxPayData> result) {
                String tradeNo = result.getValue().getTradeNo();
                BPPreferences.instance().putString("tradeNo", tradeNo);
                AppReqData appReqData = result.getValue().getAppReqData();
                PayReq req = new PayReq();
                req.appId = appReqData.getAppid();
                req.partnerId = appReqData.getPartnerid();
                req.prepayId = appReqData.getPrepayid();
                req.nonceStr = appReqData.getNoncestr();
                req.timeStamp = appReqData.getTimestamp();
                req.packageValue = appReqData.getPackage();
                req.sign = appReqData.getSign();
                ToastUtil.toast("正常调起支付...");

//                //先将app注册到微信
                IWXAPI api = WXAPIFactory.createWXAPI(activity, req.appId);
                api.sendReq(req);
            }
        });

        wxPayPrepareTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<WxPayData>() {
            @Override
            public void failCallback(Result<WxPayData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });
        String payMoney =  PassConstans.decimalFormat1.format(Float.parseFloat(totalFee) * 100);
        wxPayPrepareTask.execute(orderId, addressId, payMoney);

    }

    /**支付宝支付
     *
     * @param orderInfo
     * @param sign
     * @param tradeNo
     */
    public void aliPay(String orderInfo  , String sign , String tradeNo){
        if (TextUtils.isEmpty(aliPayConfig.getPartner()) || TextUtils.isEmpty(aliPayConfig.getSellerId())) {
            new AlertDialog.Builder(activity)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    activity.finish();
                                }
                            }).show();
            return;
        }

        // 完整的符合支付宝参数规范的订单信息
        orderInfo += "&out_trade_no=" + "\"" + tradeNo + "\"";

        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);
                AliPayReceiver.notifyReceiver(result);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    /**
     * 获取成功字符串
     *
     * @param result
     * @return
     */
    public static String getSuccess(String result) {
        String success = "";
        String[] params = result.split("&");
        for (String str : params) {
            if (str.startsWith("success")) {
                success = str;
            }
        }
        return success;
    }

    /**
     * 创建订单信息
     */
    public static String getOrderInfo(String subject, String body, String price) {

        MyAliPayConfig myAliPayConfig = new MyAliPayConfig();

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + myAliPayConfig.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + myAliPayConfig.getSellerId() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + myAliPayConfig.getNotifyUrl()
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时 ，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
//        orderInfo += "&extra_common_param=\"dasdad\"";
        return orderInfo;
    }

}
