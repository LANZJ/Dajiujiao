package com.zjyeshi.dajiujiao.buyer.views.good;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.ioc.ViewUtils;
import com.xuan.bigapple.lib.utils.DateUtils;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.itemview.BaseView;
import com.zjyeshi.dajiujiao.buyer.entity.good.GoodCommentEntity;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodCommentList;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;

import java.util.Date;

/**
 * Created by wuhk on 2016/4/7.
 */
public class GoodCommentView extends BaseView {
    private ImageView photoIv;
    private TextView nameTv;
    private TextView contentTv;
    private TextView levelTv;
    private TextView timeTv;

    public GoodCommentView(Context context) {
        super(context);
    }

    public GoodCommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dgInit() {
        inflate(getContext(), R.layout.listitem_good_comment, this);
        ViewUtils.inject(this, this);

        photoIv = (ImageView)findViewById(R.id.photoIv);
        nameTv = (TextView)findViewById(R.id.nameTv);
        contentTv = (TextView)findViewById(R.id.contentTv);
        levelTv = (TextView)findViewById(R.id.levelTv);
        timeTv = (TextView)findViewById(R.id.timeTv);

    }

    public void setData(GoodCommentList.GoodComment goodComment){
        GlideImageUtil.glidImage(photoIv , goodComment.getPic() , R.drawable.default_img);
        initTextView(nameTv, goodComment.getMemberName());
        initTextView(contentTv , goodComment.getContent());
        initTextView(timeTv , DateUtils.date2StringBySecond(new Date(goodComment.getCreateTime())));
        int level = goodComment.getLevel();
        if (level == 0){
            initTextView(levelTv , "评价星级:" +  "暂无星级");
        }else{
            initTextView(levelTv , "评价星级:" +  goodComment.getLevel() + "星");
        }
    }

    public void bindData(GoodCommentEntity goodCommentEntity){
        GlideImageUtil.glidImage( photoIv , goodCommentEntity.getPic() , R.drawable.default_img);
        initTextView(nameTv, goodCommentEntity.getMemberName());
        initTextView(contentTv , goodCommentEntity.getContent());
        int level = goodCommentEntity.getLevel();
        if (level == 0){
            initTextView(levelTv , "评价星级:" +  "暂无星级");
        }else{
            initTextView(levelTv , "评价星级:" +  goodCommentEntity.getLevel() + "星");
        }
    }
}
