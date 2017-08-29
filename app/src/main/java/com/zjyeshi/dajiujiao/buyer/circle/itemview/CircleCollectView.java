package com.zjyeshi.dajiujiao.buyer.circle.itemview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.circle.CircleImageView;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.CircleCollectionData;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.ShowImageActivity;
import com.zjyeshi.dajiujiao.buyer.chat.MyChatWebViewActivity;
import com.zjyeshi.dajiujiao.buyer.common.PassConstans;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;

import java.util.Date;

/**
 * Created by wuhk on 2016/8/16.
 */
public class CircleCollectView extends BaseView {
    @InjectView(R.id.avatarIv)
    private ImageView avatarIv;
    @InjectView(R.id.nameTv)
    private TextView nameTv;
    @InjectView(R.id.timeTv)
    private TextView timeTv;
    @InjectView(R.id.contentTv)
    private TextView contentTv;
    @InjectView(R.id.singleImageIv)
    private CircleImageView singleImageIv;
    @InjectView(R.id.pageLayout)
    private View pageLayout;//网页布局
    @InjectView(R.id.pageImageIv)
    private ImageView pageImageIv;//网页图标
    @InjectView(R.id.pageTextTv)
    private TextView pageTextTv;//网页标题


    public CircleCollectView(Context context) {
        super(context);
    }

    public CircleCollectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext() , R.layout.listitem_circle_collection , this);
        ViewUtils.inject(this , this);
        //布局先都隐藏
        contentTv.setVisibility(GONE);
        singleImageIv.setVisibility(GONE);
        pageLayout.setVisibility(GONE);
    }

    public void bindData(final CircleCollectionData.Collection collection){
//        initImageView(avatarIv , collection.getFromMemberPic());
        GlideImageUtil.glidImage(avatarIv , collection.getFromMemberPic() , R.drawable.default_img);
        initTextView(nameTv , collection.getFromMember());
        initTextView(timeTv , FriendlyTimeUtil.friendlyTime(new Date(collection.getCreationTime())));

        if (!Validators.isEmpty(collection.getContent())){
            contentTv.setVisibility(VISIBLE);
            contentTv.setText(collection.getContent());
        }else{
            contentTv.setVisibility(GONE);
        }

        if (!Validators.isEmpty(collection.getPic())){
            singleImageIv.setVisibility(VISIBLE);
//            initImageView(singleImageIv , collection.getPic());
            GlideImageUtil.glidImage(singleImageIv , collection.getPic() , R.drawable.default_img);
            singleImageIv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), ShowImageActivity.class);
                    intent.putExtra(PassConstans.IMAGEURL, collection.getPic());
                    getContext().startActivity(intent);
                }
            });
        }else{
            singleImageIv.setVisibility(GONE);
        }

        if (collection.getLinkType() == 2 || collection.getLinkType() == 3){
            pageLayout.setVisibility(VISIBLE);
//            initImageView(pageImageIv , collection.getLinkPic() , R.drawable.url_default_ic);
            GlideImageUtil.glidImage(pageImageIv , collection.getLinkPic() , R.drawable.url_default_ic);
            initTextView(pageTextTv , collection.getLinkTitle());
            pageLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyChatWebViewActivity.startForUrl(getContext() , collection.getLinkContent() , LoginedUser.getLoginedUser().getId());
                }
            });
        }else{
            pageLayout.setVisibility(GONE);
        }
    }

    public interface  CancelCallback{
        void callback();
    }
}
