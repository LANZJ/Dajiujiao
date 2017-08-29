package com.zjyeshi.dajiujiao.buyer.entity.enums;

/**
 * 订单状态枚举类
 *
 * Created by wuhk on 2015/11/4.
 */
public enum OrderStatusEnum {
    //1、待付款  2、已付款(线上/线下) ,3、已确认 5、已发货 ，6、待评价 7、已收货、未评价 ，8、已评价、已评价
    // ,9、已取消 ， -1、全部  , 12、待退货 , 13 、退货成功 14、退货失败 ,  100、未知
    WAITPAY(1), PAYED(2)  , SURED(3) , SENDED(5) ,WAITCOMMENT(6) ,ALREADYGET(7) ,ALREADYCOMMENT(8)
    ,CLOSED(9) ,  ALL(-1),  WAITRETURN(12) , RETURNSUCCESS(13) , RETURNFAILED(14)
    , NEW_TOBEREBACKED(15) , NEW_REBACK_SUCCESS(16) , NEW_REBACK_FAIL(17) , NEW_REBACK_CLOSE(18) , UNKNOW(100) ;

    private int value;

    private OrderStatusEnum(int type){
        this.value = type;
    }

    public int getValue(){
        return value;
    }

    public static OrderStatusEnum valueOf(int orderType){
        switch (orderType){
            case 1:
                return WAITPAY;
            case 2:
                return PAYED;
            case 3:
                return SURED;
            case 5:
                return SENDED;
            case 6:
                return WAITCOMMENT;
            case 7:
                return ALREADYGET;
            case 8:
                return ALREADYCOMMENT;
            case 9:
                return CLOSED;
            case 12:
                return WAITRETURN;
            case 13:
                return RETURNSUCCESS;
            case 14:
                return RETURNFAILED;
            case 15:
                return NEW_TOBEREBACKED;
            case 16:
                return NEW_REBACK_SUCCESS;
            case 17:
                return NEW_REBACK_FAIL;
            case 18:
                return NEW_REBACK_CLOSE;
            case -1:
                return ALL;
            default:
                return UNKNOW;
        }
    }


    @Override
    public String toString() {
        switch (this) {
            case WAITPAY:
                return "待付款";
            case PAYED:
                return "已付款";
            case SURED:
                return "已确认";
            case SENDED:
                return "已发货";
            case WAITCOMMENT:
                return "待评价";
            case ALREADYGET:
                return "已收货";
            case ALREADYCOMMENT:
                return "已评价";
            case CLOSED:
                return "已取消";
            case ALL:
                return "全部";
            case WAITRETURN:
                return "待退货";
            case RETURNSUCCESS:
                return "退货成功";
            case RETURNFAILED:
                return "退货失败";
            case NEW_TOBEREBACKED:
                return "待退货";
            case NEW_REBACK_SUCCESS:
                return "退货成功";
            case NEW_REBACK_FAIL:
                return "退货失败";
            case NEW_REBACK_CLOSE:
                return "退货关闭";
            default:
                return "未知";
        }
    }

    public boolean equals(OrderStatusEnum orderStatusEnum) {
        if (null == orderStatusEnum) {
            return false;
        }

        return value == orderStatusEnum.value;
    }
}
