package com.jopool.jsharelib.config;

import android.content.Context;

/**
 * 分享配置
 * Created by wuhk on 2016/3/11.
 */
public class JShareConfig {
    private Context context;
    private String title;//title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
    private String titleUrl;//titleUrl是标题的网络链接，仅在人人网和QQ空间使用
    private String text;//分享文本，所有平台都需要这个字段
    private String imagePath;//imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
    private String url;//url仅在微信（包括好友和朋友圈）中使用
    private String comment;//comment是我对这条分享的评论，仅在人人网和QQ空间使用
    private String site;//site是分享此内容的网站名称，仅在QQ空间使用
    private String siteUrl;//siteUrl是分享此内容的网站地址，仅在QQ空间使用
    private boolean haveNativePic;//是否有本地图片

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public boolean isHaveNativePic() {
        return haveNativePic;
    }

    public void setHaveNativePic(boolean haveNativePic) {
        this.haveNativePic = haveNativePic;
    }
}
