package com.xuan.bigdog.lib.bservice.blogin;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskFailCallback;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskSuccessCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.utils.Validators;
import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseTask;

import java.util.HashMap;

/**
 * 登录模块
 * <p/>
 * Created by xuan on 16/2/16.
 */
public class BDLoginModel {
    private static BDLoginModel instance;

    private BDLoginModel() {

    }

    public static BDLoginModel getInstance() {
        if (null == instance) {
            instance = new BDLoginModel();
        }
        return instance;
    }

    /**
     * 登录操作
     *
     * @param website
     * @param username
     * @param password
     * @param ext
     * @param l
     */
    public void doLogin(Context context,String website, String username, String password, String roleType , String ext, final BDLoginListener l) {
        String os = "ANDROID";
        String url = website + "common/passport/login.htm";

        BDLoginTask bdLoginTask = new BDLoginTask(context , url);
        bdLoginTask.setAsyncTaskSuccessCallback(new AsyncTaskSuccessCallback<BDLoginUser>() {
            @Override
            public void successCallback(Result<BDLoginUser> result) {
                l.success(result.getValue());
            }
        });

        bdLoginTask.setAsyncTaskFailCallback(new AsyncTaskFailCallback<BDLoginUser>() {
            @Override
            public void failCallback(Result<BDLoginUser> result) {
                l.fail(result.getMessage());
            }
        });

        bdLoginTask.execute(username , password , roleType , os , ext);
    }

    /**
     * 登录操作
     *
     * @param website
     * @param username
     * @param password
     * @param l
     */
    public void doLogin(Context context,String website, String username, String password,String roleType , BDLoginListener l) {
        doLogin(context,website,username, password, roleType ,null, l);
    }

    /**登录请求Task
     *
     */
    private class BDLoginTask extends BServiceBaseTask<BDLoginUser> {
        private String url;
        public BDLoginTask(Context context , String url) {
            super(context);
            this.url = url;
        }

        @Override
        protected Result<BDLoginUser> onHttpRequest(Object... params) {
            HashMap<String , String> paramMap = new HashMap<String , String>();
            paramMap.put("userName" , (String)params[0]);
            paramMap.put("password" , (String)params[1]);
            paramMap.put("roleType" , (String)params[2]);
            paramMap.put("os" , (String)params[3]);
            String ext = (String)params[4];
            if (!Validators.isEmpty(ext)){
                paramMap.put("ext" , (String)params[4]);
            }

            Result<BDLoginUser> result = bPost(url , paramMap);
            if (result.isSuccess()){
                BDLoginUser retData = JSON.parseObject(result.getMessage(), BDLoginUser.class);
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
}

