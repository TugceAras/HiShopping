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
 * Definition of ObjectType Favorites.
 *
 * @since 2023-09-25
 */
@PrimaryKeys({"id"})
@Indexes({"productTitle:productTitle", "productId:productId", "productOldPrice:productOldPrice", "productPhotoUrl:productPhotoUrl", "id:id", "userId:userId", "productPrice:productPrice"})
public final class Favorites extends CloudDBZoneObject {
    private String id;

    private String userId;

    private String productId;

    private String productTitle;

    private String productPrice;

    private String productOldPrice;

    private String productPhotoUrl;

    public Favorites() {
        super(Favorites.class);
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

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductOldPrice(String productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

    public String getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductPhotoUrl(String productPhotoUrl) {
        this.productPhotoUrl = productPhotoUrl;
    }

    public String getProductPhotoUrl() {
        return productPhotoUrl;
    }

}
