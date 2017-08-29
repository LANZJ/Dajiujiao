package com.zjyeshi.dajiujiao.buyer.activity.my.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.Product;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.receiver.work.CanBuyProductReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.work.ModifyCanBuyPriceTask;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

/**
 * Created by wuhk on 2016/12/14.
 */
public class CanBuyProductInfoActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.avatarIv)
    private ImageView avatarIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.desTv)
    private TextView desTv;
    @InjectView(R.id.priceTv)
    private TextView priceTv;
    @InjectView(R.id.unitTv)
    private TextView unitTv;
    @InjectView(R.id.pingEt)
    private EditText pingEt;
    @InjectView(R.id.marketEt)
    private EditText marketEt;
    @InjectView(R.id.marketUnitTv)
    private TextView marketUnitTv;
    @InjectView(R.id.sureBtn)
    private Button sureBtn;

    public static final String PARAM_PRODUCT = "param.product";
    public static final String PARAM_SHOP_ID = "param.shop.id";
    private Product product;
    private String shopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_can_buy_product_info);
        initWidgets();
    }

    private void initWidgets(){
        String jsonStr = getIntent().getStringExtra(PARAM_PRODUCT);
        product = JSON.parseObject(jsonStr , Product.class);
        shopId = getIntent().getStringExtra(PARAM_SHOP_ID);

        titleLayout.configTitle("修改价格").configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        GlideImageUtil.glidImage(avatarIv , product.getPic() , R.drawable.default_img);
        initTextView(nameTv , product.getName());
        desTv.setText(product.getSpecifications() + "/" +product.getUnit() + "  " + product.getBottlesPerBox()
                + product.getUnit() + "/箱");
        unitTv.setText("元/" + product.getUnit());
        marketUnitTv.setText("元/" + product.getUnit());

        priceTv.setText("¥" + ExtraUtil.format(Float.parseFloat(product.getPrice())/100) + "/" + product.getUnit());


        pingEt.setText(ExtraUtil.format(Float.parseFloat(product.getPrice())/100));
        marketEt.setText(ExtraUtil.format(Float.parseFloat(product.getMarketCost())/100));

        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = pingEt.getText().toString();
                String market = marketEt.getText().toString();
                if (Validators.isEmpty(price) || Validators.isEmpty(market)){
                    ToastUtil.toast("请输入价格或者市场支持费用");
                }else if (!Validators.isNumeric(price) || !Validators.isNumeric(market)){
                    ToastUtil.toast("请输入正确价格");
                }else{
                    modifyCanBuyPrice(product.getId() , price , market , shopId);
                }
            }
        });

    }

    private void modifyCanBuyPrice(String productId , String price , String market , String shopId){
        ModifyCanBuyPriceTask modifyCanBuyPriceTask = new ModifyCanBuyPriceTask(CanBuyProductInfoActivity.this);
        modifyCanBuyPriceTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        modifyCanBuyPriceTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                ToastUtil.toast("修改成功");
                CanBuyProductReceiver.notifyReceiver();
                finish();
            }
        });

        modifyCanBuyPriceTask.execute(productId , price , shopId , market);
    }

    public static void startActivity(Context context , String shopId ,  Product product){
        Intent intent = new Intent();
        intent.setClass(context , CanBuyProductInfoActivity.class);
        intent.putExtra(PARAM_SHOP_ID , shopId);
        intent.putExtra(PARAM_PRODUCT , JSON.toJSONString(product));
        context.startActivity(intent);
    }
}
