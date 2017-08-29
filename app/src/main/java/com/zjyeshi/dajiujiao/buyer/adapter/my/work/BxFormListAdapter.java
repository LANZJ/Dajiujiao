package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.dialog.DGSingleSelectDialog;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.BxFormItem;
import com.zjyeshi.dajiujiao.buyer.receiver.work.UpdatePriceReceiver;
import com.zjyeshi.dajiujiao.buyer.views.other.MyGridView;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.CostEnum;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhum on 2016/6/16.
 */
public class BxFormListAdapter extends MBaseAdapter {
    private final Context context;
    private final List<BxFormItem> dataList;

    public BxFormListAdapter(Context context,
                             List<BxFormItem> dataList) {
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
    public View getView(final int position, View convertView, ViewGroup arg2) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        convertView = mInflater.inflate(
                R.layout.listitem_bx_form_item, null);

        final BxFormItem bxFormItem = dataList.get(position);

        final EditText moneyEt = (EditText) convertView.findViewById(R.id.moneyEt);
        RelativeLayout typeLayout = (RelativeLayout) convertView.findViewById(R.id.typeLayout);
        final TextView typeTv = (TextView) convertView.findViewById(R.id.typeTv);
        MyGridView gridView = (MyGridView) convertView.findViewById(R.id.gridView);
        final EditText bzEt = (EditText) convertView.findViewById(R.id.bzEt);
        final RelativeLayout moneyLayout = (RelativeLayout)convertView.findViewById(R.id.moneyLayout);
        TextView itemTv = (TextView)convertView.findViewById(R.id.itemTv);
        TextView delTv = (TextView)convertView.findViewById(R.id.delTv);
        itemTv.setText("报销明细" + (position + 1));
        if (position == 0){
            delTv.setVisibility(View.GONE);
        }else{
            delTv.setVisibility(View.VISIBLE);
        }
        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataList.remove(bxFormItem);
                notifyDataSetChanged();
                UpdatePriceReceiver.notifyReceiver();
            }
        });

        if (bxFormItem.getApplicantType() == CostEnum.YANGPING.getValue()){
            moneyLayout.setVisibility(View.GONE);
        }else{
            moneyLayout.setVisibility(View.VISIBLE);
        }
        if (bxFormItem.getImageList() == null) {
            bxFormItem.setImageList(new ArrayList<String>());
        }
        LeaveConfirmImageAdapter leaveConfirmImageAdapter = new LeaveConfirmImageAdapter(context, bxFormItem.getImageList(), position);
        gridView.setAdapter(leaveConfirmImageAdapter);

        if (!Validators.isEmpty(bxFormItem.getApplcationMoney())) {
            moneyEt.setText(bxFormItem.getApplcationMoney());
        }

        if (bxFormItem.getApplicantType() != 0) {
            typeTv.setText(CostEnum.valueOf(bxFormItem.getApplicantType()).toString());
        }

        if (!Validators.isEmpty(bxFormItem.getRemark())) {
            bzEt.setText(bxFormItem.getRemark());
        }

        bzEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String remark = bzEt.getText().toString();
                bxFormItem.setRemark(remark);
            }
        });

        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = moneyEt.getText().toString();
                if (Validators.isNumeric(money)) {
                    bxFormItem.setApplcationMoney(moneyEt.getText().toString());
                    UpdatePriceReceiver.notifyReceiver();
                } else {
                    ToastUtil.toast("请输入正常的价格");
//                    moneyEt.setText("");
//                    moneyEt.setHint("点击输入");
                }
            }
        });


        typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DGSingleSelectDialog dialog = new DGSingleSelectDialog.Builder(context)
                        .setItemTextAndOnClickListener(new String[]{CostEnum.SHICHANG.toString(), CostEnum.YANGPING.toString() , CostEnum.QITA.toString()},
                                new View.OnClickListener[]{new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        typeTv.setText(CostEnum.SHICHANG.toString());
                                        bxFormItem.setApplicantType(CostEnum.SHICHANG.getValue());
                                        moneyLayout.setVisibility(View.VISIBLE);
                                    }
                                },new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        typeTv.setText(CostEnum.YANGPING.toString());
                                        bxFormItem.setApplicantType(CostEnum.YANGPING.getValue());
                                        moneyLayout.setVisibility(View.GONE);
                                        moneyEt.setText("0");
                                        bxFormItem.setApplcationMoney(moneyEt.getText().toString());
                                        UpdatePriceReceiver.notifyReceiver();
                                    }
                                },new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        typeTv.setText(CostEnum.QITA.toString());
                                        bxFormItem.setApplicantType(CostEnum.QITA.getValue());
                                        moneyLayout.setVisibility(View.VISIBLE);
                                    }
                                }}).create();
                dialog.show();
            }
        });

        return convertView;
    }
}
