package com.zjyeshi.dajiujiao.buyer.circle.task;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.model.GetContactsModel;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.PhoneUser;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuhk on 2016/10/11.
 */
public class SaveAndUpLoadPhoneTask extends BaseTask<NoResultData> {
    public SaveAndUpLoadPhoneTask(Context context) {
        super(context);
    }

    @Override
    protected Result<NoResultData> onHttpRequest(Object... params) {
        List<PhoneUser> phoneContacts = new ArrayList<PhoneUser>();

        ContentResolver resolver = context.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, GetContactsModel.PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                PhoneUser phoneUser = new PhoneUser();
                phoneUser.setOwnerUserId(LoginedUser.getLoginedUser().getId());

                //得到手机号码
                String phoneNumber = phoneCursor.getString(GetContactsModel.PHONES_NUMBER_INDEX);
                //得到姓名
                String phoneName = phoneCursor.getString(GetContactsModel.PHONES_DISPLAY_NAME_INDEX);

                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)){
                    continue;
                }
                phoneUser.setPhoneName(phoneName);
                if (phoneNumber.startsWith("+86")){
                    phoneNumber = phoneNumber.substring(3 , phoneNumber.length() - 1);
                }else if (phoneNumber.contains("-")){
                    phoneNumber = phoneNumber.replace("-" , "");
                }
                phoneUser.setPhoneNumber(phoneNumber);

                phoneContacts.add(phoneUser);
            }

            phoneCursor.close();
        }

        DaoFactory.getPhoneContactDao().insertBatch(phoneContacts);

        List<PhoneUser> list = DaoFactory.getPhoneContactDao().findAll();
        StringBuilder sb = new StringBuilder();
        for (PhoneUser phoneUser : list){
            sb.append(phoneUser.getPhoneNumber());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        String phones = sb.toString();

        HashMap<String , String> paramMap = new HashMap<String, String>();
        paramMap.put("phones" , phones);

        Result<NoResultData> result = postCommon(UrlConstants.ADDCONTACTLIST , paramMap);
        if (result.isSuccess()){
            NoResultData retData = JSON.parseObject(result.getMessage() , NoResultData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()){
                result.setValue(retData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }
}
