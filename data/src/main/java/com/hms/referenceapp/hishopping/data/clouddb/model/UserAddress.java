/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2020. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.hms.referenceapp.hishopping.data.clouddb.model;

import com.huawei.agconnect.cloud.database.annotations.Indexes;
import com.huawei.agconnect.cloud.database.annotations.PrimaryKeys;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.Text;

import java.util.Date;

/**
 * Definition of ObjectType UserAddress.
 *
 * @since 2023-09-25
 */
@PrimaryKeys({"id"})
@Indexes({"country:country", "province:province", "buildingNo:buildingNo", "latitude:latitude", "id:id", "title:title", "addressLine:addressLine", "aptNo:aptNo", "userId:userId", "longitude:longitude"})
public final class UserAddress extends CloudDBZoneObject {
    private String id;

    private String userId;

    private String addressLine;

    private String country;

    private String province;

    private String buildingNo;

    private String aptNo;

    private Double latitude;

    private Double longitude;

    private String title;

    public UserAddress() {
        super(UserAddress.class);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setAptNo(String aptNo) {
        this.aptNo = aptNo;
    }

    public String getAptNo() {
        return aptNo;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
