package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.task.work.data.CostPaidInfoData;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CostEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 审批
 *
 * Created by zhum on 2016/6/23.
 */
public class PaidListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<CostPaidInfoData.Paid> dataList;

    public PaidListAdapter(Context context,
                                 List<CostPaidInfoData.Paid> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != dataList) {
            count = dataList.size();
        }
        return count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        if (null == convertView) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(
                    R.layout.listitem_paid_list, null);
        }
        TextView titleTv = (TextView)convertView.findViewById(R.id.titleTv);
        TextView timeTv = (TextView)convertView.findViewById(R.id.timeTv);
        TextView priceTv = (TextView)convertView.findViewById(R.id.priceTv);
        TextView spPersonTv = (TextView)convertView.findViewById(R.id.spPersonTv);

        MyGridView gridView = (MyGridView)convertView.findViewById(R.id.gridView);
        CostPaidInfoData.Paid paid = dataList.get(position);

        titleTv.setText(CostEnum.valueOf(Integer.parseInt(paid.getApplicantType())).toString());

        initTextView(timeTv,paid.getRemark());
        if (Validators.isEmpty(paid.getApplicationMoney())){
            spPersonTv.setVisibility(View.GONE);
            priceTv.setVisibility(View.GONE);
        }else{
            spPersonTv.setVisibility(View.VISIBLE);
            priceTv.setVisibility(View.VISIBLE);
            initTextView(priceTv,"¥"+paid.getApplicationMoney());
        }
        List<String> imageList = new ArrayList<String>();
        if (!Validators.isEmpty(paid.getPics())){
            if (paid.getPics().contains(",")){
                imageList = Arrays.asList(paid.getPics().split(","));
            }else{
                imageList.clear();
                imageList.add(paid.getPics());
            }
        }
        LeaveDetailImageAdapter adapter = new LeaveDetailImageAdapter(context , imageList);
        gridView.setAdapter(adapter);

        return convertView;
    }
}
