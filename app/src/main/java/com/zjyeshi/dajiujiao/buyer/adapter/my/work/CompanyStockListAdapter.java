package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * 公司库存
 *
 * Created by zhum on 2016/6/15.
 */
public class CompanyStockListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<CompanyStock> dataList;

    public CompanyStockListAdapter(Context context,
                                   List<CompanyStock> dataList) {
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
                    R.layout.listitem_company_stock, null);

        }
        ImageView avatarIv = (ImageView)convertView.findViewById(R.id.avatarIv);
        TextView nameTv = (TextView)convertView.findViewById(R.id.nameTv);
        TextView typeTv = (TextView)convertView.findViewById(R.id.typeTv);
        TextView stockNumTv = (TextView)convertView.findViewById(R.id.stockNumTv);

        CompanyStock companyStock = dataList.get(position);
        if (Validators.isEmpty(companyStock.getUnit())){
            companyStock.setUnit("瓶");
        }
//        initImageView(avatarIv , companyStock.getPic() , R.drawable.default_img);
        GlideImageUtil.glidImage(avatarIv , companyStock.getPic() , R.drawable.default_img);
        initTextView(nameTv , companyStock.getName());
        int box = (Integer.parseInt(companyStock.getInventory()))/(Integer.parseInt(companyStock.getBottlesPerBox()));
        int bottle = (Integer.parseInt(companyStock.getInventory()))%(Integer.parseInt(companyStock.getBottlesPerBox()));

        initTextView(typeTv , companyStock.getSpecifications() + "/" + companyStock.getUnit() +"   " + companyStock.getBottlesPerBox()
                + companyStock.getUnit() + "/箱");

        initTextView(stockNumTv , box + "箱" + bottle + companyStock.getUnit());
        return convertView;
    }
}
