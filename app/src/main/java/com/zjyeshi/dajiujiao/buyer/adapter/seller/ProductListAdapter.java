package com.zjyeshi.dajiujiao.buyer.adapter.seller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.CompanyStock;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.GlideImageUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * 商品列表适配器
 * <p/>
 * Created by xuan on 15/10/28.
 */
public class ProductListAdapter extends MBaseAdapter {
    private List<CompanyStock> dataList;
    private Context context;

    public ProductListAdapter(Context context, List<CompanyStock> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(int postion, View view, ViewGroup arg2) {
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.seller_layout_product_list_item, null);
        }

        ImageView productIv = (ImageView) view.findViewById(R.id.productIv);
        TextView titleTv = (TextView) view.findViewById(R.id.titleTv);
        TextView detailTv = (TextView) view.findViewById(R.id.detailTv);
        TextView priceTv = (TextView) view.findViewById(R.id.priceTv);

        CompanyStock data = dataList.get(postion);
        GlideImageUtil.glidImage(productIv , ExtraUtil.getResizePic(data.getPic() , 200 , 200) , R.drawable.default_img);
        initTextView(titleTv, data.getName());
        if (Integer.parseInt(data.getInventory()) < 0){
            data.setInventory("0");
        }
        initTextView(detailTv, "库存:" + getKc(data));
        initTextView(priceTv, "¥" + ExtraUtil.format(Float.parseFloat(data.getPrice())/100) + "/" + data.getUnit());

        return view;
    }

    /**计算库存
     *
     * @param companyStock
     * @return
     */
    private String getKc(CompanyStock companyStock){
        int invetory = Integer.parseInt(companyStock.getInventory());
        int perBox = Integer.parseInt(companyStock.getBottlesPerBox());

        int boxNum = invetory / perBox;
        int unitNum = invetory % perBox;

        if (boxNum == 0){
            return unitNum + companyStock.getUnit();
        }else{
            return boxNum + "箱" + unitNum + companyStock.getUnit();
        }
    }

}
