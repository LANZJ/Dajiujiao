package com.zjyeshi.dajiujiao.buyer.entity.enums.work;

/**费用类型
 *
 * Created by wuhk on 2016/6/22.
 */
public enum  CostEnum {
    //1、餐饮 ，2、交通 、 3、住宿 ，4、特殊费用 5、其他 ， 6、市场费用,  7、 样品 , -1未知
    CANYIN(1) , JIAOTONG(2) ,ZHUSU(3) , KEHUHD(4) , QITA(5)  , SHICHANG(6) , YANGPING(7) , UNKNOW(-1);

    private int value;

    public int getValue() {
        return value;
    }

    CostEnum(int value) {
        this.value = value;
    }

    public static CostEnum valueOf(int status) {
        switch (status){
            case 1:
                return CANYIN;
            case 2:
                return JIAOTONG;
            case 3:
                return ZHUSU;
            case 4:
                return KEHUHD;
            case 5:
                return QITA;
            case 6:
                return SHICHANG;
            case 7:
                return YANGPING;
            default:
                return UNKNOW;
        }
    }

    public static CostEnum stringOf(String content) {
        switch (content){
            case "餐饮费":
                return CANYIN;
            case "交通费":
                return JIAOTONG;
            case "住宿费":
                return ZHUSU;
            case "特殊费用":
                return KEHUHD;
            case "其他":
                return QITA;
            case "市场费用":
                return SHICHANG;
            case "样品":
                return YANGPING;
            default:
                return UNKNOW;
        }
    }
    @Override
    public String toString() {
        switch (this) {
            case CANYIN:
                return "餐饮费";
            case JIAOTONG:
                return "交通费";
            case ZHUSU:
                return "住宿费";
            case KEHUHD:
                return "特殊费用";
            case QITA:
                return "其他";
            case SHICHANG:
                return "市场费用";
            case YANGPING:
                return "样品";
            default:
                return "未知";
        }
    };

    public boolean equals(CostEnum costEnum) {
        if (null == costEnum) {
            return false;
        }

        return value == costEnum.value;
    }


}
