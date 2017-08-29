package com.xuan.bigdog.lib.bservice.bregister;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseTask;
import com.xuan.bigdog.lib.bservice.bcommon.BServiceNoResultData;

import java.util.HashMap;

/**
 * 注册忘记密码
 *
 * Created by xuan on 16/2/16.
 */
public class BDRegisterModel {
    private static BDRegisterModel instance;

    private BDRegisterModel() {

    }

    public static BDRegisterModel getInstance() {
        if (null == instance) {
            instance = new BDRegisterModel();
        }
        return instance;
    }

    /**
     * 发送手机验证码
     *
     * @param website
     * @param type
     * @param phone
     * @param l
     */
    public void sendSmsCode(Context context ,String website, String type, String phone, final BDSendSmsCodeListener l){
        String url = website + "common/sms/sendSmsCode.htm";
        SendSmsCodeTask sendSmsCodeTask = new SendSmsCodeTask(context , url);
        sendSmsCodeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BServiceNoResultData>() {
            @Override
            public void successCallback(Result<BServiceNoResultData> result) {
                l.success();
            }
        });
        sendSmsCodeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<BServiceNoResultData>() {
            @Override
            public void failCallback(Result<BServiceNoResultData> result) {
                l.fail(result.getMessage());
            }
        });

        sendSmsCodeTask.execute(phone , type);
    }

    /**
     * 发送手机验证码-注册
     *
     * @param website
     * @param phone
     * @param l
     */
    public void sendSmsCode4Register(Context context ,String website,String phone, BDSendSmsCodeListener l){
        String type = "REGISTER";
        sendSmsCode(context, website, type, phone, l);
    }

    /**
     * 发送手机验证码-忘记密码
     *
     * @param website
     * @param phone
     * @param l
     */
    public void sendSmsCode4forgetPassword(Context context ,String website,String phone, BDSendSmsCodeListener l){
        String type = "FORGET_PASSWORD";
        sendSmsCode(context, website, type, phone, l);
    }

    /**验证手机验证码
     *
     * @param context
     * @param website
     * @param type
     * @param phone
     * @param smsCode
     * @param l
     */
    public void verifySmsCode(Context context ,String website, String type, String phone , String smsCode , final BDVerifySmsCodeListener l){
        String url = website + "common/sms/verifySmsCode.htm";
        VerifySmsCodeTask verifySmsCodeTask = new VerifySmsCodeTask(context , url);
        verifySmsCodeTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BServiceNoResultData>() {
            @Override
            public void successCallback(Result<BServiceNoResultData> result) {
                l.success();
            }
        });

        verifySmsCodeTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<BServiceNoResultData>() {
            @Override
            public void failCallback(Result<BServiceNoResultData> result) {
                l.fail(result.getMessage());
            }
        });

        verifySmsCodeTask.execute(phone, smsCode, type);
    }


    /**注册验证手机验证码
     *
     * @param context
     * @param webSite
     * @param phone
     * @param smsCode
     * @param l
     */
    public void verifySmsCode4Register(Context context , String webSite , String phone , String smsCode , BDVerifySmsCodeListener l){
        String type = "REGISTER";
        verifySmsCode(context, webSite, type, phone, smsCode, l);
    }

    /**忘记密码验证手机验证码
     *
     * @param context
     * @param webSite
     * @param phone
     * @param smsCode
     * @param l
     */
    public void verifySmsCode4ForgetPassword(Context context , String webSite , String phone , String smsCode , BDVerifySmsCodeListener l){
        String type = "FORGET_PASSWORD";
        verifySmsCode(context, webSite, type, phone, smsCode, l);
    }

    /**
     * 忘记密码，设置密码
     *
     * @param website
     * @param phone
     * @param password
     * @param l
     */
    public void setPassword(Context context , String website,String phone, String password ,  String roleType ,final BDSetPasswordListener l){
        String url = website + "common/passport/setPassword.htm";
        SetPasswordTask  setPasswordTask = new SetPasswordTask(context , url);
        setPasswordTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BServiceNoResultData>() {
            @Override
            public void successCallback(Result<BServiceNoResultData> result) {
                l.success();
            }
        });

        setPasswordTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<BServiceNoResultData>() {
            @Override
            public void failCallback(Result<BServiceNoResultData> result) {
                l.fail(result.getMessage());
            }
        });

        setPasswordTask.execute(phone, password , roleType);
    }

    /**
     * 注册，设置密码
     *
     * @param context
     * @param website
     * @param phone
     * @param password
     * @param roleType
     * @param l
     */
    public void register(Context context , String website,String phone, String password , String roleType ,final BDRegisterListener l){
        String url = website + "common/passport/register.htm";
        RegisterTask registerTask = new RegisterTask(context , url);
        registerTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BDRegisterData>() {
            @Override
            public void successCallback(Result<BDRegisterData> result) {
                l.success(result.getValue().getUserId());
            }
        });
        registerTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<BDRegisterData>() {
            @Override
            public void failCallback(Result<BDRegisterData> result) {
                l.fail(result.getMessage());
            }
        });

        registerTask.execute(phone , password , roleType);
    }


    /**手机获取验证码Task
     *
     */
    private class SendSmsCodeTask extends BServiceBaseTask<BServiceNoResultData> {
        private String url;
        public SendSmsCodeTask(Context context , String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BServiceNoResultData> onHttpRequest(Object... params) {
            HashMap<String , String> paramMap = new HashMap<String , String>();
            paramMap.put("phone" , (String)params[0]);
            paramMap.put("type" , (String)params[1]);

            Result<BServiceNoResultData> result = bPost(url , paramMap);
            if (result.isSuccess()){
                BServiceNoResultData retData = JSON.parseObject(result.getMessage() , BServiceNoResultData.class);
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

    /**设置密码Task
     *
     */
    private class  SetPasswordTask extends BServiceBaseTask<BServiceNoResultData>{
        private String url;
        public SetPasswordTask(Context context , String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BServiceNoResultData> onHttpRequest(Object... params) {
            HashMap<String , String> paramMap = new HashMap<String , String>();
            paramMap.put("phone" , (String)params[0]);
            paramMap.put("password" , (String)params[1]);
            paramMap.put("roleType" , (String)params[2]);

            Result<BServiceNoResultData> result = bPost(url , paramMap);
            if (result.isSuccess()){
                BServiceNoResultData retData = JSON.parseObject(result.getMessage() , BServiceNoResultData.class);
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

    /**验证手机验证码Task
     *
     */
    private class VerifySmsCodeTask extends BServiceBaseTask<BServiceNoResultData>{
        private String url;

        public VerifySmsCodeTask(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BServiceNoResultData> onHttpRequest(Object... params) {
            HashMap<String , String> paramMap = new HashMap<String , String>();
            paramMap.put("phone" , (String)params[0]);
            paramMap.put("smsCode" , (String)params[1]);
            paramMap.put("type" , (String)params[2]);

            Result<BServiceNoResultData> result = bPost(url , paramMap);
            if (result.isSuccess()){
                BServiceNoResultData retData = JSON.parseObject(result.getMessage() , BServiceNoResultData.class);
                result.setMessage(retData.getMessage());
                if(retData.codeOk()){
                    result.setValue(retData.getResult());
                }else{
                    result.setSuccess(false);
                }
            }
            return result;
        }
    }

    /**注册Task
     *
     */
    private class  RegisterTask extends BServiceBaseTask<BDRegisterData>{
        private String url ;

        public RegisterTask(Context context, String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BDRegisterData> onHttpRequest(Object... params) {
            HashMap<String , String> paramMap = new HashMap<String  , String>();
            paramMap.put("phone" , (String)params[0]);
            paramMap.put("password" , (String)params[1]);
            paramMap.put("roleType" , (String)params[2]);

            Result<BDRegisterData> result = bPost(url , paramMap);
            if (result.isSuccess()){
                BDRegisterData retData = JSON.parseObject(result.getMessage() , BDRegisterData.class);
                result.setMessage(retData.getMessage());
                if(retData.codeOk()){
                    result.setValue(retData.getResult());
                }else{
                    result.setSuccess(false);
                }
            }
            return result;
        }
    }
}
