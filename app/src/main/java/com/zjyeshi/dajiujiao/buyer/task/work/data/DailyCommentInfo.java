package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.List;

/**
 * 日报评价列表
 * Created by wuhk on 2016/7/18.
 */
public class DailyCommentInfo extends BaseData<DailyCommentInfo> {

    private List<CommentInfo> list;

    public List<CommentInfo> getList() {
        return list;
    }

    public void setList(List<CommentInfo> list) {
        this.list = list;
    }

    public static class CommentInfo{
        private String id;
        private String dailyId;
        private String content;
        private String dailyCommentId;
        private String pics;
        private String voice;
        private String voiceLength;
        private String toMemberId;
        private String memberId;
        private String memberName;
        private long creationTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDailyId() {
            return dailyId;
        }

        public void setDailyId(String dailyId) {
            this.dailyId = dailyId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDailyCommentId() {
            return dailyCommentId;
        }

        public void setDailyCommentId(String dailyCommentId) {
            this.dailyCommentId = dailyCommentId;
        }

        public String getToMemberId() {
            return toMemberId;
        }

        public void setToMemberId(String toMemberId) {
            this.toMemberId = toMemberId;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(long creationTime) {
            this.creationTime = creationTime;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getVoice() {
            return voice;
        }

        public void setVoice(String voice) {
            this.voice = voice;
        }

        public String getVoiceLength() {
            return voiceLength;
        }

        public void setVoiceLength(String voiceLength) {
            this.voiceLength = voiceLength;
        }
    }
}
