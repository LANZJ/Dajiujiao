package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 商品是否选中
 * Created by wuhk on 2015/11/29.
 */
public enum SelectEnum {
    //1、未选中  2、选中 100、未知
    UNSELECT(1) , SELECTED(2) , UNKNOW(100);

    private int value;

    SelectEnum(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static SelectEnum valueOf(int type) {
        switch (type){
            case 1:
                return UNSELECT;
            case 2:
                return SELECTED;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case UNSELECT:
                return "未选中";
            case SELECTED:
                return "选中";
            default:
                return "未知";
        }
    };

    public boolean equals(SelectEnum selectEnum) {
        if (null == selectEnum) {
            return false;
        }

        return value == selectEnum.value;
    }
}
