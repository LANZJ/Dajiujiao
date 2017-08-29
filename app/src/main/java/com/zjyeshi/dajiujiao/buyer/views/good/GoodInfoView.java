package com.zjyeshi.dajiujiao.buyer.views.good;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.bitmap.BPBitmapLoader;
import com.xuan.bigapple.lib.bitmap.BitmapDisplayConfig;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.App;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.good.GoodInfoEntity;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;

/**
 * 商品详情布局
 * Created by wuhk on 2016/4/7.
 */
public class GoodInfoView extends BaseView {
    private ImageView photoIv;
    private TextView nameTv;
    private TextView priceTv;
    public GoodInfoView(Context context) {
        super(context);
    }

    public GoodInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.view_good_info, this);
        ViewUtils.inject(this, this);
        photoIv = (ImageView)findViewById(R.id.photoIv);
        nameTv = (TextView)findViewById(R.id.nameTv);
        priceTv = (TextView)findViewById(R.id.priceTv);

    }

    public void bindData(GoodInfoEntity goodInfoEntity){
        GlideImageUtil.glidImage(photoIv , ExtraUtil.getResizePic(goodInfoEntity.getGoodIcon(), 400 ,400) , R.drawable.default_img);
        initTextView(nameTv , goodInfoEntity.getGoodName());
        initTextView(priceTv , "¥" + goodInfoEntity.getGoodPrice() + "/" + goodInfoEntity.getUnit());
    }

    /**
     * 设置图片，图片是空的话就设置默认图片
     *
     * @param imageView
     * @param url
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
}
