package com.zjyeshi.dajiujiao.buyer.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.circle.UpLoadPhoneActivity;
import com.zjyeshi.dajiujiao.buyer.circle.task.AddContactListTask;
import com.zjyeshi.dajiujiao.buyer.circle.task.SaveAndUpLoadPhoneTask;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.contact.PhoneUser;
import com.zjyeshi.dajiujiao.buyer.task.data.NoResultData;
import com.zjyeshi.dajiujiao.buyer.utils.LogUtil;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhk on 2016/9/10.
 */
public class GetContactsModel{

    /**电话号码**/
    public static final int PHONES_NUMBER_INDEX = 0;

    /**联系人显示名称**/
    public  static final int PHONES_DISPLAY_NAME_INDEX = 1;

    /**获取库Phone表字段**/
    public static final String[] PHONES_PROJECTION = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};


    /**得到手机通讯录联系人信息**/
    public static void savePhoneContacts(final Context  mContext , final SaveSuccess saveSuccess) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                DGProgressDialog progressDialog = new DGProgressDialog(mContext);
                progressDialog.show();

                List<PhoneUser> phoneContacts = new ArrayList<PhoneUser>();

                ContentResolver resolver = mContext.getContentResolver();

                // 获取手机联系人
                Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

                if (phoneCursor != null) {
                    while (phoneCursor.moveToNext()) {

                        PhoneUser phoneUser = new PhoneUser();
                        phoneUser.setOwnerUserId(LoginedUser.getLoginedUser().getId());

                        //得到手机号码
                        String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                        //得到姓名
                        String phoneName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

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

                int oldLen = 0;
                List<PhoneUser> lengthList = DaoFactory.getPhoneContactDao().findAll();
                if (!Validators.isEmpty(lengthList)){
                    oldLen = lengthList.size();
                }

                DaoFactory.getPhoneContactDao().insertBatch(phoneContacts);
                if (!Validators.isEmpty(phoneContacts)){
                    if (oldLen < phoneContacts.size()){
                        boolean uploaded = BPPreferences.instance().getBoolean(UpLoadPhoneActivity.PHONE_UPLOAD_KEY , false);
                        if (uploaded){
                            saveSuccess.success(true);
                        }
                    }else{
                        saveSuccess.success(false);
                    }
                }
                progressDialog.dismiss();
            }
        }).start();
    }

    /**上传手机通讯录
     *
     */
    public static void upLoadContacts(Context mContext , boolean showDialog ,final UpLoadSuccess upLoadSuccess){
        List<PhoneUser> list = DaoFactory.getPhoneContactDao().findAll();
        StringBuilder sb = new StringBuilder();
        for (PhoneUser phoneUser : list){
            sb.append(phoneUser.getPhoneNumber());
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        String phones = sb.toString();

        AddContactListTask addContactListTask = new AddContactListTask(mContext);
        addContactListTask.setShowProgressDialog(showDialog);
        addContactListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        addContactListTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                LogUtil.e("上传成功");
                upLoadSuccess.success();
            }
        });

        addContactListTask.execute(phones);
    }


    /**得到手机通讯录联系人信息**/
    public static void saveAndUpLoadPhoneContacts(final Context  mContext , final UpLoadSuccess upLoadSuccess) {
        SaveAndUpLoadPhoneTask saveAndUpLoadPhoneTask = new SaveAndUpLoadPhoneTask(mContext);
        saveAndUpLoadPhoneTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<NoResultData>() {
            @Override
            public void failCallback(Result<NoResultData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        saveAndUpLoadPhoneTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<NoResultData>() {
            @Override
            public void successCallback(Result<NoResultData> result) {
                upLoadSuccess.success();
            }
        });

        saveAndUpLoadPhoneTask.execute();
    }

    public interface UpLoadSuccess{
        void success();
    }

    public interface SaveSuccess{
        void success(boolean change);
    }
}
