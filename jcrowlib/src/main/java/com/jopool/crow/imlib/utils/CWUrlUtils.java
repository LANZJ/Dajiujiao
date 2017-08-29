package com.jopool.crow.imlib.utils;

/**
 * Created by xuan on 16/8/16.
 */
public abstract class CWUrlUtils {
    /**
     * 判断是否是可访问HTTP地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (!CWValidator.isEmpty(str)) {
            if (str.startsWith("http://")) {
                return true;
            }
            if (str.startsWith("https://")) {
                return true;
            }
            //还有很多,主流的弄几个就好
            if (str.indexOf(".com") > 0) {
                return true;
            }
            if (str.indexOf(".cn") > 0) {
                return true;
            }
            if (str.indexOf(".net") > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 矫正HTTP地址
     *
     * @param url
     * @return
     */
    public static String correctUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

}
