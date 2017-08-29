package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.task.work.data.ApproverListData;

import java.util.List;

/**
 * Created by wuhk on 2016/6/21.
 */
public class ApproverAdapter extends MBaseAdapter {
    private Context context;
    private List<ApproverListData.Approver> dataList;

    public ApproverAdapter(Context context, List<ApproverListData.Approver> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_choose_employee , null);
        }

        ImageView avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        TextView employerTv = (TextView)view.findViewById(R.id.employerTv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);

        employerTv.setVisibility(View.GONE);
        final ApproverListData.Approver approver = dataList.get(position);
//        initImageView(avatarIv, approver.getPic() , R.drawable.default_img);
        GlideImageUtil.glidImage(avatarIv , approver.getPic() , R.drawable.default_img);
        initTextView(nameTv, approver.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Activity)context).setResult(Activity.RESULT_OK , ((Activity)context).getIntent().
                        putExtra("approver", JSON.toJSONString(approver)));
                ((Activity)context).finish();
            }
        });
        return view;
    }
}
