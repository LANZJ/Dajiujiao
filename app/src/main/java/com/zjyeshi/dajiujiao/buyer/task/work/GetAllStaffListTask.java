package com.zjyeshi.dajiujiao.buyer.task.work;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.zjyeshi.dajiujiao.buyer.dao.DaoFactory;
import com.zjyeshi.dajiujiao.buyer.entity.contact.AddressUser;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.work.data.StaffListData;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wuhk on 2016/11/8.
 */
public class GetAllStaffListTask extends BaseTask<StaffListData> {

    public GetAllStaffListTask(Context context) {
        super(context);
    }

    @Override
    protected Result<StaffListData> onHttpRequest(Object... params) {
        HashMap<String, String> paramMap = new HashMap<String, String>();

        Result<StaffListData> result = postCommon(UrlConstants.ALLSTAFFLIST, paramMap);
        if (result.isSuccess()) {
            StaffListData retData = JSON.parseObject(result.getMessage(), StaffListData.class);
            result.setMessage(retData.getMessage());
            if (retData.codeOk()) {
                result.setValue(retData.getResult());
                List<StaffListData.Staff> staffList = retData.getResult().getList();
                List<AddressUser> addressUserList = new ArrayList<AddressUser>();

                for (StaffListData.Staff staff : staffList) {
                    AddressUser addressUser = new AddressUser();
                    addressUser.setUserId(staff.getId());
                    addressUser.setOwnerUserId(LoginedUser.getLoginedUser().getId());
                    addressUser.setName(staff.getName());
                    addressUser.setAvatar(staff.getPic());
                    addressUserList.add(addressUser);
                }
                DaoFactory.getAddressUserDao().insertBatch(addressUserList);
            } else {
                result.setSuccess(false);
            }
        }
        return result;
    }


    /**
     * 获取成员
     *
     * @param context
     * @param successCallback
     */
    public static void getAllStaffList(Context context, AsyncTaskSuccessCallback<StaffListData> successCallback) {
        GetAllStaffListTask getAllStaffListTask = new GetAllStaffListTask(context);
        getAllStaffListTask.setShowProgressDialog(false);
        getAllStaffListTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<StaffListData>() {
            @Override
            public void failCallback(Result<StaffListData> result) {
                ToastUtil.toast(result.getMessage());
            }
        });

        getAllStaffListTask.setAsyncTaskSuccessCallback(successCallback);

        getAllStaffListTask.execute();


    }
}
