package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.activity.sales.SalesActivity;
import com.zjyeshi.dajiujiao.buyer.activity.sales.SalesDetailActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.EnvironmentTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFormTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesGiveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.sales.RemoveSalesTask;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.buyer.views.other.IVCheckBox;

import java.util.List;

/**
 * Created by wuhk on 2017/4/26.
 */

public class SalesListAdapter extends MBaseAdapter {
    private Context context;
    private List<SalesListData.Sales> dataList;

    public SalesListAdapter(Context context, List<SalesListData.Sales> dataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.listitem_sales_list , null);
        }

        TextView fillDesTv = (TextView)view.findViewById(R.id.fillDesTv);
        TextView fillTv = (TextView)view.findViewById(R.id.fillTv);
        final TextView cutDesTv = (TextView)view.findViewById(R.id.cutDesTv);
        TextView cutTv = (TextView)view.findViewById(R.id.cutTv);
        TextView timeTv = (TextView)view.findViewById(R.id.timeTv);

        final SalesListData.Sales sales = dataList.get(position);

        timeTv.setText(sales.getStartTime() + "~" + sales.getEndTime());

        //满足条件
        SalesFillTypeEnum salesFillTypeEnum = SalesFillTypeEnum.valueOf(sales.getSatisfyType());
        String fillDes = SalesFormTypeEnum.valueOf(sales.getFormType()).getName();
        if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_MONEY)){
            fillDesTv.setText(fillDes);
            fillTv.setText(sales.getSatisfyCondition());
        }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.FILL_BOX)){
            fillDesTv.setText(fillDes);
            fillTv.setText(sales.getSatisfyCondition() + "箱");
        }else if (salesFillTypeEnum.equals(SalesFillTypeEnum.MIX_BOX)){
            fillDesTv.setText("合购" + fillDes);
            fillTv.setText(sales.getSatisfyCondition() + "箱");
        }

        //优惠政策
        SalesGiveTypeEnum salesGiveTypeEnum = SalesGiveTypeEnum.valueOf(sales.getFavouredType());
        if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_GIFT)){
            cutDesTv.setText("送");
            cutTv.setText(sales.getGiftName());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.BACK_MONEY)){
            cutDesTv.setText("返");
            cutTv.setText(sales.getFavouredPolicy());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.CUT_MONEY)){
            cutDesTv.setText("减");
            cutTv.setText(sales.getFavouredPolicy());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_WINE)){
            cutDesTv.setText("送");
            cutTv.setText(sales.getGiveProductName() + sales.getFavouredPolicy() + "箱");
        }

//        SalesTypeEnum salesTypeEnum = SalesTypeEnum.valueOf(sales.getType());
//        if (salesTypeEnum.equals(SalesTypeEnum.CUT_MONEY)){
//            //实减金额
//            fillDesTv.setText("满");
//            fillTv.setText(sales.getFulfilMoney());
//            cutDesTv.setText("减");
//            cutTv.setText(sales.getMinusMoney());
//        }else if (salesTypeEnum.equals(SalesTypeEnum.GIVE_MONEY)){
//            //赠送金额
//            fillDesTv.setText("满");
//            fillTv.setText(sales.getFulfilMoney());
//            cutDesTv.setText("返");
//            cutTv.setText(sales.getMinusMoney());
//        }else if (salesTypeEnum.equals(SalesTypeEnum.GIVE_PRODUCT)){
//            //赠送商品
//            fillDesTv.setText("满");
//            fillTv.setText(sales.getFulfilMoney());
//            cutDesTv.setText("送");
//            cutTv.setText(sales.getGiftName());
//        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalesDetailActivity.startSalesDetailActivity(context , sales.getId());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String[] itemStr = {"删除活动"};
                View.OnClickListener[] ls = {new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveSalesTask.removeSales(context, sales.getId(), new AsyncTaskSuccessCallback<NoResultData>() {
                            @Override
                            public void successCallback(Result<NoResultData> result) {
                                ToastUtil.toast("删除活动成功");
                                ((SalesActivity)context).loadData();
                            }
                        });
                    }
                }};
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(context).setItemTextAndOnClickListener(itemStr, ls).create();
                dialog.show();
                return true;
            }
        });
        return view;
    }
}
