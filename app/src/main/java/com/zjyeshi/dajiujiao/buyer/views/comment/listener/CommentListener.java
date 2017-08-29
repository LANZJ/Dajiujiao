package com.zjyeshi.dajiujiao.buyer.views.comment.listener;

/**
 * 评论回调
 * Created by wuhk on 2016/8/9.
 */
public interface CommentListener {

    /**发送文字评论
     *
     */
    void send(String text , String pics , Voice voice);


    class Voice{
        String voiceUrl;
        String voiceLength;


        public Voice() {
            this.voiceUrl = "";
            this.voiceLength = "";
        }

        public Voice(String voiceUrl, String voiceLength) {
            this.voiceUrl = voiceUrl;
            this.voiceLength = voiceLength;
        }

        public String getVoiceUrl() {
            return voiceUrl;
        }

        public void setVoiceUrl(String voiceUrl) {
            this.voiceUrl = voiceUrl;
        }

        public String getVoiceLength() {
            return voiceLength;
        }

        public void setVoiceLength(String voiceLength) {
            this.voiceLength = voiceLength;
        }
    }

}
