package com.zjyeshi.dajiujiao.buyer.circle.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.UserInfoActivity;
import com.zjyeshi.dajiujiao.buyer.circle.itementity.TopHeadEntity;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

/**
 * 头像布局
 *
 * Created by xuan on 15/10/18.
 */
public class TopHeadView extends BaseView {
    @InjectView(R.id.bgIv)
    private ImageView bgIv;
    @InjectView(R.id.centerIv)
    private ImageView centerIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;

    public TopHeadView(Context context) {
        super(context);
    }

    public TopHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.fragment_circle_topheadview, this);
        ViewUtils.inject(this, this);
    }

    public void bindData(final TopHeadEntity topHeadEntity){
        refreshAvator(centerIv , topHeadEntity.getHeadUrl());
        setBgImage(bgIv, topHeadEntity.getHeadBgUrl());
        initTextView(nameTv, topHeadEntity.getUserName());
        if (null != topHeadEntity.getUploadHeadModel()){
            topHeadEntity.getUploadHeadModel().initForFragment(centerIv , topHeadEntity.getFragment());
        }else{
            centerIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoActivity.startActivity(getContext() , topHeadEntity.getId());
                }
            });
        }
        if (null != topHeadEntity.getUploadCircleBgModel()){
            topHeadEntity.getUploadCircleBgModel().initForFragment(bgIv , topHeadEntity.getFragment());
        }
    }

    public void refreshAvator(ImageView headIv , String pic) {
//        BitmapDisplayConfig config = new BitmapDisplayConfig();
//        Bitmap temp = BitmapFactory.decodeResource(App.instance.getResources(), R.drawable.head_default);
//        config.setLoadFailedBitmap(temp);
//        config.setLoadingBitmap(temp);
//        config.setBitmapMaxHeight(200);
//        config.setBitmapMaxWidth(200);
//        BPBitmapLoader.getInstance().display(headIv, pic , config);
        GlideImageUtil.glidImage(headIv , pic , R.drawable.head_default);
    }

    //图片配置
    public void setBgImage(ImageView imageView, String url) {
//        if (!Validators.isEmpty(url)) {
//            BitmapDisplayConfig config = BitmapDisplayConfigFactory.getInstance().getCircleImageConfig();
//            config.setBitmapMaxHeight((int) App.instance.getResources().getDimension(R.dimen.circle_top_bg_image_max_size));
//            config.setBitmapMaxWidth((int)App.instance.getResources().getDimension(R.dimen.circle_top_bg_image_max_size));
//            BPBitmapLoader.getInstance().display(imageView, url, BitmapDisplayConfigFactory.getInstance().getCircleImageConfig());
//        } else {
//            imageView.setImageBitmap(App.defaultBitmap);
//        }
        GlideImageUtil.glidImage(imageView , url , R.drawable.default_img);
    }
}
