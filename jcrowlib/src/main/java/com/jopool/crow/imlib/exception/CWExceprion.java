package com.jopool.crow.imlib.exception;

/**
 * CW异常处理
 *
 * Created by xuan on 15/11/2.
 */
public class CWExceprion extends Exception{

    public CWExceprion() {
        super();
    }

    public CWExceprion(String detailMessage) {
        super(detailMessage);
    }

    public CWExceprion(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CWExceprion(Throwable throwable) {
        super(throwable);
    }

}
