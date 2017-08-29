package com.zjyeshi.dajiujiao.buyer.circle.itementity;

import android.support.v4.app.Fragment;

import com.zjyeshi.dajiujiao.buyer.model.UploadCircleBgModel;
import com.zjyeshi.dajiujiao.buyer.model.UploadHeadModel;

/**
 * 头像
 *
 * Created by xuan on 15/10/18.
 */
public class TopHeadEntity extends BaseEntity {
    private String id;//用户id
    private String userName;//姓名
    private String headUrl;//头像地址
    private String headBgUrl;//头像背景地址
    private UploadHeadModel uploadHeadModel;
    private UploadCircleBgModel uploadCircleBgModel;
    private Fragment fragment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHeadBgUrl() {
        return headBgUrl;
    }

    public void setHeadBgUrl(String headBgUrl) {
        this.headBgUrl = headBgUrl;
    }

    public UploadHeadModel getUploadHeadModel() {
        return uploadHeadModel;
    }

    public void setUploadHeadModel(UploadHeadModel uploadHeadModel) {
        this.uploadHeadModel = uploadHeadModel;
    }

    public UploadCircleBgModel getUploadCircleBgModel() {
        return uploadCircleBgModel;
    }

    public void setUploadCircleBgModel(UploadCircleBgModel uploadCircleBgModel) {
        this.uploadCircleBgModel = uploadCircleBgModel;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
