package com.zjyeshi.dajiujiao.buyer.adapter.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.data.store.goods.GoodCommentList;

import java.util.List;

/**商品评价列表适配器
 * Created by wuhk on 2016/4/7.
 */
public class GoodCommentsAdapter extends MBaseAdapter {
    private Context context;
    private List<GoodCommentList.GoodComment> dataList;

    public GoodCommentsAdapter(Context context, List<GoodCommentList.GoodComment> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_good_comment , null);
        }
        ImageView photoIv = (ImageView)view.findViewById(R.id.photoIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView levelTv = (TextView)view.findViewById(R.id.levelTv);

        GoodCommentList.GoodComment goodComment = dataList.get(position);
//        initImageView(photoIv, goodComment.getPic(), R.drawable.default_tx);
        GlideImageUtil.glidImage(photoIv , goodComment.getPic() , R.drawable.default_tx);
        initTextView(nameTv , goodComment.getMemberName());
        initTextView(contentTv , goodComment.getContent());
        int level = goodComment.getLevel();
        if (level == 0){
            initTextView(levelTv , "评价星级：" + "暂无星级");
        }else{
            initTextView(levelTv , "评价星级：" + level + "星");
        }
        return view;
    }
}
