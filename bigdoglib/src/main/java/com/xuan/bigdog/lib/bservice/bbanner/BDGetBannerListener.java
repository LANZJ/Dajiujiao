package com.xuan.bigdog.lib.bservice.bbanner;

/**轮播图回调
 * Created by wuhk on 2016/3/14.
 */
public interface BDGetBannerListener {
    void success(BDGetBannerData bdGetBannerData);

    void fail(String message);
}
