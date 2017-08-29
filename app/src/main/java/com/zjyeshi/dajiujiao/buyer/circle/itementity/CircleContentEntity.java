package com.zjyeshi.dajiujiao.buyer.circle.itementity;

import com.zjyeshi.dajiujiao.buyer.circle.task.data.Evaluate;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Member;
import com.zjyeshi.dajiujiao.buyer.circle.task.data.Praise;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 图文，网页实体
 * <p/>
 * Created by xuan on 15/10/21.
 */
public class CircleContentEntity extends BaseEntity implements  Serializable{
    private String id;//动态id
    private String content;//发表文字
    private Date time;//发表时间
    private Member member;//发布人
    private String[] imageUrls;//图片数组地址
    private String pageImage;//网页图标
    private String pageContent;//网页内容
    private String pageUrl;//网页访问地址
    private int pageType;//连接类型

    private List<Praise> praiseList;//赞列表
    private List<Evaluate> evaluateList;//评论列表

    public List<Praise> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<Praise> praiseList) {
        this.praiseList = praiseList;
    }

    public List<Evaluate> getEvaluateList() {
        return evaluateList;
    }

    public void setEvaluateList(List<Evaluate> evaluateList) {
        this.evaluateList = evaluateList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getPageImage() {
        return pageImage;
    }

    public void setPageImage(String pageImage) {
        this.pageImage = pageImage;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }
}
