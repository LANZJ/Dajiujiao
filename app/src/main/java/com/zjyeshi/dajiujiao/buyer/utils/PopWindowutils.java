package com.zjyeshi.dajiujiao.buyer.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.adapter.sales.HuoDoAdapter;
import com.zjyeshi.dajiujiao.buyer.task.sales.data.SalesListData;

import java.util.List;

/**
 * Created by lan on 2017/8/15.
 */
public class PopWindowutils {

    private ListView listView;
    private PopupWindow window;
    //窗口在x轴偏移量
    private int xOff = 0;
    //窗口在y轴的偏移量
    private int yOff = 0;

    public PopWindowutils(Context context, HuoDoAdapter.IDialogControl iDialogControl, List<SalesListData.Sales> dataList) {

        window = new PopupWindow(context);
        //ViewGroup.LayoutParams.WRAP_CONTENT，自动包裹所有的内容
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(false);
        //点击 back 键的时候，窗口会自动消失
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);

        View localView = LayoutInflater.from(context).inflate(R.layout.lv_pw_menu, null);
       // listView = (ListView) localView.findViewById(R.id.huodongll);

        //HuoDoAdapter  huoDoAdapter=new HuoDoAdapter(context,iDialogControl,dataList);
        //listView.setAdapter(huoDoAdapter);
      //  listView.setTag(window);
        //设置显示的视图
        window.setContentView(localView);

    }

    public void setItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }


    public void dismiss() {
        window.dismiss();
    }

    /**
     * @param xOff x轴（左右）偏移
     * @param yOff y轴（上下）偏移
     */
    public void setOff(int xOff, int yOff) {
        this.xOff = xOff;
        this.yOff = yOff;
    }

    /**
     * @param paramView 点击的按钮
     */
    public void show(View paramView, int count) {
        //该count 是手动调整窗口的宽度
        window.setWidth(paramView.getWidth() * count);
        //设置窗口显示位置, 后面两个0 是表示偏移量，可以自由设置
        window.showAsDropDown(paramView, xOff, yOff);
        //更新窗口状态
        window.update();
    }


}