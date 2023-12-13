
package com.stuffer.stuffers.models.restaurant;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MerchantDetails {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("costFee")
    @Expose
    private Object costFee;
    @SerializedName("merchantId")
    @Expose
    private String merchantId;
    @SerializedName("firstName")
    @Expose
    private Object firstName;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("middleName")
    @Expose
    private Object middleName;
    @SerializedName("companyName")
    @Expose
    private Object companyName;
    @SerializedName("businessName")
    @Expose
    private Object businessName;
    @SerializedName("rucNumber")
    @Expose
    private Object rucNumber;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("countryId")
    @Expose
    private Object countryId;
    @SerializedName("state")
    @Expose
    private Object state;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("createDate")
    @Expose
    private Object createDate;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("busiPhoneNumber")
    @Expose
    private Object busiPhoneNumber;
    @SerializedName("areaCode")
    @Expose
    private Object areaCode;
    @SerializedName("mobileNumber")
    @Expose
    private Object mobileNumber;
    @SerializedName("currencyType")
    @Expose
    private Object currencyType;
    @SerializedName("busiLocation")
    @Expose
    private Object busiLocation;
    @SerializedName("storeName")
    @Expose
    private Object storeName;
    @SerializedName("canCreatePromotion")
    @Expose
    private Object canCreatePromotion;
    @SerializedName("processingFee")
    @Expose
    private Object processingFee;
    @SerializedName("flatFees")
    @Expose
    private Object flatFees;
    @SerializedName("percentage")
    @Expose
    private Object percentage;
    @SerializedName("bankName")
    @Expose
    private Object bankName;
    @SerializedName("accountType")
    @Expose
    private Object accountType;
    @SerializedName("accountNumber")
    @Expose
    private Object accountNumber;
    @SerializedName("categoryWithMerchantCode")
    @Expose
    private Object categoryWithMerchantCode;
    @SerializedName("merchantProfileType")
    @Expose
    private Object merchantProfileType;
    @SerializedName("encodedMerchantQrcode")
    @Expose
    private Object encodedMerchantQrcode;
    @SerializedName("avisoPic")
    @Expose
    private Object avisoPic;
    @SerializedName("merchantAddressPic")
    @Expose
    private Object merchantAddressPic;
    @SerializedName("governmentIdPic")
    @Expose
    private Object governmentIdPic;
    @SerializedName("signaturePic")
    @Expose
    private Object signaturePic;
    @SerializedName("name")
    @Expose
    private Object name;
    @SerializedName("date")
    @Expose
    private Object date;
    @SerializedName("salesName")
    @Expose
    private Object salesName;
    @SerializedName("merchantQrcode")
    @Expose
    private Object merchantQrcode;
    @SerializedName("terminalId")
    @Expose
    private Object terminalId;
    @SerializedName("active")
    @Expose
    private Object active;
    @SerializedName("partnerId")
    @Expose
    private Object partnerId;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("website")
    @Expose
    private Object website;
    @SerializedName("businessAddress")
    @Expose
    private Object businessAddress;
    @SerializedName("schedule")
    @Expose
    private Object schedule;
    @SerializedName("businessType")
    @Expose
    private Object businessType;
    @SerializedName("shopType")
    @Expose
    private Object shopType;
    @SerializedName("orderSystem")
    @Expose
    private Boolean orderSystem;
    @SerializedName("categories")
    @Expose
    private Object categories;
    @SerializedName("merchantAccounts")
    @Expose
    private List<MerchantAccount> merchantAccounts;
    @SerializedName("userId")
    @Expose
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getCostFee() {
        return costFee;
    }

    public void setCostFee(Object costFee) {
        this.costFee = costFee;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public Object getCompanyName() {
        return companyName;
    }

    public void setCompanyName(Object companyName) {
        this.companyName = companyName;
    }

    public Object getBusinessName() {
        return businessName;
    }

    public void setBusinessName(Object businessName) {
        this.businessName = businessName;
    }

    public Object getRucNumber() {
        return rucNumber;
    }

    public void setRucNumber(Object rucNumber) {
        this.rucNumber = rucNumber;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getCountryId() {
        return countryId;
    }

    public void setCountryId(Object countryId) {
        this.countryId = countryId;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getBusiPhoneNumber() {
        return busiPhoneNumber;
    }

    public void setBusiPhoneNumber(Object busiPhoneNumber) {
        this.busiPhoneNumber = busiPhoneNumber;
    }

    public Object getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Object areaCode) {
        this.areaCode = areaCode;
    }

    public Object getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Object mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Object getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Object currencyType) {
        this.currencyType = currencyType;
    }

    public Object getBusiLocation() {
        return busiLocation;
    }

    public void setBusiLocation(Object busiLocation) {
        this.busiLocation = busiLocation;
    }

    public Object getStoreName() {
        return storeName;
    }

    public void setStoreName(Object storeName) {
        this.storeName = storeName;
    }

    public Object getCanCreatePromotion() {
        return canCreatePromotion;
    }

    public void setCanCreatePromotion(Object canCreatePromotion) {
        this.canCreatePromotion = canCreatePromotion;
    }

    public Object getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(Object processingFee) {
        this.processingFee = processingFee;
    }

    public Object getFlatFees() {
        return flatFees;
    }

    public void setFlatFees(Object flatFees) {
        this.flatFees = flatFees;
    }

    public Object getPercentage() {
        return percentage;
    }

    public void setPercentage(Object percentage) {
        this.percentage = percentage;
    }

    public Object getBankName() {
        return bankName;
    }

    public void setBankName(Object bankName) {
        this.bankName = bankName;
    }

    public Object getAccountType() {
        return accountType;
    }

    public void setAccountType(Object accountType) {
        this.accountType = accountType;
    }

    public Object getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Object accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Object getCategoryWithMerchantCode() {
        return categoryWithMerchantCode;
    }

    public void setCategoryWithMerchantCode(Object categoryWithMerchantCode) {
        this.categoryWithMerchantCode = categoryWithMerchantCode;
    }

    public Object getMerchantProfileType() {
        return merchantProfileType;
    }

    public void setMerchantProfileType(Object merchantProfileType) {
        this.merchantProfileType = merchantProfileType;
    }

    public Object getEncodedMerchantQrcode() {
        return encodedMerchantQrcode;
    }

    public void setEncodedMerchantQrcode(Object encodedMerchantQrcode) {
        this.encodedMerchantQrcode = encodedMerchantQrcode;
    }

    public Object getAvisoPic() {
        return avisoPic;
    }

    public void setAvisoPic(Object avisoPic) {
        this.avisoPic = avisoPic;
    }

    public Object getMerchantAddressPic() {
        return merchantAddressPic;
    }

    public void setMerchantAddressPic(Object merchantAddressPic) {
        this.merchantAddressPic = merchantAddressPic;
    }

    public Object getGovernmentIdPic() {
        return governmentIdPic;
    }

    public void setGovernmentIdPic(Object governmentIdPic) {
        this.governmentIdPic = governmentIdPic;
    }

    public Object getSignaturePic() {
        return signaturePic;
    }

    public void setSignaturePic(Object signaturePic) {
        this.signaturePic = signaturePic;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public Object getSalesName() {
        return salesName;
    }

    public void setSalesName(Object salesName) {
        this.salesName = salesName;
    }

    public Object getMerchantQrcode() {
        return merchantQrcode;
    }

    public void setMerchantQrcode(Object merchantQrcode) {
        this.merchantQrcode = merchantQrcode;
    }

    public Object getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Object terminalId) {
        this.terminalId = terminalId;
    }

    public Object getActive() {
        return active;
    }

    public void setActive(Object active) {
        this.active = active;
    }

    public Object getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Object partnerId) {
        this.partnerId = partnerId;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getWebsite() {
        return website;
    }

    public void setWebsite(Object website) {
        this.website = website;
    }

    public Object getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(Object businessAddress) {
        this.businessAddress = businessAddress;
    }

    public Object getSchedule() {
        return schedule;
    }

    public void setSchedule(Object schedule) {
        this.schedule = schedule;
    }

    public Object getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Object businessType) {
        this.businessType = businessType;
    }

    public Object getShopType() {
        return shopType;
    }

    public void setShopType(Object shopType) {
        this.shopType = shopType;
    }

    public Boolean getOrderSystem() {
        return orderSystem;
    }

    public void setOrderSystem(Boolean orderSystem) {
        this.orderSystem = orderSystem;
    }

    public Object getCategories() {
        return categories;
    }

    public void setCategories(Object categories) {
        this.categories = categories;
    }

    public List<MerchantAccount> getMerchantAccounts() {
        return merchantAccounts;
    }

    public void setMerchantAccounts(List<MerchantAccount> merchantAccounts) {
        this.merchantAccounts = merchantAccounts;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
