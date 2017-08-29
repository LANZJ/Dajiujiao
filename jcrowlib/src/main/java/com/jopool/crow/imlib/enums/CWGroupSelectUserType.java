package com.jopool.crow.imlib.enums;

/**
 * Created by wuhk on 2016/11/8.
 */
public enum CWGroupSelectUserType {
    CREATE(1), ADD(2), REMOVE(3);
    private int value;

    public int getValue() {
        return value;
    }

    CWGroupSelectUserType(int value) {
        this.value = value;
    }

    public static CWGroupSelectUserType valueOf(int value) {
        CWGroupSelectUserType selectUserType = null;
        switch (value) {
            case 1:
                selectUserType = CREATE;
                break;
            case 2:
                selectUserType = ADD;
                break;
            case 3:
                selectUserType = REMOVE;
                break;
            default:
                selectUserType = CREATE;
                break;
        }
        return selectUserType;
    }

    public boolean equals(CWGroupSelectUserType selectUserType) {
        if (null == selectUserType) {
            return false;
        }
        return value == selectUserType.value;
    }
}
