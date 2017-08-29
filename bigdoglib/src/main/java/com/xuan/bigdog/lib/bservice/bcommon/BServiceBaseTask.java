package com.xuan.bigdog.lib.bservice.bcommon;

import android.app.ProgressDialog;
import android.content.Context;

import com.xuan.bigapple.lib.asynctask.AbstractTask;
import com.xuan.bigapple.lib.asynctask.callback.AsyncTaskResultNullCallback;
import com.xuan.bigapple.lib.asynctask.helper.Result;
import com.xuan.bigapple.lib.http.BPHttpUtils;
import com.xuan.bigapple.lib.http.BPRequest;
import com.xuan.bigapple.lib.http.BPResponse;
import com.xuan.bigapple.lib.utils.ContextUtils;
import com.xuan.bigapple.lib.utils.ToastUtils;
import com.xuan.bigdog.lib.dialog.DGProgressDialog;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuhk on 2016/2/23.
 */
public abstract class BServiceBaseTask<T> extends AbstractTask<T> {
    public BServiceBaseTask(Context context) {
        super(context);
        initTask();
    }

    // 初始化一些自定义的异步加载属性
    private void initTask() {

        setProgressDialog(new DGProgressDialog(context, getProgressTip()));

        setAsyncTaskResultNullCallback(new AsyncTaskResultNullCallback() {
            @Override
            public void resultNullCallback() {
                if (!ContextUtils.hasNetwork()) {
                    ToastUtils.displayTextShort("亲~ 没有网络连接");
                }
            }
        });
    }


    /**
     * post提交(无通用参数)
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> bPost(String url, Map<String, String> postParam) {
        BPResponse response = BPHttpUtils.post(url, postParam);
        Result<T> result = new Result<T>();
        if (-1 == response.getStatusCode()) {
            result.setSuccess(false);
            result.setMessage(response.getReasonPhrase());
        } else if (HttpStatus.SC_OK == response.getStatusCode()) {
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

    /**上传文件
     *
     * @param url
     * @param postParam
     * @return
     */
    protected Result<T> bUpload(String url, Map<String, String> postParam) {
        File file = new File(postParam.get("file"));
        HashMap<String , File> fileHashMap = new HashMap<String, File>();
        fileHashMap.put("file" , file);
        BPResponse response = BPHttpUtils.upload(url, fileHashMap , null);
        Result<T> result = new Result<T>();
        if (-1 == response.getStatusCode()) {
            result.setSuccess(false);
            result.setMessage(response.getReasonPhrase());
        } else if (HttpStatus.SC_OK == response.getStatusCode()) {
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

    @Override
    protected Result<T> doHttpRequest(Object... objects) {
        // 访问前先判断是否有网络
        if (!ContextUtils.hasNetwork()) {
            return null;
        }
        // 真正的网络操作
        return onHttpRequest(objects);
    }

    protected abstract Result<T> onHttpRequest(Object... params);

    /**
     * 加载中的提示语
     *
     * @return
     */
    protected String getProgressTip() {
        return "请稍后...";
    }

}
