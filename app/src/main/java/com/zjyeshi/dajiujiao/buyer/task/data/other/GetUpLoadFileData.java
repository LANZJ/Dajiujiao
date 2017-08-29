package com.zjyeshi.dajiujiao.buyer.task.data.other;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * 上传文件
 * Created by wuhk on 2015/11/17.
 */
public class GetUpLoadFileData extends BaseData<GetUpLoadFileData> {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
