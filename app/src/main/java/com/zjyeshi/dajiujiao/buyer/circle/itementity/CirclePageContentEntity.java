package com.zjyeshi.dajiujiao.buyer.circle.itementity;

/**
 * 分享的链接地址
 *
 * Created by xuan on 15/10/19.
 */
public class CirclePageContentEntity extends CircleContentEntity {
    private String pageImage;//网页图标
    private String pageContent;//网页内容
    private String pageUrl;//网页访问地址

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getPageImage() {
        return pageImage;
    }

    public void setPageImage(String pageImage) {
        this.pageImage = pageImage;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }
}
