package com.jopool.crow.imapi;

/**
 * 提供给外部使用者调用的接口,单例模式
 * <p/>
 * Created by xuan on 16/11/7.
 */
public class CWApi {
    private static CWApi instance;
    private CWApiDb apiDb = new CWApiDb();
    private CWApiStart apiStart = new CWApiStart();
    private CWApiUi apiUi = new CWApiUi();
    private CWApiUser apiUser = new CWApiUser();
    private CWApiMessage apiMessage = new CWApiMessage();
    private CWApiGroup apiGroup = new CWApiGroup();

    private CWApi() {
    }

    /**
     * 获取CWApi实例
     *
     * @return
     */
    public static CWApi getInstance() {
        if (null == instance) {
            instance = new CWApi();
        }
        return instance;
    }

    public CWApiDb getApiDb() {
        return apiDb;
    }

    public void setApiDb(CWApiDb apiDb) {
        this.apiDb = apiDb;
    }

    public CWApiGroup getApiGroup() {
        return apiGroup;
    }

    public void setApiGroup(CWApiGroup apiGroup) {
        this.apiGroup = apiGroup;
    }

    public CWApiMessage getApiMessage() {
        return apiMessage;
    }

    public void setApiMessage(CWApiMessage apiMessage) {
        this.apiMessage = apiMessage;
    }

    public CWApiStart getApiStart() {
        return apiStart;
    }

    public void setApiStart(CWApiStart apiStart) {
        this.apiStart = apiStart;
    }

    public CWApiUi getApiUi() {
        return apiUi;
    }

    public void setApiUi(CWApiUi apiUi) {
        this.apiUi = apiUi;
    }

    public CWApiUser getApiUser() {
        return apiUser;
    }

    public void setApiUser(CWApiUser apiUser) {
        this.apiUser = apiUser;
    }
}
