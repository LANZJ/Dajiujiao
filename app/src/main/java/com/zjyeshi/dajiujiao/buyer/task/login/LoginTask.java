package com.zjyeshi.dajiujiao.buyer.task.login;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigapple.lib.utils.sharepreference.BPPreferences;
import com.zjyeshi.dajiujiao.buyer.common.Constants;
import com.zjyeshi.dajiujiao.buyer.common.PreferenceConstants;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.task.BaseTask;
import com.zjyeshi.dajiujiao.buyer.task.data.login.LoginData;
import com.xuan.bigapple.lib.asynctask.helper.Result;

import java.util.HashMap;

/**
 * 登录任务类
 *
 * Created by wuhk on 2015/10/22.
 */
public class LoginTask extends BaseTask<LoginData> {
    public LoginTask(Context context) {
        super(context);
    }

    @Override
    protected Result<LoginData> onHttpRequest(Object... params) {
        HashMap<String,String> paramMap = new HashMap<String , String>();
        String phone = (String)params[0];
        String password  = (String)params[1];
        paramMap.put("phone" ,phone);
        paramMap.put("password" ,password);
        paramMap.put("uuid" ,deviceId());
        paramMap.put("os" ,"Android");
        paramMap.put("lngE5" ,(String)params[2]);
        paramMap.put("latE5" ,(String)params[3]);
        paramMap.put("terminalType" ,"2");

        Result<LoginData> result = post(UrlConstants.LOGIN ,paramMap);
        if (result.isSuccess()){
            LoginData loginData = JSON.parseObject(result.getMessage() ,LoginData.class);
            result.setMessage(loginData.getMessage());
            if (loginData.codeOk()){
                //code成功
                result.setValue(loginData.getResult());
            }else{
                result.setSuccess(false);
            }
        }
        return result;
    }

    // 获取唯一设备号
    private String deviceId() {
        TelephonyManager TelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }
}
