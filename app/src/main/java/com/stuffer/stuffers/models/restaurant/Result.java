
package com.stuffer.stuffers.models.restaurant;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("countryId")
    @Expose
    private Object countryId;
    @SerializedName("addressPic")
    @Expose
    private String addressPic;
    @SerializedName("areaCode")
    @Expose
    private Object areaCode;
    @SerializedName("busiLocation")
    @Expose
    private Object busiLocation;
    @SerializedName("busiPhoneNumber")
    @Expose
    private Object busiPhoneNumber;
    @SerializedName("businessName")
    @Expose
    private String businessName;
    @SerializedName("businessTime")
    @Expose
    private Object businessTime;
    @SerializedName("currencyType")
    @Expose
    private Object currencyType;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("userId")
    @Expose
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getCountryId() {
        return countryId;
    }

    public void setCountryId(Object countryId) {
        this.countryId = countryId;
    }

    public String getAddressPic() {
        return addressPic;
    }

    public void setAddressPic(String addressPic) {
        this.addressPic = addressPic;
    }

    public Object getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Object areaCode) {
        this.areaCode = areaCode;
    }

    public Object getBusiLocation() {
        return busiLocation;
    }

    public void setBusiLocation(Object busiLocation) {
        this.busiLocation = busiLocation;
    }

    public Object getBusiPhoneNumber() {
        return busiPhoneNumber;
    }

    public void setBusiPhoneNumber(Object busiPhoneNumber) {
        this.busiPhoneNumber = busiPhoneNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Object getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(Object businessTime) {
        this.businessTime = businessTime;
    }

    public Object getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Object currencyType) {
        this.currencyType = currencyType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
