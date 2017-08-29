package com.zjyeshi.dajiujiao.buyer.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.circle.task.UpLoadFileTask;
import com.zjyeshi.dajiujiao.buyer.receiver.order.ChangeOrderStatusReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.other.GetUpLoadFileData;
import com.zjyeshi.dajiujiao.buyer.task.order.ReturnProductTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.widgets.uploadMultImage.GridViewImageWidget;

import java.util.List;

/**
 * 退款退货
 * Created by wuhk on 2016/8/22.
 */
public class ReturnGoodsActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.remarkEt)
    private EditText remarkEt;
    @InjectView(R.id.priceTv)
    private TextView priceTv;
    @InjectView(R.id.gridViewImageWidget)
    private GridViewImageWidget gridViewImageWidget;
    @InjectView(R.id.sendBtn)
    private TextView sendBtn;

    public static final String ORDERID = "order_id";
    public static final String AMOUNT = "amount";

    private String orderId = "";
    private String returnAmount = "";
    private String returnReason = "";
    private String pics = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_return_goods);
        initWidgets();
    }

    private void initWidgets(){
        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("申请退货");

        orderId = getIntent().getStringExtra(ORDERID);
        returnAmount = getIntent().getStringExtra(AMOUNT);

        priceTv.setText("¥" + ExtraUtil.format(ExtraUtil.getShowPrice(returnAmount)));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnReason = remarkEt.getText().toString();
                getPicUrlAndSend();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gridViewImageWidget.getUploadGridViewImageModel().onActivityResult(requestCode , resultCode , data , gridViewImageWidget.getDataList());
    }



    /**
     * 上传图片获取图片路径
     */
    private void getPicUrlAndSend(){
        List<String> editImageList = gridViewImageWidget.getDataList();
        if (!Validators.isEmpty(editImageList)){
            StringBuilder fileName = new StringBuilder();
            for(String localUrl : editImageList){
                fileName.append(localUrl);
                fileName.append(",");

            }
            fileName.deleteCharAt(fileName.length() - 1);
            UpLoadFileTask upLoadFileTask = new UpLoadFileTask(ReturnGoodsActivity.this);
            upLoadFileTask.setShowProgressDialog(true);
            upLoadFileTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<GetUpLoadFileData>() {
                @Override
                public void failCallback(Result<GetUpLoadFileData> result) {
                    ToastUtil.toast(result.getMessage());
                }
            });

            upLoadFileTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<GetUpLoadFileData>() {
                @Override
                public void successCallback(Result<GetUpLoadFileData> result) {
                    pics = result.getMessage();
//                    sendCircle();
                    applyReturn();
                }
            });

            upLoadFileTask.execute(fileName.toString() , String.valueOf(editImageList.size()));
        }else{
//            sendCircle();
            applyReturn();
        }
    }

    private void applyReturn(){
        ReturnProductTask returnProductTask = new ReturnProductTask(ReturnGoodsActivity.this);
        returnProductTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        returnProductTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("申请成功 , 等待卖家确认");
                finish();
                ChangeOrderStatusReceiver.notifyReceiver();
            }
        });

        returnProductTask.execute(orderId , priceTv.getText().toString() ,returnReason , pics);
    }

    public static void startActivity(Context context , String id , String amount){
        Intent intent = new Intent();
        intent.putExtra(ORDERID , id);
        intent.putExtra(AMOUNT , amount);
        intent.setClass(context , ReturnGoodsActivity.class);
        context.startActivity(intent);
    }
}
