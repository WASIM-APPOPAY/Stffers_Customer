package com.stuffer.stuffers.utils;

import java.io.Serializable;
import java.util.List;

public class MerchantInfoBean implements Serializable {
    public String id;
    public String avatar;
    public String businessAddress;
    public String address;
    public String businessName;
    public String businessType;
    public List<CategoryBean> categories;
    public String description;
    public String email;
    public String mobileNumber;
    public String partnerId;
    public ScheduleTimeBean schedule;
    public String website;
    public String shopType;
    public String merchantAccount;

    public static class CategoryBean implements Serializable{
        public String countryOfOrigin;
        public String description;
        public String id;
        public String itemCode;
        public String itemName;
        public String link;
        public String sellerId;
        public List<String> pictureList;
        public int price = -1;
        public int count;
    }
}
