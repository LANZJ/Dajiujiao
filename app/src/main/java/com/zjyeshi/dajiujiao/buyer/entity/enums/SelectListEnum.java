package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 购物车全选类型
 *
 * Created by wuhk on 2015/10/21.
 */
public enum SelectListEnum  {
    // 1、list 2、item 3、未知
    LIST(1), ITEM(2) ,UNKNOW(3);

    private int value;

    private SelectListEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static SelectListEnum valueOf(int selectType) {
        switch (selectType){
            case 1:
                return LIST;
            case 2:
                return ITEM;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LIST:
                return "list";
            case ITEM:
                return "item";
            default:
                return "未知";
        }
    };

    public boolean equals(SelectListEnum selectListEnum) {
        if (null == selectListEnum) {
            return false;
        }

        return value == selectListEnum.value;
    }
}
