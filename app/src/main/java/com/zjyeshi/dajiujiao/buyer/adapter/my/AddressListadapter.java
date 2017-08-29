package com.zjyeshi.dajiujiao.buyer.adapter.my;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjyeshi.dajiujiao.buyer.activity.my.personal.EditAddressActivity;
import com.zjyeshi.dajiujiao.buyer.activity.my.personal.MyAddressActivity;
import com.zjyeshi.dajiujiao.buyer.adapter.MBaseAdapter;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.my.Address;
import com.zjyeshi.dajiujiao.buyer.task.my.DeleteAddressTask;
import com.zjyeshi.dajiujiao.buyer.utils.ExtraUtil;
import com.zjyeshi.dajiujiao.buyer.utils.SingleSelectDialogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.receiver.info.SelectAddressReceiver;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址列表适配器
 * Created by wuhk on 2015/11/13.
 */
public class AddressListadapter extends MBaseAdapter {
    private Context context;
    private List<Address> dataList = new ArrayList<Address>();
    private boolean isSelect;
    private Activity act;

    public AddressListadapter(Context context, List<Address> dataList , boolean isSelect) {
        this.context = context;
        this.dataList = dataList;
        this.isSelect = isSelect;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        act = (MyAddressActivity)context;
        view = LayoutInflater.from(context).inflate(R.layout.listitem_address , null);
        final Address address = dataList.get(position);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView addressTv = (TextView)view.findViewById(R.id.addressTv);
        ImageView editIv = (ImageView)view.findViewById(R.id.editIv);

        initTextView(nameTv , address.getName());
        String area = ExtraUtil.getAreaByCode(address.getArea());
        initTextView(addressTv, area + address.getAddress());
        //地址编辑
        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(PassConstans.ADDRESS, address);
//                intent.putExtras(bundle);
//                intent.setClass(context, EditAddressActivity.class);
//                context.startActivity(intent);
                EditAddressActivity.startActivity(context , address , "");
            }
        });
        //选择地址
        if (isSelect){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selectId = address.getId();
                    for (Address ad : dataList) {
                        if (ad.getId().equals(selectId)) {
                            ad.setSelected(1);
                        } else {
                            ad.setSelected(2);
                        }
                    }
                    DaoFactory.getAddressDao().insertBatch(dataList, LoginedUser.getLoginedUser().getId());
                    act.finish();
                    SelectAddressReceiver.notifyReceiver();
                }
            });
        }
        //删除地址
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                List<String> itemList = new ArrayList<String>();
                List<View.OnClickListener> onClickListenerList = new ArrayList<View.OnClickListener>();

                    itemList.add("删除该地址");
                    onClickListenerList.add(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteAddress(position);
                        }
                    });

                SingleSelectDialogUtil dialog = new SingleSelectDialogUtil.Builder(context)
                        .setItemTextAndOnClickListener(
                                itemList.toArray(new String[itemList.size()]),
                                onClickListenerList
                                        .toArray(new View.OnClickListener[onClickListenerList
                                                .size()])).createInstance();
                dialog.show();
                return true;
            }
        });
        return view;
    }


    //删除收货地址
    private void deleteAddress(final int position){
        final Address address = dataList.get(position);
        DeleteAddressTask deleteAddressTask = new DeleteAddressTask(context);
        deleteAddressTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        deleteAddressTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                dataList.remove(position);
                DaoFactory.getAddressDao().deleteById(address.getId() , LoginedUser.getLoginedUser().getId());
                notifyDataSetChanged();
            }
        });

        deleteAddressTask.execute(address.getId());
    }

}
