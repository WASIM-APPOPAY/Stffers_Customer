package com.stuffer.stuffers.models.shop_model;

public class ShopModel  {
    String mTitle;
    int resIds;

    public ShopModel(String mTitle, int resIds) {
        this.mTitle = mTitle;
        this.resIds = resIds;
    }

    public String getmTitle() {
        return mTitle;
    }

    public int getResIds() {
        return resIds;
    }
}
