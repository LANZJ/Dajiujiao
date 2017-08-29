package com.xuan.bigdog.lib;


import android.content.Context;

/**
 * 全局类
 * 
 * @author xuan
 */
public class BigDog {
    private static BigDog instance;

    private Context application;

    private BigDog(){

    }

    public static BigDog getInstance(){
        if(null == instance){

            instance = new BigDog();
        }
        return instance;
    }

    public void init(Context application){
        this.application = application;
    }

    public Context getApplication() {
        return application;
    }
}
