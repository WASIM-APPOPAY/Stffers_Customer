
package com.stuffer.stuffers.models.restaurant;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("passwordHistory")
    @Expose
    private Object passwordHistory;
    @SerializedName("firstName")
    @Expose
    private Object firstName;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("passwordExpireTime")
    @Expose
    private String passwordExpireTime;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("accountexpired")
    @Expose
    private Boolean accountexpired;
    @SerializedName("credentialsexpired")
    @Expose
    private Boolean credentialsexpired;
    @SerializedName("accountlocked")
    @Expose
    private Boolean accountlocked;
    @SerializedName("accountLockedDate")
    @Expose
    private Object accountLockedDate;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("mobilenumber")
    @Expose
    private Long mobilenumber;
    @SerializedName("transactionpin")
    @Expose
    private Object transactionpin;
    @SerializedName("roles")
    @Expose
    private List<Role> roles;
    @SerializedName("customerdetails")
    @Expose
    private Object customerdetails;
    @SerializedName("merchantDetails")
    @Expose
    private MerchantDetails merchantDetails;
    @SerializedName("sellerDetails")
    @Expose
    private Object sellerDetails;
    @SerializedName("partner")
    @Expose
    private Object partner;
    @SerializedName("phonecode")
    @Expose
    private Integer phonecode;
    @SerializedName("securityanswer")
    @Expose
    private Object securityanswer;
    @SerializedName("usertype")
    @Expose
    private String usertype;
    @SerializedName("storename")
    @Expose
    private Object storename;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("screenlockpin")
    @Expose
    private Object screenlockpin;
    @SerializedName("question1")
    @Expose
    private Object question1;
    @SerializedName("answer1")
    @Expose
    private Object answer1;
    @SerializedName("question2")
    @Expose
    private Object question2;
    @SerializedName("answer2")
    @Expose
    private Object answer2;
    @SerializedName("middleName")
    @Expose
    private Object middleName;
    @SerializedName("partnerId")
    @Expose
    private Object partnerId;
    @SerializedName("isPasswordOutdated")
    @Expose
    private Object isPasswordOutdated;
    @SerializedName("zipCode")
    @Expose
    private Object zipCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public Object getPasswordHistory() {
        return passwordHistory;
    }

    public void setPasswordHistory(Object passwordHistory) {
        this.passwordHistory = passwordHistory;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordExpireTime() {
        return passwordExpireTime;
    }

    public void setPasswordExpireTime(String passwordExpireTime) {
        this.passwordExpireTime = passwordExpireTime;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Boolean getAccountexpired() {
        return accountexpired;
    }

    public void setAccountexpired(Boolean accountexpired) {
        this.accountexpired = accountexpired;
    }

    public Boolean getCredentialsexpired() {
        return credentialsexpired;
    }

    public void setCredentialsexpired(Boolean credentialsexpired) {
        this.credentialsexpired = credentialsexpired;
    }

    public Boolean getAccountlocked() {
        return accountlocked;
    }

    public void setAccountlocked(Boolean accountlocked) {
        this.accountlocked = accountlocked;
    }

    public Object getAccountLockedDate() {
        return accountLockedDate;
    }

    public void setAccountLockedDate(Object accountLockedDate) {
        this.accountLockedDate = accountLockedDate;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(Long mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public Object getTransactionpin() {
        return transactionpin;
    }

    public void setTransactionpin(Object transactionpin) {
        this.transactionpin = transactionpin;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Object getCustomerdetails() {
        return customerdetails;
    }

    public void setCustomerdetails(Object customerdetails) {
        this.customerdetails = customerdetails;
    }

    public MerchantDetails getMerchantDetails() {
        return merchantDetails;
    }

    public void setMerchantDetails(MerchantDetails merchantDetails) {
        this.merchantDetails = merchantDetails;
    }

    public Object getSellerDetails() {
        return sellerDetails;
    }

    public void setSellerDetails(Object sellerDetails) {
        this.sellerDetails = sellerDetails;
    }

    public Object getPartner() {
        return partner;
    }

    public void setPartner(Object partner) {
        this.partner = partner;
    }

    public Integer getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(Integer phonecode) {
        this.phonecode = phonecode;
    }

    public Object getSecurityanswer() {
        return securityanswer;
    }

    public void setSecurityanswer(Object securityanswer) {
        this.securityanswer = securityanswer;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Object getStorename() {
        return storename;
    }

    public void setStorename(Object storename) {
        this.storename = storename;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Object getScreenlockpin() {
        return screenlockpin;
    }

    public void setScreenlockpin(Object screenlockpin) {
        this.screenlockpin = screenlockpin;
    }

    public Object getQuestion1() {
        return question1;
    }

    public void setQuestion1(Object question1) {
        this.question1 = question1;
    }

    public Object getAnswer1() {
        return answer1;
    }

    public void setAnswer1(Object answer1) {
        this.answer1 = answer1;
    }

    public Object getQuestion2() {
        return question2;
    }

    public void setQuestion2(Object question2) {
        this.question2 = question2;
    }

    public Object getAnswer2() {
        return answer2;
    }

    public void setAnswer2(Object answer2) {
        this.answer2 = answer2;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public Object getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Object partnerId) {
        this.partnerId = partnerId;
    }

    public Object getIsPasswordOutdated() {
        return isPasswordOutdated;
    }

    public void setIsPasswordOutdated(Object isPasswordOutdated) {
        this.isPasswordOutdated = isPasswordOutdated;
    }

    public Object getZipCode() {
        return zipCode;
    }

    public void setZipCode(Object zipCode) {
        this.zipCode = zipCode;
    }

}
