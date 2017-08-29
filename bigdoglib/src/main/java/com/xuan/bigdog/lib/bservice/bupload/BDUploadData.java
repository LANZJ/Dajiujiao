package com.xuan.bigdog.lib.bservice.bupload;


import com.xuan.bigdog.lib.bservice.bcommon.BServiceBaseData;

/**
 * Created by wuhk on 2016/2/24.
 */
public class BDUploadData extends BServiceBaseData<BDUploadData> {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
