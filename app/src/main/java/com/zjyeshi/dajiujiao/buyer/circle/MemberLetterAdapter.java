package com.zjyeshi.dajiujiao.buyer.circle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.IntentUtils;
import com.xuan.bigappleui.lib.lettersort.entity.ItemContent;
import com.xuan.bigappleui.lib.lettersort.entity.ItemDivide;
import com.xuan.bigappleui.lib.lettersort.entity.ItemTopContent;
import com.xuan.bigappleui.lib.lettersort.view.LetterSortAdapter;
import com.zjyeshi.dajiujiao.buyer.entity.contact.PhoneUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.PhoneUsedEnum;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.task.my.ApplyFriendTask;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;
import com.zjyeshi.dajiujiao.R;

import java.util.List;

/**
 * 带字母人员选择适配器
 * Created by wuhk on 2016/1/5.
 */
public class MemberLetterAdapter extends LetterSortAdapter {
    private Context context;

    public MemberLetterAdapter(List<ItemContent> list, Context context) {
        super(list, context);
        this.context = context;
    }

    @Override
    public View drawItemContent(int i, View view, ViewGroup viewGroup, final ItemContent itemContent) {
        view = LayoutInflater.from(context).inflate(R.layout.listitem_letter_member_content, null);
        TextView nameTv = (TextView)view.findViewById(R.id.nameTv);
        TextView phoneTv = (TextView)view.findViewById(R.id.phoneTv);
        final Button addBtn = (Button)view.findViewById(R.id.addBtn);
        TextView desTv = (TextView)view.findViewById(R.id.desTv);

        nameTv.setText(itemContent.getName());
        final PhoneUser phoneUser = (PhoneUser)itemContent.getValue();

        phoneTv.setText(phoneUser.getPhoneNumber());
        int phoneUsed = phoneUser.getPhoneUsed();
        PhoneUsedEnum phoneUsedEnum = PhoneUsedEnum.valueOf(phoneUsed);

        addBtn.setVisibility(View.GONE);
        desTv.setVisibility(View.GONE);
        if (phoneUsedEnum.equals(PhoneUsedEnum.NOTEXIT)){
            addBtn.setVisibility(View.VISIBLE);
            addBtn.setText("邀请");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtils.sendSmsByPhoneAndContext(context , phoneUser.getPhoneNumber() , "快来使用卖好酒吧！下载地址:http://a.app.qq.com/o/simple.jsp?pkgname=com.zjyeshi.dajiujiao");
                }
            });
        }else if (phoneUsedEnum.equals(PhoneUsedEnum.NOTFRIEND)){
            addBtn.setVisibility(View.VISIBLE);
            addBtn.setText("添加");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplyFriendTask applyFriendTask = new ApplyFriendTask(context);
                    applyFriendTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
                        @Override
                        public void failCallback(Result<NoResultData> result) {
                            ToastUtil.toast(result.getMessage());
                        }
                    });

                    applyFriendTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
                        @Override
                        public void successCallback(Result<NoResultData> result) {
                            ToastUtil.toast("申请成功");
                        }
                    });
                    applyFriendTask.execute(phoneUser.getPhoneNumber());
                }
            });
        }else if (phoneUsedEnum.equals(PhoneUsedEnum.FRIEND)){
            desTv.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public View drawItemTopContent(int i, View view, ViewGroup viewGroup, ItemTopContent itemTopContent) {
        return null;
    }

    @Override
    public View drawItemDivide(int i, View view, ViewGroup viewGroup, ItemDivide itemDivide) {
        View divider = LayoutInflater.from(context).inflate(R.layout.listitem_letter_member_divider, null);
        TextView typeTv = (TextView) divider.findViewById(R.id.typeTv);
        typeTv.setText(itemDivide.getLetter());
        return divider;
    }
}
