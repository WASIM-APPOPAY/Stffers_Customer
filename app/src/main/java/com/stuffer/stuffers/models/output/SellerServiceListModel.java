package com.stuffer.stuffers.models.output;

/**
 * Created by Sandeep Londhe on 29-01-2019.
 *
 * @Email :  sandeeplondhe54@gmail.com
 * @Author :  https://twitter.com/mesandeeplondhe
 * @Skype :  sandeeplondhe54
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SellerServiceListModel {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sellers")
    @Expose
    private List<Seller> sellers = null;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Seller> getSellers() {
        return sellers;
    }

    public void setSellers(List<Seller> sellers) {
        this.sellers = sellers;
    }


    public class Seller {

        @SerializedName("seller_id")
        @Expose
        private String sellerId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("seller_name")
        @Expose
        private String sellerName;
        @SerializedName("seller_logo")
        @Expose
        private String sellerLogo;
        @SerializedName("contact_no")
        @Expose
        private String contactNo;
        @SerializedName("contact_email")
        @Expose
        private String contactEmail;
        @SerializedName("seller_address")
        @Expose
        private String sellerAddress;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("registration_date")
        @Expose
        private String registrationDate;
        @SerializedName("service_id")
        @Expose
        private String serviceId;
        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("service_image")
        @Expose
        private String serviceImage;
        @SerializedName("service_cat_id")
        @Expose
        private String serviceCatId;
        @SerializedName("service_sub_cat_id")
        @Expose
        private String serviceSubCatId;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("max_no_of_bookings")
        @Expose
        private String maxNoOfBookings;
        @SerializedName("service_type")
        @Expose
        private String serviceType;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("modified_date")
        @Expose
        private String modifiedDate;

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getSellerLogo() {
            return sellerLogo;
        }

        public void setSellerLogo(String sellerLogo) {
            this.sellerLogo = sellerLogo;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }

        public String getSellerAddress() {
            return sellerAddress;
        }

        public void setSellerAddress(String sellerAddress) {
            this.sellerAddress = sellerAddress;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getRegistrationDate() {
            return registrationDate;
        }

        public void setRegistrationDate(String registrationDate) {
            this.registrationDate = registrationDate;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getServiceImage() {
            return serviceImage;
        }

        public void setServiceImage(String serviceImage) {
            this.serviceImage = serviceImage;
        }

        public String getServiceCatId() {
            return serviceCatId;
        }

        public void setServiceCatId(String serviceCatId) {
            this.serviceCatId = serviceCatId;
        }

        public String getServiceSubCatId() {
            return serviceSubCatId;
        }

        public void setServiceSubCatId(String serviceSubCatId) {
            this.serviceSubCatId = serviceSubCatId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getMaxNoOfBookings() {
            return maxNoOfBookings;
        }

        public void setMaxNoOfBookings(String maxNoOfBookings) {
            this.maxNoOfBookings = maxNoOfBookings;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

    }
}