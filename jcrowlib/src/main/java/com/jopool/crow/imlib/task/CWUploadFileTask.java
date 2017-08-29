package com.jopool.crow.imlib.task;

import android.content.Context;

import com.jopool.crow.CWChat;
import com.jopool.crow.imlib.constants.UrlConstants;
import com.jopool.crow.imlib.utils.CWHttpUtil;
import com.jopool.crow.imlib.utils.asynctask.NetAbstractTask;
import com.jopool.crow.imlib.utils.asynctask.helper.Result;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传文件
 *
 * @author xuan
 */
public class CWUploadFileTask extends NetAbstractTask<String> {
    public CWUploadFileTask(Context context) {
        super(context);
        setShowProgressDialog(false);
    }

    @Override
    protected Result<String> onHttpRequest(Object... params) {
        String type = (String) params[0];
        String fileName = (String) params[1];

        Result<String> result = null;
        CWHttpUtil.CWResponse response = null;

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("type", type);

        Map<String, File> fileMap = new HashMap<String, File>();
        fileMap.put("file", new File(fileName));

        try {
            response = CWHttpUtil.uploadFile(CWChat.getInstance().getConfig().getApiPrefix() + UrlConstants.FILE_UPLOAD, paramMap, fileMap);
            if (200 == response.getStatusCode()) {
                String retStr = response.getResultStr();
                JSONObject retObj = new JSONObject(retStr);
                int code = retObj.getInt("code");
                if (1 == code) {
                    JSONObject resultObj = retObj.getJSONObject("result");
                    result = new Result<String>(true, "上传成功");
                    result.setValue(resultObj.getString("path"));
                } else {
                            result = new Result<String>(false,
                            retObj.getString("message"));
                }
            } else {
                // 响应非200
                result = new Result<String>(false, response.getReasonPhrase());
            }
        } catch (Exception e) {
            result = new Result<String>(false, e.getMessage());
        }

        return result;
    }

}
