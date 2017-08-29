package com.zjyeshi.dajiujiao.buyer.circle.task.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

import java.util.Date;
import java.util.List;

/**
 * HTTP请求返回数据
 *
 * Created by xuan on 15/11/5.
 */
public class CircleData extends BaseData<CircleData>{
    private List<Circle> list;

    public List<Circle> getList() {
        return list;
    }

    public void setList(List<Circle> list) {
        this.list = list;
    }

    /**
     * 一条圈子对象
     */
    public static class Circle{
        private String id;
        private String pics;
        private String content;
        private int linkType;
        private String linkPic;
        private String linkTitle;
        private String linkContent;
        private Date creationTime;
        private Member member;
        private List<Praise> praises;
        private List<Evaluate> evaluates;

        //dto
        private int state;
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Date creationTime) {
            this.creationTime = creationTime;
        }

        public List<Evaluate> getEvaluates() {
            return evaluates;
        }

        public void setEvaluates(List<Evaluate> evaluates) {
            this.evaluates = evaluates;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Member getMember() {
            return member;
        }

        public void setMember(Member member) {
            this.member = member;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public List<Praise> getPraises() {
            return praises;
        }

        public void setPraises(List<Praise> praises) {
            this.praises = praises;
        }

        public int getLinkType() {
            return linkType;
        }

        public void setLinkType(int linkType) {
            this.linkType = linkType;
        }

        public String getLinkPic() {
            return linkPic;
        }

        public void setLinkPic(String linkPic) {
            this.linkPic = linkPic;
        }

        public String getLinkTitle() {
            return linkTitle;
        }

        public void setLinkTitle(String linkTitle) {
            this.linkTitle = linkTitle;
        }

        public String getLinkContent() {
            return linkContent;
        }

        public void setLinkContent(String linkContent) {
            this.linkContent = linkContent;
        }
    }

//    /**
//     * 评论
//     */
//    public class Evaluate{
//        private String id;
//        private String content;
//        private Date creationTime;
//        private Member member;//评论的人
//        private Member memberup;//评论评论的人
//
//        public String getContent() {
//            return content;
//        }
//
//        public void setContent(String content) {
//            this.content = content;
//        }
//
//        public Date getCreationTime() {
//            return creationTime;
//        }
//
//        public void setCreationTime(Date creationTime) {
//            this.creationTime = creationTime;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public Member getMember() {
//            return member;
//        }
//
//        public void setMember(Member member) {
//            this.member = member;
//        }
//
//        public Member getMemberup() {
//            return memberup;
//        }
//
//        public void setMemberup(Member memberup) {
//            this.memberup = memberup;
//        }
//    }
//
//    /**
//     * 赞
//     */
//    public class Praise{
//        private String id;
//        private Member member;
//        private Date creationTime;
//
//        public Date getCreationTime() {
//            return creationTime;
//        }
//
//        public void setCreationTime(Date creationTime) {
//            this.creationTime = creationTime;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public Member getMember() {
//            return member;
//        }
//
//        public void setMember(Member member) {
//            this.member = member;
//        }
//    }
//
//    /**
//     * 成员
//     */
//    public class Member{
//        private String id;
//        private String name;
//        private String pic;//用户头像
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getPic() {
//            return pic;
//        }
//
//        public void setPic(String pic) {
//            this.pic = pic;
//        }
//    }

}
