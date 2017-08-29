package com.zjyeshi.dajiujiao.buyer.adapter.my.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.view.listview.BUHighHeightListView;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.Employee;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.my.work.HmcEntity;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.HmcItemClickListner;
import com.zjyeshi.dajiujiao.buyer.widgets.callback.HmcLongClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhk on 2016/9/12.
 */
public class HmcListAdapter extends MBaseAdapter {
    private Context context;
    private List<HmcEntity> dataList = new ArrayList<HmcEntity>();
    private HmcLongClickListener longClickListener;
    private HmcItemClickListner clickListner;

    public HmcListAdapter(Context context, List<Employee> orginalList , HmcItemClickListner clickListner , HmcLongClickListener longClickListener) {
        this.context = context;
        this.clickListner = clickListner;
        this.longClickListener = longClickListener;
        reformData(orginalList);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if (null == view){
            view = LayoutInflater.from(context).inflate(R.layout.listitem_new_hmc , null);
        }
        final HmcEntity hmcEntity  = dataList.get(position);

        TextView provinceTv = (TextView)view.findViewById(R.id.provinceTv);
        BUHighHeightListView listView = (BUHighHeightListView)view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickListner.click(hmcEntity.getList().get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickListener.longClick(hmcEntity.getList().get(position));
                return true;
            }
        });
        HmcChildrenAdapter childrenAdapter = new HmcChildrenAdapter(context , hmcEntity.getList());
        listView.setAdapter(childrenAdapter);

        initTextView(provinceTv , hmcEntity.getProvince());


        return view;
    }


    public void notifyDataSetChanged(List<Employee> list) {
        reformData(list);
        super.notifyDataSetChanged();
    }

    private void reformData(List<Employee> list){
        dataList.clear();
        Map<String , List<Employee>> map = new HashMap<>();

        for (Employee employee : list){
            String province = employee.getProvince();

            List<Employee> itemList = map.get(province);
            if (Validators.isEmpty(itemList)){
                itemList = new ArrayList<Employee>();
                map.put(province , itemList);
            }
            itemList.add(employee);
        }

        for(Map.Entry<String, List<Employee>> entry : map.entrySet()){
            HmcEntity hmcEntity = new HmcEntity();
            hmcEntity.setProvince(entry.getKey());
            hmcEntity.setList(entry.getValue());
            dataList.add(hmcEntity);
        }

    }
}
