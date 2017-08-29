package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.data.DailyCommentInfo;
import com.zjyeshi.dajiujiao.buyer.utils.FriendlyTimeUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.Date;
import java.util.List;

/**
 * Created by wuhk on 2016/7/18.
 */
public class DailyCommentAdapter extends MBaseAdapter {
    private Context context;
    private List<DailyCommentInfo.CommentInfo> dataList;

    public DailyCommentAdapter(Context context, List<DailyCommentInfo.CommentInfo> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_comment_list , null);
        }
        TextView contentTv = (TextView)view.findViewById(R.id.contentTv);
        TextView fromTv = (TextView)view.findViewById(R.id.fromTv);
        TextView timeTv = (TextView)view.findViewById(R.id.timeTv);

        DailyCommentInfo.CommentInfo commentInfo = dataList.get(position);

        initTextView(timeTv, FriendlyTimeUtil.friendlyTime(new Date(commentInfo.getCreationTime())));
        initTextView(fromTv,"来自:"+commentInfo.getMemberName());
        initTextView(contentTv,commentInfo.getContent());

        return view;
    }
}
