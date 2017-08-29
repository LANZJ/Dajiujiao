package com.zjyeshi.dajiujiao.buyer.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjyeshi.dajiujiao.buyer.adapter.store.SelectAddressDialogAdapter;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择地址dialog
 * Created by wuhk on 2016/7/19.
 */
public class AddressDialog extends Dialog {
    private SelectAddressDialogAdapter adapter;
    private List<Address> dataList = new ArrayList<Address>();
    private ItemClickListener itemClickListener;

    public AddressDialog(Context context, List<Address> dataList , ItemClickListener itemClickListener) {
        super(context);
        this.dataList = dataList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 背景透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);// 不需要标题
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_sel_address , null);
        setContentView(view);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        adapter = new SelectAddressDialogAdapter(getContext() , dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (null != itemClickListener){
                    itemClickListener.itemClick(dataList.get(position));
                }
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.7
        lp.height = (int) (d.heightPixels * 0.5); // 高度设置为屏幕的0.5
        dialogWindow.setAttributes(lp);
    }

    public  interface ItemClickListener{
        void itemClick(Address address);
    }
}
