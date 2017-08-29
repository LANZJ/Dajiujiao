package com.zjyeshi.dajiujiao.buyer.utils;

import com.zjyeshi.dajiujiao.buyer.entity.enums.MarketCostEnum;
import com.zjyeshi.dajiujiao.buyer.entity.enums.UserEnum;
import com.zjyeshi.dajiujiao.buyer.entity.LoginedUser;

/**
 * 权限判断
 * Created by wuhk on 2016/7/12.
 */
public abstract class AuthUtil {

    private final static String ssss="4";

    private static LoginedUser getLoginedUser() {
        return LoginedUser.getLoginedUser();
    }

    /**
     * 考勤是否显示请假模块
     *
     * @return
     */
    public static boolean isLeaveShow() {
        //最高权限不显示请假
        if (getLoginedUser().isMaxLeavel()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 考勤是否显示待审批模块
     *
     * @return
     */
    public static boolean isLeaveApproveShow() {
        //最高权限显示
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            //非最高权限，有请假审批权限的显示
            if (getLoginedUser().isLeaveAuth()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 显示设置优惠活动
     *
     * @return
     */
    public static boolean showSalesLayout() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 考勤查看考勤记录是否显示选择员工
     *
     * @return
     */
    public static boolean isLeaveSelPersonShow() {
        //最高权限显示选择员工
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 客户管理是否显示花名册
     *
     * @return
     */
    public static boolean isHmcShow() {
        //终端和终端业务员没有花名册
        if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isTerminalOrSales() {
        //终端和终端业务员没有花名册
        if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 客户管理，点击订单管理是否直接进入点单
     *
     * @return
     */
    public static boolean goOrderDirect() {
        //终端或终端业务员直接进入订单
        if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否显示订单管理的筛选
     *
     * @return
     */
    public static boolean isOrderManagerFilterShow() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 订单管理筛选显示选择下级
     *
     * @return
     */
    public static boolean isOrderFilterTopShow() {

        if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
            //终端和终端业务员直接显示列表
            return false;
        } else {
            return true;
        }
    }

    /**
     * 订单筛选选择下集的描述
     *
     * @return
     */
    public static String subNameDes() {
        if (getLoginedUser().getUserEnum().equals(UserEnum.AGENT) || getLoginedUser().getShopType() == 3) {
            return "选择经销商";
        } else if (getLoginedUser().getUserEnum().equals(UserEnum.DEALER) || getLoginedUser().getShopType() == 2) {
            return "选择终端";
        } else {
            return "";
        }
    }

    /**
     * 库存管理显示客户库存
     *
     * @return
     */
    public static boolean customerStockShow() {
        if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
            //终端或终端业务员不显示客户库存
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否显示费用申请和费用报销模块
     *
     * @return
     */
    public static boolean isApplyAndBxMoneyShow() {
        if (getLoginedUser().isMaxLeavel()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否显示费用的审批
     *
     * @return
     */
    public static boolean isMoneyApproveshow() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            if (!getLoginedUser().isLeaveAuth() && !getLoginedUser().isBxAuth()) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 工作日报是否直接进入筛选
     *
     * @return
     */
    public static boolean dateGoFilterDirect() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 有权限的就不显示日报了
     *
     * @return
     */
    public static boolean showDateReport() {
        if ((!getLoginedUser().isBxAuth() && !getLoginedUser().isDeliverAuth()
                && !getLoginedUser().isLeaveAuth() && !getLoginedUser().isMoneyAuth()) || getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 联系里面是否显示终端客户
     *
     * @return
     */
    public static boolean isShowTerminal() {
        if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 下级显示选择终端
     *
     * @return
     */
    public static boolean showTermialSel() {
        if (getLoginedUser().isMaxLeavel()) {
            if (getLoginedUser().getUserEnum().equals(UserEnum.AGENT) || getLoginedUser().getShopType() == 3) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否显示代为下单
     *
     * @return
     */
    public static boolean showHelpOrdered() {
        //最高权限没有代为下单，业务员有，但是终端业务员没有
        if (!getLoginedUser().isMaxLeavel()) {
            if (getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN) && getLoginedUser().getShopType() != 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否可修改订单
     * 超级业务员和商家必须在业务员确认之后才能进行操作
     *
     * @return
     */
    public static boolean isSalesEdit() {
        if (getLoginedUser().isMaxLeavel()) {
            return false;
        } else if (getLoginedUser().getUserEnum().equals(UserEnum.SALESMAN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 工作日报列表是否显示未读标记
     *
     * @return
     */
    public static boolean dailyUnreadShow() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是boss查看考情
     *
     * @return
     */
    public static boolean bossViewKq() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }

    //如果是总代或者总代的超管,
    //如果是经销商或者经销商的超管
    //如果是终端或终端的超管
    public static int getFilterRole() {
        int role = -1;
        if (getLoginedUser().isMaxLeavel()) {
            if (getLoginedUser().getUserEnum().equals(UserEnum.AGENT) || getLoginedUser().getShopType() == 3) {
                role = UserEnum.AGENT.getValue();
            } else if (getLoginedUser().getUserEnum().equals(UserEnum.DEALER) || getLoginedUser().getShopType() == 2) {
                role = UserEnum.DEALER.getValue();
            } else if (getLoginedUser().getUserEnum().equals(UserEnum.TERMINAL) || getLoginedUser().getShopType() == 1) {
                role = UserEnum.TERMINAL.getValue();
            }
        }
        return role;
    }

    /**
     * 能否发货
     * 只有发货权限的业务员能发货，boss也不能发货
     *
     * @return
     */
    public static boolean canDeliver() {
//        if (getLoginedUser().isMaxLeavel()){
//            return true;
//        }else{
//            if (getLoginedUser().isDeliverAuth()){
//                return true;
//            }else{
//                return false;
//            }
//        }
        if (getLoginedUser().isDeliverAuth() && !getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 能否确认订单
     *
     * @return
     */
    public static boolean canSureOrder() {
        if (getLoginedUser().isMaxLeavel()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 是否显示市场支持tab页
     *
     * @return
     */
    public static boolean showMarketCostTab() {
        if (getLoginedUser().getMarketCostEnum().equals(MarketCostEnum.NO_SUPPORT)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否本地记录市场支持费用,立返显示，其余不显示
     *
     * @return
     */
    public static boolean recordMarketCostFee() {
        if (getLoginedUser().getMarketCostEnum().equals(MarketCostEnum.REBACK_NOW)) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean recrKmot(){
        if (getLoginedUser().getjur().equals(ssss)){
          return false;
        }else {
            return true;
        }
    }
}
