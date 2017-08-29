package com.xuan.bigdog.lib.bservice.bcommon;

/**
 * Created by wuhk on 2016/2/24.
 */
public class BServiceBaseData<T> {
    private int code;// 返回接口， 1表示成功
    private long serverTime; // 服务器返回时间
    private String message;// 提示信息
    private T result;// 业务数据

    /**
     * 判断是否成功
     *
     * @return
     */
    public boolean codeOk() {
        return 1 == code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
