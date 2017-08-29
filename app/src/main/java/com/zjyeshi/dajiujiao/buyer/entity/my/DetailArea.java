package com.zjyeshi.dajiujiao.buyer.entity.my;

import com.zjyeshi.dajiujiao.buyer.entity.enums.ProvinceEnum;

/**
 * Created by wuhk on 2015/11/22.
 */
public class DetailArea {
    private String code ;
    private String name ;
    private ProvinceEnum provinceEnum;
    private String speName;
    private String speCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProvinceEnum getProvinceEnum() {
        return provinceEnum;
    }

    public void setProvinceEnum(ProvinceEnum provinceEnum) {
        this.provinceEnum = provinceEnum;
    }

    public String getSpeName() {
        return speName;
    }

    public void setSpeName(String speName) {
        this.speName = speName;
    }

    public String getSpeCode() {
        return speCode;
    }

    public void setSpeCode(String speCode) {
        this.speCode = speCode;
    }
}
