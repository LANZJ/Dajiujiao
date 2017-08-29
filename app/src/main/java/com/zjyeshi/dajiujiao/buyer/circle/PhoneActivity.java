package com.zjyeshi.dajiujiao.buyer.circle;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.ioc.InjectView;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigappleui.lib.lettersort.entity.ItemContent;
import com.xuan.bigappleui.lib.lettersort.view.LetterSortView;
import com.xuan.bigdog.lib.widgets.title.DGTitleLayout;
import com.zjyeshi.dajiujiao.buyer.activity.BaseActivity;
import com.zjyeshi.dajiujiao.buyer.circle.task.GetContactListInfoTask;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.contact.PhoneUser;
import com.zjyeshi.dajiujiao.R;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.ContactListData;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录匹配
 * Created by wuhk on 2016/9/11.
 */
public class PhoneActivity extends BaseActivity {
    @InjectView(R.id.titleLayout)
    private DGTitleLayout titleLayout;
    @InjectView(R.id.letterSortView)
    private LetterSortView letterSortView;
    @InjectView(R.id.searchEt)
    private EditText searchEt;

    private MemberLetterAdapter memberLetterAdapter;
    private List<ItemContent> dataList = new ArrayList<ItemContent>();

    private boolean showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_phone);
        initWidgets();
    }

    private void initWidgets(){

        titleLayout.configReturn(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }).configTitle("手机联系人");

        letterSortView.getListView().setDividerHeight(0);
        letterSortView.getLetterSortBar().setVisibility(View.GONE);
        memberLetterAdapter = new MemberLetterAdapter(dataList, PhoneActivity.this);
        letterSortView.getListView().setAdapter(memberLetterAdapter);

        refreshData();
        loadData();

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = "%" + searchEt.getText().toString() + "%";
                List<PhoneUser> nameList = DaoFactory.getPhoneContactDao().searchUserByName(content);
                List<PhoneUser> numberList = DaoFactory.getPhoneContactDao().searchUserByNumber(content);
                List<PhoneUser> tempList = new ArrayList<PhoneUser>();
                tempList.addAll(nameList);
                tempList.addAll(numberList);

                dataList.clear();
                for (PhoneUser phoneUser : tempList){
                    ItemContent itemContent = new ItemContent(phoneUser.getPhoneName() , phoneUser);
                    dataList.add(itemContent);
                }

                memberLetterAdapter.notifyDataSetChanged(dataList);

            }
        });

    }

    private void loadData(){
        GetContactListInfoTask getContactListInfoTask = new GetContactListInfoTask(PhoneActivity.this);
        getContactListInfoTask.setShowProgressDialog(showDialog);
        getContactListInfoTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<ContactListData>() {
            @Override
            public void failCallback(Result<ContactListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getContactListInfoTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<ContactListData>() {
            @Override
            public void successCallback(Result<ContactListData> result) {
                List<ContactListData.ContactInfo> infoList = result.getValue().getList();
                for (ContactListData.ContactInfo contact : infoList){
                    DaoFactory.getPhoneContactDao().updateContacts(String.valueOf(contact.getType()) , contact.getPhone());
                }
                refreshData();
            }
        });

        getContactListInfoTask.execute();
    }

    private void refreshData(){
        dataList.clear();
        List<PhoneUser> tempList = DaoFactory.getPhoneContactDao().findAll();
        for (PhoneUser phoneUser : tempList){
            ItemContent itemContent = new ItemContent(phoneUser.getPhoneName() , phoneUser);
            dataList.add(itemContent);
        }
        if (Validators.isEmpty(dataList)){
            showDialog = true;
        }else{
            showDialog = false;
        }
        memberLetterAdapter.notifyDataSetChanged(dataList);
    }
}
