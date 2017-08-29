package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesDetailData;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * Created by wuhk on 2017/4/27.
 */

public class JoinAdapter extends MBaseAdapter {
    private Context context;
    private List<SalesDetailData.Join> dataList;

    public JoinAdapter(Context context, List<SalesDetailData.Join> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_select_sale_shop , null);
        }

        ImageView avatarIv = (ImageView)view.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        final IVCheckBox checkIv = (IVCheckBox)view.findViewById(R.id.checkIv);

        checkIv.setVisibility(View.GONE);

        final SalesDetailData.Join data = dataList.get(position);
        GlideImageUtil.glidImage(avatarIv , ExtraUtil.getResizePic(data.getPic() , 80 , 80) , R.drawable.default_img);
        initTextView(nameTv , data.getName());

        return view;
    }
}
