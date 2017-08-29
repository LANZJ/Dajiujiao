package com.zjyeshi.dajiujiao.buyer.task.data.store.homepage;

import java.io.Serializable;

/**
 * Created by wuhk on 2015/10/26.
 */
public class ShopActivity implements Serializable{
    private String id;
    private String title;
    private String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
