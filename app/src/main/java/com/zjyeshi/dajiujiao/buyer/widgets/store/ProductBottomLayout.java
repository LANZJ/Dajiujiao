package com.zjyeshi.dajiujiao.buyer.widgets.store;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.buyer.activity.seller.ProductListActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.buyer.task.seller.ChangeProductTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.utils.Validators;

/**
 * 商品管理底部操作(酒库)
 * <p/>
 * Created by wuhk on 2015/11/5.
 */
public class ProductBottomLayout extends RelativeLayout {
    private LinearLayout bottomRemainLayout;
    private ImageView editIv;
    private ImageView closeIv;
    private ImageView photoIv;
    private TextView nameTv;
    private TextView pingPriceTv;
    private TextView xiangPriceTv;
    private TextView pingKcTv;
    private TextView xiangKcTv;
    private TextView desTv;
    private TextView okTv;
    private EditText pingPriceEt;
    private TextView xiangPriceTv2;
    private EditText pingKcEt;
    private TextView xiangKcTv2;

    private TextView editText;

    private LinearLayout okLayout;
    private LinearLayout editLayout;
    private RelativeLayout canChangeLayout;
    private RelativeLayout terminalLayout;

    private TextView unitPriceTv;
    private TextView unitRemainTv;
    private TextView canUnitPriceTv;
    private TextView canUnitRemainTv;
    private TextView terminalPriceTv;

    private ProductListActivity act;


    public ProductBottomLayout(Context context) {
        super(context);
        init();
    }

    public ProductBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductBottomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        act = (ProductListActivity) getContext();
        inflate(getContext(), R.layout.view_product_bottom, this);
        editIv = (ImageView) findViewById(R.id.editIv);
        okLayout = (LinearLayout) findViewById(R.id.okLayout);
        editLayout = (LinearLayout) findViewById(R.id.editLayout);
        okTv = (TextView) findViewById(R.id.okTv);
        closeIv = (ImageView) findViewById(R.id.closeIv);
        bottomRemainLayout = (LinearLayout) findViewById(R.id.bottomRemainLayout);
        photoIv = (ImageView) findViewById(R.id.photoIv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        desTv = (TextView) findViewById(R.id.desTv);
        canChangeLayout = (RelativeLayout) findViewById(R.id.canChangeLayout);//卖家非终端
        terminalLayout = (RelativeLayout) findViewById(R.id.terminalLayout);//卖家终端
        editText = (TextView) findViewById(R.id.editText);//终端时不能修改价格


        //未编辑状态
        pingPriceTv = (TextView) findViewById(R.id.pingPriceTv);
        xiangPriceTv = (TextView) findViewById(R.id.xiangPriceTv);
        pingKcTv = (TextView) findViewById(R.id.pingkcTv);
        xiangKcTv = (TextView) findViewById(R.id.xiangKcTv);
        //编辑状态
        pingPriceEt = (EditText) findViewById(R.id.pingPriceEt);
        xiangPriceTv2 = (TextView) findViewById(R.id.xiangPriceTv2);
        pingKcEt = (EditText) findViewById(R.id.pingkcEt);
        xiangKcTv2 = (TextView) findViewById(R.id.xiangKcTv2);

        unitPriceTv = (TextView)findViewById(R.id.unitPriceTv);
        unitRemainTv = (TextView)findViewById(R.id.unitRemainTv);
        canUnitPriceTv = (TextView)findViewById(R.id.canUnitPriceTv);
        canUnitRemainTv = (TextView)findViewById(R.id.canUnitRemainTv);
        terminalPriceTv = (TextView)findViewById(R.id.terminalPriceTv);


        //点击消失
        closeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(false);
                act.getProductBottomLayout().setVisibility(GONE);
            }
        });
        bottomRemainLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(false);
                act.getProductBottomLayout().setVisibility(GONE);
            }
        });
    }

    //显示数据
    public void bindData(final CompanyStock product) {
        String unit = product.getUnit();
        if (Validators.isEmpty(unit)){
            unit = "瓶";
        }
        initTextView(unitPriceTv , "/" + unit);
        initTextView(unitRemainTv ,  unit);
        initTextView(canUnitPriceTv , "/" + unit);
        initTextView(canUnitRemainTv , unit);
        initTextView(terminalPriceTv , "/" + unit);
//        initImageViewDefault(photoIv, product.getPic(), R.drawable.ic_default);
        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(product.getPic() , 400 , 400) , R.drawable.default_img);
        final float floatPrice = Float.parseFloat(product.getPrice()) / 100;
        final float floatXiangPrice = Float.parseFloat(String.valueOf((Integer.parseInt(product.getPrice()) * (Integer.parseInt(product.getBottlesPerBox()))))) / 100;
        //点击查看大图
        photoIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(PassConstans.IMAGEURL, product.getPic());
                intent.setClass(getContext(), ShowImageActivity.class);
                getContext().startActivity(intent);
            }
        });
        initTextView(nameTv, product.getName());
        initTextView(pingPriceTv, "¥" + ExtraUtil.format(floatPrice));
        initTextView(xiangPriceTv, "¥" + ExtraUtil.format(floatXiangPrice));
        initTextView(pingKcTv, product.getInventory());
        initTextView(xiangKcTv, String.valueOf((Integer.parseInt(product.getInventory()) / (Integer.parseInt(product.getBottlesPerBox())))));
        if (Validators.isEmpty(product.getDescription())) {
            initTextView(desTv, "暂无描述");
        } else {
            initTextView(desTv, product.getDescription());
        }

        //进入编辑状态
        editIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState(true);

                //将原先的数据显示在EditText,如果是终端，不能修改价格
//                if (LoginedUser.getLoginedUser().getUserEnum() == UserEnum.TERMINAL) {
//                    canChangeLayout.setVisibility(GONE);
//                    terminalLayout.setVisibility(VISIBLE);
//                    editText.setText(ExtraUtil.format(floatPrice));
//                } else {
//                    canChangeLayout.setVisibility(VISIBLE);
//                    terminalLayout.setVisibility(GONE);
//                    pingPriceEt.setText(ExtraUtil.format(floatPrice));
//                }
                //现在都不能修改价格,只能修改库存
                canChangeLayout.setVisibility(GONE);
                terminalLayout.setVisibility(VISIBLE);
                editText.setText(ExtraUtil.format(floatPrice));

                xiangPriceTv2.setText("¥" + ExtraUtil.format(floatXiangPrice));
                pingPriceEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (pingPriceEt.getText().toString().equals("")) {
                            xiangPriceTv2.setText("");
                        } else if (pingPriceEt.getText().toString().length() > 9) {
                            ToastUtil.toast("价格不能超过9位数");
                        } else {
                            xiangPriceTv2.setText("¥" + ExtraUtil.format(((Float.parseFloat(pingPriceEt.getText().toString()) * (Integer.parseInt(product.getBottlesPerBox()))))));
                        }
                    }
                });

                //库存都能修改
                pingKcEt.setText(product.getInventory());
                xiangKcTv2.setText(String.valueOf((Integer.parseInt(product.getInventory()) / (Integer.parseInt(product.getBottlesPerBox())))));
                if (LoginedUser.getLoginedUser().getUserEnum() == UserEnum.TERMINAL) {

                }
                pingKcEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (pingKcEt.getText().toString().equals("")) {
                            xiangKcTv2.setText("");
                        } else if (pingKcEt.getText().toString().length() > 9) {
                            ToastUtil.toast("价格不能超过9位");
                        } else {
                            xiangKcTv2.setText(String.valueOf((Integer.parseInt(pingKcEt.getText().toString()) / (Integer.parseInt(product.getBottlesPerBox())))));
                        }
                    }
                });

            }
        });
        //编辑完成
        okTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String price;
                //编辑完成时，终端取text的价格，其他取Et的价格
//                if (LoginedUser.getLoginedUser().getUserEnum() == UserEnum.TERMINAL) {
//                    price = editText.getText().toString();
//                } else {
//                    price = pingPriceEt.getText().toString();
//                }
                //现在都不能修改价格，都去text的价格
                price = editText.getText().toString();

                //库存都能修改，保存时取Et的价格
                final String inventory = pingKcEt.getText().toString();

                if (price.equals("0")) {
                    ToastUtil.toast("价格不能为0");
                } else if (price.equals("") || inventory.equals("")) {
                    ToastUtil.toast("请填写价格或库存");
                } else {
                    ChangeProductTask changeProductTask = new ChangeProductTask(getContext());
                    changeProductTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });
                    changeProductTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            ToastUtil.toast("修改商品信息成功");
                            changeState(false);
                            //将编辑好的数据存到TextView中
                            initTextView(pingPriceTv, "¥" + price);
                            initTextView(xiangPriceTv, xiangPriceTv2.getText().toString());
                            initTextView(pingKcTv, inventory);
                            initTextView(xiangKcTv, xiangKcTv2.getText().toString());
                            String bigPrice = PassConstans.decimalFormat1.format(Float.parseFloat(price) * 100);
                            product.setPrice(bigPrice);
                            product.setInventory(inventory);
//                            GoodData goodData = new GoodData();
//                            goodData.setProduct(product);
//                            DaoFactory.getMyWineDao().insert(goodData);
                            act.getProductListAdapter().notifyDataSetChanged();
                            act.calCount();
                        }
                    });

                    changeProductTask.execute(product.getId(), PassConstans.decimalFormat1.format(Float.parseFloat(price) * 100), inventory);
                }

            }
        });
    }

    //改变编辑状态
    private void changeState(boolean b) {
        if (b) {
            okLayout.setVisibility(GONE);
            editLayout.setVisibility(VISIBLE);
            editIv.setVisibility(GONE);
            okTv.setVisibility(VISIBLE);
        } else {
            okLayout.setVisibility(VISIBLE);
            editLayout.setVisibility(GONE);
            editIv.setVisibility(VISIBLE);
            okTv.setVisibility(GONE);
        }
    }

    /**
     * 设置图片
     *
     * @param imageView
     * @param url
     * @param resid
     */
    public void initImageViewDefault(ImageView imageView, String url, int resid) {
        imageView.setVisibility(View.VISIBLE);
        if (!Validators.isEmpty(url)) {
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            Bitmap temp = BitmapFactory.decodeResource(App.instance.getResources(), resid);
            config.setLoadFailedBitmap(temp);
            config.setLoadingBitmap(temp);
            BPBitmapLoader.getInstance().display(imageView, url, config);
        } else {
            imageView.setImageResource(resid);
        }
    }

    /**
     * 设置文本
     *
     * @param textView
     * @param text
     */
    public void initTextView(TextView textView, String text) {
        if (!Validators.isEmpty(text)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
