package com.jopool.crow.imlib.task;

/**
 * Created by xuan on 16/8/22.
 */
public class BaseRes<T> {
    private int code;
    private long serverTime;
    private String message;
    private T result;

    public boolean isOk() {
        return code == 1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
