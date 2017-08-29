package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * Created by wuhk on 2016/12/15.
 */
public enum ShopModifyEnum {
    // 1、修改店铺名称，2、修改起送价格 3、修改市场支持费用 4、修改营业时间, 5、修改活动网址 , 99、未知
    MODIFYSHOPNAME(1), MODIFYDELIVERYPRICE(2), MODIFYMARKETCOST(3), MODIFYSELLINGTIME(4), MODIFYSALESURL(5) , UNKNOW(99);


    private int value;

    private ShopModifyEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public static ShopModifyEnum valueOf(int type) {
        switch (type) {
            case 1:
                return MODIFYSHOPNAME;
            case 2:
                return MODIFYDELIVERYPRICE;
            case 3:
                return MODIFYMARKETCOST;
            case 4:
                return MODIFYSELLINGTIME;
            case 5:
                return MODIFYSALESURL;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case MODIFYSHOPNAME:
                return "修改店铺名称";
            case MODIFYDELIVERYPRICE:
                return "修改起送价格";
            case MODIFYMARKETCOST:
                return "修改市场支持费用";
            case MODIFYSELLINGTIME:
                return "修改营业时间";
            case MODIFYSALESURL:
                return "修改活动网址";
            default:
                return "未知";
        }
    }

    ;

    public boolean equals(ShopModifyEnum shopModifyEnum) {
        if (null == shopModifyEnum) {
            return false;
        }

        return value == shopModifyEnum.value;
    }
}
