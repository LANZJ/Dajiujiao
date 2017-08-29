package com.zjyeshi.dajiujiao.buyer.task.data.store.sort;

/**
 * 分类信息
 *
 * Created by wuhk on 2015/10/28.
 */
public class Category {
    private String id ;
    private String name ;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
