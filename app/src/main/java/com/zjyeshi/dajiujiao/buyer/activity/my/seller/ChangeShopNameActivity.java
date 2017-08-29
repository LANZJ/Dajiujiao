package com.zjyeshi.dajiujiao.buyer.activity.my.seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.entity.enums.ShopModifyEnum;
import com.zjyeshi.dajiujiao.buyer.receiver.info.ModifyShopInfoReceiver;
import com.zjyeshi.dajiujiao.R;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;

/**
 * 修改店铺名称
 * Created by wuhk on 2015/11/12.
 */
public class ChangeShopNameActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.shopNameEt)
    private EditText shopNameEt;
    @InjectView(R.id.cleanIv)
    private ImageView cleanIv;

    public static final String PARAM_MODIFY_TYPE = "param.modify.type";
    public static final String PARMA_SHOW_CONTENT = "param.show.content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_shop_name);
        initWidets();
    }

    private void initWidets(){
        final int modifyType = getIntent().getIntExtra(PARAM_MODIFY_TYPE , 99);
        String content = getIntent().getStringExtra(PARMA_SHOW_CONTENT);

        final String title = ShopModifyEnum.valueOf(modifyType).toString();

        titleLayout.configReturn("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle(title).configRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = shopNameEt.getText().toString();
                ModifyShopInfoReceiver.notifyReceiver(name, modifyType);
                finish();
            }
        });

        if (!Validators.isEmpty(content)){
            shopNameEt.setText(content);
        }

        shopNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shopNameEt.getText().toString().length() > 0) {
                    cleanIv.setVisibility(View.VISIBLE);
                } else {
                    cleanIv.setVisibility(View.GONE);
                }
            }
        });

        cleanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopNameEt.setText("");
            }
        });
    }

    public static void startActivity(Context context , ShopModifyEnum shopModifyEnum , String content){
        Intent intent = new Intent();
        intent.setClass(context , ChangeShopNameActivity.class);
        intent.putExtra(PARAM_MODIFY_TYPE , shopModifyEnum.getValue());
        intent.putExtra(PARMA_SHOW_CONTENT , content);
        context.startActivity(intent);
    }
}
