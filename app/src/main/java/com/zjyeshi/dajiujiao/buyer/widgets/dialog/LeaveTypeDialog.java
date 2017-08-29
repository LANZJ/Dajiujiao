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

import com.zjyeshi.dajiujiao.buyer.adapter.my.work.LeaveTypeAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.enums.work.LeaveTypeEnum;
import com.zjyeshi.dajiujiao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/6/20.
 */
public class LeaveTypeDialog extends Dialog {
    private LeaveTypeAdapter leaveTypeAdapter;
    private List<String> dataList = new ArrayList<String>();
    private ItemClickListener itemClickListener;

    public LeaveTypeDialog(Context context , ItemClickListener itemClickListener , List<String> dataList) {
        super(context);
        this.itemClickListener = itemClickListener;
        this.dataList = dataList;
    }

    public static interface ItemClickListener{
        void itemClick(String content);
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
        View view = inflater.inflate(R.layout.dialog_leave_type , null);
        setContentView(view);

        ListView listView = (ListView)view.findViewById(R.id.listView);
//        initList();
        leaveTypeAdapter = new LeaveTypeAdapter(getContext() , dataList);
        listView.setAdapter(leaveTypeAdapter);

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
        lp.width = (int) (d.widthPixels * 0.7); // 宽度设置为屏幕的0.7
        if (dataList.size() > 6){
            lp.height = (int) (d.heightPixels * 0.5); // 高度设置为屏幕的0.5
        }else{
            lp.height = lp.height;
        }
        dialogWindow.setAttributes(lp);
    }
}
