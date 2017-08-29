package com.jopool.jsharelib;

import com.jopool.jsharelib.config.JShareConfig;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 分享
 * Created by wuhk on 2016/3/11.
 */
public class JShare  {
    private static JShare instance;


    /**获取单例
     *
     * @return
     */
    public static JShare getInstance(){
        if (null == instance){
            instance = new JShare();
        }
        return instance;
    }

    /**分享
     *
     * @param config
     */
    public void share(JShareConfig config){
        ShareSDK.initSDK(config.getContext());
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(config.getTitle());

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(config.getTitleUrl());

        // text是分享文本，所有平台都需要这个字段
        oks.setText(config.getText());

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (config.isHaveNativePic()){
            oks.setImagePath(config.getImagePath());//确保SDcard下面存在此张图片
        }

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(config.getUrl());

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(config.getComment());

        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(config.getSite());

        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(config.getSiteUrl());

        // 启动分享GUI
        oks.show(config.getContext());
    }
}
