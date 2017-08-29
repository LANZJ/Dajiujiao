package com.zjyeshi.dajiujiao.buyer.entity.enums.work;

/**
 * 请假类型
 * Created by wuhk on 2016/6/20.
 */
public enum LeaveTypeEnum {
    //1、年假 ， 2、事假 3、病假 4、调休假 5、婚假 6、产假 7、其他 -1 未知
    NIANJIA(1) , SHIJIA(2) , BINGJIA(3) , TIAOXIUJIA(4) , HUNJIA(5) , CHANJIA(6) ,QITA(7) , UNKNOW(-1);

    private int value;

    public int getValue() {
        return value;
    }

    LeaveTypeEnum(int value) {
        this.value = value;
    }

    public static LeaveTypeEnum valueOf(int status) {
        switch (status){
            case 1:
                return NIANJIA;
            case 2:
                return SHIJIA;
            case 3:
                return BINGJIA;
            case 4:
                return TIAOXIUJIA;
            case 5:
                return HUNJIA;
            case 6:
                return CHANJIA;
            case 7:
                return QITA;
            default:
                return UNKNOW;
        }
    }


    public static LeaveTypeEnum stringOf(String content) {
        switch (content){
            case "年假":
                return NIANJIA;
            case "事假":
                return SHIJIA;
            case "病假":
                return BINGJIA;
            case "调休假":
                return TIAOXIUJIA;
            case "婚假":
                return HUNJIA;
            case "产假":
                return CHANJIA;
            case "其他":
                return QITA;
            default:
                return UNKNOW;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case NIANJIA:
                return "年假";
            case SHIJIA:
                return "事假";
            case BINGJIA:
                return "病假";
            case TIAOXIUJIA:
                return "调休假";
            case HUNJIA:
                return "婚假";
            case CHANJIA:
                return "产假";
            case QITA:
                return "其他";
            default:
                return "未知";
        }
    };

    public boolean equals(LeaveTypeEnum leaveTypeEnum) {
        if (null == leaveTypeEnum) {
            return false;
        }

        return value == leaveTypeEnum.value;
    }
}
