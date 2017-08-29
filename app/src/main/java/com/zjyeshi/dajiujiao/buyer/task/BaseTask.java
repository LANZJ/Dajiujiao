package com.zjyeshi.dajiujiao.buyer.task;

import android.content.Context;

import com.squareup.okhttp.Response;
import com.xuan.bigapple.lib.asynctask.AbstractTask;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskResultNullCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.http.BPResponse;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigapple.lib.utils.log.LogUtils;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;
import com.zjyeshi.dajiujiao.buyer.common.UrlConstants;
import com.zjyeshi.dajiujiao.buyer.receiver.ToLogoutReceiver;
import com.zjyeshi.dajiujiao.buyer.utils.HttpUtil;
import com.zjyeshi.dajiujiao.buyer.utils.JsonUtil;
import com.zjyeshi.dajiujiao.buyer.utils.okhttp.OkHttpClientManager;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * 所有Http请求任务类的基类
 *
 * @author wuhk
 */
public abstract class BaseTask<T> extends AbstractTask<T> {
    public BaseTask(Context context) {
        super(context);
        initTask();
    }

    // 初始化一些自定义的异步加载属性
    private void initTask() {
        setProgressDialog(new DGProgressDialog(context, getProgressTip()));
        // 提示进度条
        setAsyncTaskResultNullCallback(new AsyncTaskResultNullCallback() {
            @Override
            public void resultNullCallback() {
                if (!ContextUtils.hasNetwork()) {
                    ToastUtils.displayTextShort("亲~ 没有网络连接");
                }
            }
        });
    }

    @Override
    protected Result<T> doHttpRequest(Object... params) {
        // 访问前先判断是否有网络
        if (!ContextUtils.hasNetwork()) {
            return null;
        }

        // 真正的网络操作
        return onHttpRequest(params);

    }


    /**
     * post提交(无通用参数)
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> post(String url, Map<String, String> postParam) {
        return post(url, postParam, false);
    }

    /**
     * post提交(无通用参数)
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> post(String url, Map<String, String> postParam, boolean compelete) {
        String completeUrl = "";
        if (compelete) {
            completeUrl = url;
        } else {
            completeUrl = UrlConstants.WEB_SITE + url;
        }

        BPResponse response = HttpUtil.post(context, completeUrl, postParam);
        Result<T> result = new Result<T>();
        if (-1 == response.getStatusCode()) {
            result.setSuccess(false);
            result.setMessage(response.getReasonPhrase());
        } else if (HttpURLConnection.HTTP_OK == response.getStatusCode()) {
            // 正常200
            result.setSuccess(true);
            result.setMessage(response.getResultStr());
        } else {
            // 非200的状态码
            result.setSuccess(false);
            result.setMessage("返回状态码错误" + response.toString());
        }
        return result;
    }

    /**
     * post提交(无通用参数)
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> postBPush(String url, Map<String, String> postParam) {
        String completeUrl = UrlConstants.JCROW_WEB_SITE + url;
        BPResponse response = HttpUtil.post(context, completeUrl, postParam);
        Result<T> result = new Result<T>();
        if (-1 == response.getStatusCode()) {
            result.setSuccess(false);
            result.setMessage(response.getReasonPhrase());
        } else if (HttpURLConnection.HTTP_OK == response.getStatusCode()) {
            // 正常200
            result.setSuccess(true);
            result.setMessage(response.getResultStr());
        } else {
            // 非200的状态码
            result.setSuccess(false);
            result.setMessage("返回状态码错误" + response.toString());
        }
        return result;
    }

    /**
     * post提交(包含通用参数)
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> postCommon(String url, Map<String, String> postParam) {
        String completeUrl = UrlConstants.WEB_SITE + url;
        BPResponse response = HttpUtil.postCommom(context, completeUrl, postParam);
        Result<T> result = new Result<T>();
        if (-1 == response.getStatusCode()) {
            result.setSuccess(false);
            result.setMessage(response.getReasonPhrase());
        } else if (HttpURLConnection.HTTP_OK == response.getStatusCode()) {
            // 正常200
            result.setSuccess(true);
            result.setMessage(response.getResultStr());
            //判断退出登录
            judgeLogOut(response, result);
        } else {
            // 非200的状态码
            result.setSuccess(false);
            result.setMessage("返回状态码错误" + response.toString());
        }
        return result;
    }

    /**
     * 支付post
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> payPost(String url, Map<String, String> postParam) {
        String completeUrl = UrlConstants.PAY_WEB_SITE + url;
        BPResponse response = HttpUtil.postCommom(context, completeUrl, postParam);
        Result<T> result = new Result<T>();
        if (-1 == response.getStatusCode()) {
            result.setSuccess(false);
            result.setMessage(response.getReasonPhrase());
        } else if (HttpURLConnection.HTTP_OK == response.getStatusCode()) {
            // 正常200
            result.setSuccess(true);
            result.setMessage(response.getResultStr());
            //判断退出登录
            judgeLogOut(response, result);
        } else {
            // 非200的状态码
            result.setSuccess(false);
            result.setMessage("返回状态码错误" + response.toString());
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param url
     * @param filePathName
     * @return
     */
    protected Result<T> upLoadPost(String url, String filePathName) {
        Result<T> result = new Result<T>();
        File files = new File(filePathName);
        String fileKeys = "file";
        try {
            Response response = OkHttpClientManager.getInstance().getUploadDelegate().post(url, fileKeys, files);
            if (response.isSuccessful()) {
                result.setSuccess(true);
                try {
                    result.setMessage(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                result.setSuccess(false);
                result.setMessage(response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加载中的提示语
     *
     * @return
     */
    protected String getProgressTip() {
        return "请稍后...";
    }

    protected abstract Result<T> onHttpRequest(Object... params);

    /**
     * 退出登录
     *
     * @param response
     * @param result
     */
    private void judgeLogOut(BPResponse response, Result<T> result) {
        try {
            org.json.JSONObject retObj = new org.json.JSONObject(
                    response.getResultStr());
            String code = JsonUtil.getString(retObj, "code");

            if ("7".equals(code)) {
                result.setSuccess(false);
                result.setMessage("您的账号再另一地点登录，请重新登录");
                //发起退出登录广播
                LogUtils.e(context.getPackageName() + "发出退出登录广播");
                ToLogoutReceiver.notifyReceiver();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }
    }

}
