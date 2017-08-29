package com.zjyeshi.dajiujiao.buyer.adapter.sales;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFillTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesFormTypeEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.SalesGiveTypeEnum;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lan on 2017/8/10.
 */
public class HuoDoAdapter extends MBaseAdapter {
    private Context context;
    private List<SalesListData.Sales> dataList = new ArrayList<SalesListData.Sales>();
    private IDialogControl iDialogControl;
   private HashMap map = new HashMap();
    List<String>muer=new ArrayList<String>();
    private  Boolean isan=false;
    private int zjm=98;
    //private int backgroundColor = Color.parseColor("#ffffff");
    SparseBooleanArray mCheckStates=new SparseBooleanArray();
    public HuoDoAdapter(Context context,IDialogControl iDialogControl, List<SalesListData.Sales> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.iDialogControl=iDialogControl;
        //this.backgroundColor = backgroundColor;

    }

    @Override
    public int getCount() {
        if (Validators.isEmpty(dataList)){
            return 0;
        }else{
            return dataList.size();
        }
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.hodolayout , null);
        }

        LinearLayout backLayout = (LinearLayout)view.findViewById(R.id.backLayout);
        ImageView flagIv = (ImageView)view.findViewById(R.id.flagIv);
        TextView fillDesTv = (TextView)view.findViewById(R.id.fillDesTv);
        TextView fillTv = (TextView)view.findViewById(R.id.fillTv);
        TextView cutDesTv = (TextView)view.findViewById(R.id.cutDesTv);
        TextView cutTv = (TextView)view.findViewById(R.id.cutTv);
        //final TextView ssss = (TextView)view.findViewById(R.id.ssss);
       //  CheckBox checkBox= (CheckBox) view.findViewById(R.id.checkBox);



        final SalesListData.Sales sales = dataList.get(position);

       // backLayout.setBackgroundColor(backgroundColor);
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
            if (sales.getSuperposition()){
            flagIv.setImageResource(R.drawable.icon_xiangsss);}else {
                flagIv.setImageResource(R.drawable.icon_send);
            }
            cutTv.setText(sales.getGiftName());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.BACK_MONEY)){
            cutDesTv.setText("" +
                    "");
            if (sales.getSuperposition()){
                flagIv.setImageResource(R.drawable.icon_xiangsss);}else {
            flagIv.setImageResource(R.drawable.icon_back);}
            cutTv.setText(sales.getFavouredPolicy());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.CUT_MONEY)){
            if (sales.getSuperposition()){
                flagIv.setImageResource(R.drawable.icon_xiangsss);}else {
            cutDesTv.setText("减");}
            flagIv.setImageResource(R.drawable.icon_cut);
            cutTv.setText(sales.getFavouredPolicy());
        }else if (salesGiveTypeEnum.equals(SalesGiveTypeEnum.GIVE_WINE)){
            if (sales.getSuperposition()){
                flagIv.setImageResource(R.drawable.icon_xiangsss);}else {
            cutDesTv.setText("送");}
            flagIv.setImageResource(R.drawable.icon_send);
            cutTv.setText(sales.getGiveProductName() + sales.getFavouredPolicy() + "箱");
        }
        mCheckStates.put(position,sales.getSuperposition());
        final Resources resources=context.getResources();
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zjm!=position) {
                    isan=true;
                }else {
                    isan=false;
                }
                Boolean op=mCheckStates.get(position);
                if (isan){
                    if (op){
                        String apid = sales.getId();
                       // Ft(isan, position, ssss, apid);
                    }else {
                        iDialogControl.onshio(isan,sales.getId());
                }}
               // et(isan,ssss,position,sales.getId());
            }
        });
        return view;
    }


   void Ft(boolean e,int p ,View ssss,String ops){
      // Resources resources=context.getResources();

      iDialogControl.onShowDialog(e,ops,p,muer);
    }

    void  et(Boolean isan,View ssss,int po,String ues){
        if (!isan){
            Resources resources=context.getResources();
            Drawable drawables=resources.getDrawable(R.drawable.hook);
            ssss.setBackgroundDrawable(drawables);
           ToastUtil.toast(po+"");
            zjm=99;
        }else {
            Resources resources=context.getResources();
            Drawable drawables=resources.getDrawable(R.drawable.hoo);
            ssss.setBackgroundDrawable(drawables);
            ToastUtil.toast(po+"");
            muer.add(ues);
            zjm=po;
        }

    }

            public interface IDialogControl {
                public void onShowDialog(boolean kl,String activityids,int position,List<String> maru);
                public void onshio(boolean k,String activityee);

 }
}
