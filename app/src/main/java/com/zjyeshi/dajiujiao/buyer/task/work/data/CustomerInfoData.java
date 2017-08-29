package com.zjyeshi.dajiujiao.buyer.task.work.data;

import com.zjyeshi.dajiujiao.buyer.task.data.BaseData;

/**
 * 客户信息
 *
 * Created by zhum on 2016/6/22.
 */
public class CustomerInfoData extends BaseData<CustomerInfoData>{

    private String id;//客户id
    private String name;//店铺名字
    private String pic;//店铺图片
    private String phone;//手机
    private String landlinePhone;//固话
    private String legalPerson;//法人名字
    private String turnover;//年营业额
    private String rightPerson;//权力人物
    private String salemensQuantity;//业务员人数
    private String personQuantity;//内勤人数
    private String area;//行政区划代码，省市区编码，逗号分隔
    private String province;
    private String city;
    private String town;
    private String address;//地址
    private String lngE5;//经度
    private String latE5;//纬度
    private String businessArea;//营业面积
    private String warehouseArea;//仓库面积
    private String paymentMethod;//付款方式 1 现金 2 打卡
    private String creditRating;//信誉等级
    private String productStatus;//产品地位 1 一般 2 核心 3重点
    private String comptingProducts;//竞品信息
    private String ramark;//备注

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLandlinePhone() {
        return landlinePhone;
    }

    public void setLandlinePhone(String landlinePhone) {
        this.landlinePhone = landlinePhone;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getRightPerson() {
        return rightPerson;
    }

    public void setRightPerson(String rightPerson) {
        this.rightPerson = rightPerson;
    }

    public String getSalemensQuantity() {
        return salemensQuantity;
    }

    public void setSalemensQuantity(String salemensQuantity) {
        this.salemensQuantity = salemensQuantity;
    }

    public String getPersonQuantity() {
        return personQuantity;
    }

    public void setPersonQuantity(String personQuantity) {
        this.personQuantity = personQuantity;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLngE5() {
        return lngE5;
    }

    public void setLngE5(String lngE5) {
        this.lngE5 = lngE5;
    }

    public String getLatE5() {
        return latE5;
    }

    public void setLatE5(String latE5) {
        this.latE5 = latE5;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public String getWarehouseArea() {
        return warehouseArea;
    }

    public void setWarehouseArea(String warehouseArea) {
        this.warehouseArea = warehouseArea;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getComptingProducts() {
        return comptingProducts;
    }

    public void setComptingProducts(String comptingProducts) {
        this.comptingProducts = comptingProducts;
    }

    public String getRamark() {
        return ramark;
    }

    public void setRamark(String ramark) {
        this.ramark = ramark;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
