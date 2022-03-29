package com.stuffer.stuffers.models.shop_model;

/*public class ItemDetails {
}*/


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemDetails {
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("error")
    @Expose

    private boolean error;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean getError() {
        return this.error;
    }

    public class Data {
        @SerializedName("count")
        @Expose
        private int count;
        @SerializedName("items")
        @Expose
        private List<Items> items;

        public void setCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return this.count;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public List<Items> getItems() {
            return this.items;
        }

        public class Items {
            @SerializedName("metaSeo")
            @Expose
            private MetaSeo metaSeo;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("price")
            @Expose
            private double price;
            @SerializedName("salePrice")
            @Expose
            private double salePrice;
            @SerializedName("discounted")
            @Expose
            private boolean discounted;
            @SerializedName("stockQuantity")
            @Expose
            private int stockQuantity;
            @SerializedName("featured")
            @Expose
            private boolean featured;
            @SerializedName("hot")
            @Expose
            private boolean hot;
            @SerializedName("bestSell")
            @Expose
            private boolean bestSell;
            @SerializedName("isActive")
            @Expose
            private boolean isActive;
            @SerializedName("sku")
            @Expose
            private String sku;
            @SerializedName("upc")
            @Expose
            private String upc;
            @SerializedName("mpn")
            @Expose
            private String mpn;
            @SerializedName("ean")
            @Expose
            private String ean;
            @SerializedName("jan")
            @Expose
            private String jan;
            @SerializedName("isbn")
            @Expose
            private String isbn;
            @SerializedName("images")
            @Expose
            private List<String> images;
            @SerializedName("shopVerified")
            @Expose
            private boolean shopVerified;
            @SerializedName("shopActivated")
            @Expose
            private boolean shopActivated;
            @SerializedName("shopFeatured")
            @Expose
            private boolean shopFeatured;
            @SerializedName("taxPercentage")
            @Expose
            private int taxPercentage;
            @SerializedName("restrictCODAreas")
            @Expose
            private List<String> restrictCODAreas;
            @SerializedName("freeShip")
            @Expose
            private boolean freeShip;
            @SerializedName("dailyDeal")
            @Expose
            private boolean dailyDeal;
            @SerializedName("soldOut")
            @Expose
            private boolean soldOut;
            @SerializedName("weight")
            @Expose
            private double weight;
            @SerializedName("ratingAvg")
            @Expose
            private int ratingAvg;

            @SerializedName("totalRating")
            @Expose
            private int totalRating;
            @SerializedName("ratingScore")
            @Expose
            private int ratingScore;
            @SerializedName("_id")
            @Expose
            private String _id;
            @SerializedName("name")
            @Expose
            private String name;

            @SerializedName("alias")
            @Expose
            private String alias;

            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("shortDescription")
            @Expose
            private String shortDescription;
            @SerializedName("categoryId")
            @Expose

            private String categoryId;
            @SerializedName("specifications")
            @Expose
            private List<String> specifications;
            @SerializedName("mainImage")
            @Expose
            private MainImage mainImage;
            @SerializedName("restrictFreeShipAreas")
            @Expose
            private List<String> restrictFreeShipAreas;
            @SerializedName("weightType")
            @Expose
            private String weightType;
            @SerializedName("volume")
            @Expose
            private int volume;
            @SerializedName("createdBy")
            @Expose
            private String createdBy;
            @SerializedName("updatedBy")
            @Expose
            private String updatedBy;
            @SerializedName("shopId")
            @Expose
            private String shopId;
            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("createdAt")
            @Expose
            private String createdAt;
            @SerializedName("updatedAt")
            @Expose
            private String updatedAt;
            @SerializedName("taxClass")
            @Expose
            private String taxClass;
            @SerializedName("__v")
            @Expose
            private int __v;
            @SerializedName("category")
            @Expose
            private Category category;
            @SerializedName("shop")
            @Expose
            private Shop shop;
            @SerializedName("id")
            @Expose
            private String id;

            public void setMetaSeo(MetaSeo metaSeo) {
                this.metaSeo = metaSeo;
            }

            public MetaSeo getMetaSeo() {
                return this.metaSeo;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getType() {
                return this.type;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public double getPrice() {
                return this.price;
            }

            public void setSalePrice(double salePrice) {
                this.salePrice = salePrice;
            }

            public double getSalePrice() {
                return this.salePrice;
            }

            public void setDiscounted(boolean discounted) {
                this.discounted = discounted;
            }

            public boolean getDiscounted() {
                return this.discounted;
            }

            public void setStockQuantity(int stockQuantity) {
                this.stockQuantity = stockQuantity;
            }

            public int getStockQuantity() {
                return this.stockQuantity;
            }

            public void setFeatured(boolean featured) {
                this.featured = featured;
            }

            public boolean getFeatured() {
                return this.featured;
            }

            public void setHot(boolean hot) {
                this.hot = hot;
            }

            public boolean getHot() {
                return this.hot;
            }

            public void setBestSell(boolean bestSell) {
                this.bestSell = bestSell;
            }

            public boolean getBestSell() {
                return this.bestSell;
            }

            public void setIsActive(boolean isActive) {
                this.isActive = isActive;
            }

            public boolean getIsActive() {
                return this.isActive;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getSku() {
                return this.sku;
            }

            public void setUpc(String upc) {
                this.upc = upc;
            }

            public String getUpc() {
                return this.upc;
            }

            public void setMpn(String mpn) {
                this.mpn = mpn;
            }

            public String getMpn() {
                return this.mpn;
            }

            public void setEan(String ean) {
                this.ean = ean;
            }

            public String getEan() {
                return this.ean;
            }

            public void setJan(String jan) {
                this.jan = jan;
            }

            public String getJan() {
                return this.jan;
            }

            public void setIsbn(String isbn) {
                this.isbn = isbn;
            }

            public String getIsbn() {
                return this.isbn;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public List<String> getImages() {
                return this.images;
            }

            public void setShopVerified(boolean shopVerified) {
                this.shopVerified = shopVerified;
            }

            public boolean getShopVerified() {
                return this.shopVerified;
            }

            public void setShopActivated(boolean shopActivated) {
                this.shopActivated = shopActivated;
            }

            public boolean getShopActivated() {
                return this.shopActivated;
            }

            public void setShopFeatured(boolean shopFeatured) {
                this.shopFeatured = shopFeatured;
            }

            public boolean getShopFeatured() {
                return this.shopFeatured;
            }

            public void setTaxPercentage(int taxPercentage) {
                this.taxPercentage = taxPercentage;
            }

            public int getTaxPercentage() {
                return this.taxPercentage;
            }

            public void setRestrictCODAreas(List<String> restrictCODAreas) {
                this.restrictCODAreas = restrictCODAreas;
            }

            public List<String> getRestrictCODAreas() {
                return this.restrictCODAreas;
            }

            public void setFreeShip(boolean freeShip) {
                this.freeShip = freeShip;
            }

            public boolean getFreeShip() {
                return this.freeShip;
            }

            public void setDailyDeal(boolean dailyDeal) {
                this.dailyDeal = dailyDeal;
            }

            public boolean getDailyDeal() {
                return this.dailyDeal;
            }

            public void setSoldOut(boolean soldOut) {
                this.soldOut = soldOut;
            }

            public boolean getSoldOut() {
                return this.soldOut;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }

            public double getWeight() {
                return this.weight;
            }

            public void setRatingAvg(int ratingAvg) {
                this.ratingAvg = ratingAvg;
            }

            public int getRatingAvg() {
                return this.ratingAvg;
            }

            public void setTotalRating(int totalRating) {
                this.totalRating = totalRating;
            }

            public int getTotalRating() {
                return this.totalRating;
            }

            public void setRatingScore(int ratingScore) {
                this.ratingScore = ratingScore;
            }

            public int getRatingScore() {
                return this.ratingScore;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String get_id() {
                return this._id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return this.name;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getAlias() {
                return this.alias;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return this.description;
            }

            public void setShortDescription(String shortDescription) {
                this.shortDescription = shortDescription;
            }

            public String getShortDescription() {
                return this.shortDescription;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getCategoryId() {
                return this.categoryId;
            }

            public void setSpecifications(List<String> specifications) {
                this.specifications = specifications;
            }

            public List<String> getSpecifications() {
                return this.specifications;
            }

            public void setMainImage(MainImage mainImage) {
                this.mainImage = mainImage;
            }

            public MainImage getMainImage() {
                return this.mainImage;
            }

            public void setRestrictFreeShipAreas(List<String> restrictFreeShipAreas) {
                this.restrictFreeShipAreas = restrictFreeShipAreas;
            }

            public List<String> getRestrictFreeShipAreas() {
                return this.restrictFreeShipAreas;
            }

            public void setWeightType(String weightType) {
                this.weightType = weightType;
            }

            public String getWeightType() {
                return this.weightType;
            }

            public void setVolume(int volume) {
                this.volume = volume;
            }

            public int getVolume() {
                return this.volume;
            }

            public void setCreatedBy(String createdBy) {
                this.createdBy = createdBy;
            }

            public String getCreatedBy() {
                return this.createdBy;
            }

            public void setUpdatedBy(String updatedBy) {
                this.updatedBy = updatedBy;
            }

            public String getUpdatedBy() {
                return this.updatedBy;
            }

            public void setShopId(String shopId) {
                this.shopId = shopId;
            }

            public String getShopId() {
                return this.shopId;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getCurrency() {
                return this.currency;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getCreatedAt() {
                return this.createdAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getUpdatedAt() {
                return this.updatedAt;
            }

            public void setTaxClass(String taxClass) {
                this.taxClass = taxClass;
            }

            public String getTaxClass() {
                return this.taxClass;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }

            public int get__v() {
                return this.__v;
            }

            public void setCategory(Category category) {
                this.category = category;
            }

            public Category getCategory() {
                return this.category;
            }

            public void setShop(Shop shop) {
                this.shop = shop;
            }

            public Shop getShop() {
                return this.shop;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return this.id;
            }

            public class MetaSeo {

                private String keywords;

                private String description;

                public void setKeywords(String keywords) {
                    this.keywords = keywords;
                }

                public String getKeywords() {
                    return this.keywords;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public String getDescription() {
                    return this.description;
                }
            }


            public class MainImage {
                private boolean uploaded;

                private String _id;

                private String type;

                private String id;

                private String fileUrl;

                private String mediumUrl;

                private String thumbUrl;

                public void setUploaded(boolean uploaded) {
                    this.uploaded = uploaded;
                }

                public boolean getUploaded() {
                    return this.uploaded;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String get_id() {
                    return this._id;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getType() {
                    return this.type;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return this.id;
                }

                public void setFileUrl(String fileUrl) {
                    this.fileUrl = fileUrl;
                }

                public String getFileUrl() {
                    return this.fileUrl;
                }

                public void setMediumUrl(String mediumUrl) {
                    this.mediumUrl = mediumUrl;
                }

                public String getMediumUrl() {
                    return this.mediumUrl;
                }

                public void setThumbUrl(String thumbUrl) {
                    this.thumbUrl = thumbUrl;
                }

                public String getThumbUrl() {
                    return this.thumbUrl;
                }
            }


            public class Category {
                private int totalProduct;

                private String _id;

                private String name;

                private String parentId;

                private String mainImage;

                private String id;

                public void setTotalProduct(int totalProduct) {
                    this.totalProduct = totalProduct;
                }

                public int getTotalProduct() {
                    return this.totalProduct;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String get_id() {
                    return this._id;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return this.name;
                }

                public void setParentId(String parentId) {
                    this.parentId = parentId;
                }

                public String getParentId() {
                    return this.parentId;
                }

                public void setMainImage(String mainImage) {
                    this.mainImage = mainImage;
                }

                public String getMainImage() {
                    return this.mainImage;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return this.id;
                }
            }


            public class BusinessInfo {
                private String name;

                private String identifier;

                private String address;

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return this.name;
                }

                public void setIdentifier(String identifier) {
                    this.identifier = identifier;
                }

                public String getIdentifier() {
                    return this.identifier;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getAddress() {
                    return this.address;
                }
            }


            public class Socials {
                private String facebook;

                private String instagram;

                public void setFacebook(String facebook) {
                    this.facebook = facebook;
                }

                public String getFacebook() {
                    return this.facebook;
                }

                public void setInstagram(String instagram) {
                    this.instagram = instagram;
                }

                public String getInstagram() {
                    return this.instagram;
                }
            }


            public class SocialConnected {
                private boolean facebook;

                private boolean twitter;

                private boolean google;

                private boolean linkedin;

                public void setFacebook(boolean facebook) {
                    this.facebook = facebook;
                }

                public boolean getFacebook() {
                    return this.facebook;
                }

                public void setTwitter(boolean twitter) {
                    this.twitter = twitter;
                }

                public boolean getTwitter() {
                    return this.twitter;
                }

                public void setGoogle(boolean google) {
                    this.google = google;
                }

                public boolean getGoogle() {
                    return this.google;
                }

                public void setLinkedin(boolean linkedin) {
                    this.linkedin = linkedin;
                }

                public boolean getLinkedin() {
                    return this.linkedin;
                }
            }


            public class Notifications {
                private boolean lowInventory;

                public void setLowInventory(boolean lowInventory) {
                    this.lowInventory = lowInventory;
                }

                public boolean getLowInventory() {
                    return this.lowInventory;
                }
            }


            public class ShippingSettings {
                private int defaultPrice;

                private int perProductPrice;

                private int perQuantityPrice;

                private String processingTime;

                private String shippingPolicy;

                private String refundPolicy;

                public void setDefaultPrice(int defaultPrice) {
                    this.defaultPrice = defaultPrice;
                }

                public int getDefaultPrice() {
                    return this.defaultPrice;
                }

                public void setPerProductPrice(int perProductPrice) {
                    this.perProductPrice = perProductPrice;
                }

                public int getPerProductPrice() {
                    return this.perProductPrice;
                }

                public void setPerQuantityPrice(int perQuantityPrice) {
                    this.perQuantityPrice = perQuantityPrice;
                }

                public int getPerQuantityPrice() {
                    return this.perQuantityPrice;
                }

                public void setProcessingTime(String processingTime) {
                    this.processingTime = processingTime;
                }

                public String getProcessingTime() {
                    return this.processingTime;
                }

                public void setShippingPolicy(String shippingPolicy) {
                    this.shippingPolicy = shippingPolicy;
                }

                public String getShippingPolicy() {
                    return this.shippingPolicy;
                }

                public void setRefundPolicy(String refundPolicy) {
                    this.refundPolicy = refundPolicy;
                }

                public String getRefundPolicy() {
                    return this.refundPolicy;
                }
            }


            public class PickUpAddress {
                private String street;

                private String city;

                private String area;

                private double lat;

                private double lng;

                public void setStreet(String street) {
                    this.street = street;
                }

                public String getStreet() {
                    return this.street;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getCity() {
                    return this.city;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public String getArea() {
                    return this.area;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLat() {
                    return this.lat;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }

                public double getLng() {
                    return this.lng;
                }
            }


            public class Shop {
                private BusinessInfo businessInfo;

                private Socials socials;

                private SocialConnected socialConnected;

                private Notifications notifications;

                private ShippingSettings shippingSettings;

                private List<Double> location;

                private boolean verified;

                private boolean activated;

                private boolean featured;

                private String storeType;

                private String gaCode;

                private String headerText;

                private boolean doCOD;

                private boolean pickUpAtStore;

                private List<PickUpAddress> pickUpAddress;

                private boolean storeWideShipping;

                private String announcement;

                private int ratingAvg;

                private int totalRating;

                private int ratingScore;

                private String _id;

                private String city;

                private String area;

                private String phoneNumber;

                private String email;

                private String name;

                private String country;

                private String ownerId;

                private String address;

                private String createdAt;

                private String updatedAt;

                private String returnAddress;

                private String alias;

                private int __v;

                private String logoId;

                private String bannerId;

                private String shopDelivery;

                private String id;

                public void setBusinessInfo(BusinessInfo businessInfo) {
                    this.businessInfo = businessInfo;
                }

                public BusinessInfo getBusinessInfo() {
                    return this.businessInfo;
                }

                public void setSocials(Socials socials) {
                    this.socials = socials;
                }

                public Socials getSocials() {
                    return this.socials;
                }

                public void setSocialConnected(SocialConnected socialConnected) {
                    this.socialConnected = socialConnected;
                }

                public SocialConnected getSocialConnected() {
                    return this.socialConnected;
                }

                public void setNotifications(Notifications notifications) {
                    this.notifications = notifications;
                }

                public Notifications getNotifications() {
                    return this.notifications;
                }

                public void setShippingSettings(ShippingSettings shippingSettings) {
                    this.shippingSettings = shippingSettings;
                }

                public ShippingSettings getShippingSettings() {
                    return this.shippingSettings;
                }

                public void setLocation(List<Double> location) {
                    this.location = location;
                }

                public List<Double> getLocation() {
                    return this.location;
                }

                public void setVerified(boolean verified) {
                    this.verified = verified;
                }

                public boolean getVerified() {
                    return this.verified;
                }

                public void setActivated(boolean activated) {
                    this.activated = activated;
                }

                public boolean getActivated() {
                    return this.activated;
                }

                public void setFeatured(boolean featured) {
                    this.featured = featured;
                }

                public boolean getFeatured() {
                    return this.featured;
                }

                public void setStoreType(String storeType) {
                    this.storeType = storeType;
                }

                public String getStoreType() {
                    return this.storeType;
                }

                public void setGaCode(String gaCode) {
                    this.gaCode = gaCode;
                }

                public String getGaCode() {
                    return this.gaCode;
                }

                public void setHeaderText(String headerText) {
                    this.headerText = headerText;
                }

                public String getHeaderText() {
                    return this.headerText;
                }

                public void setDoCOD(boolean doCOD) {
                    this.doCOD = doCOD;
                }

                public boolean getDoCOD() {
                    return this.doCOD;
                }

                public void setPickUpAtStore(boolean pickUpAtStore) {
                    this.pickUpAtStore = pickUpAtStore;
                }

                public boolean getPickUpAtStore() {
                    return this.pickUpAtStore;
                }

                public void setPickUpAddress(List<PickUpAddress> pickUpAddress) {
                    this.pickUpAddress = pickUpAddress;
                }

                public List<PickUpAddress> getPickUpAddress() {
                    return this.pickUpAddress;
                }

                public void setStoreWideShipping(boolean storeWideShipping) {
                    this.storeWideShipping = storeWideShipping;
                }

                public boolean getStoreWideShipping() {
                    return this.storeWideShipping;
                }

                public void setAnnouncement(String announcement) {
                    this.announcement = announcement;
                }

                public String getAnnouncement() {
                    return this.announcement;
                }

                public void setRatingAvg(int ratingAvg) {
                    this.ratingAvg = ratingAvg;
                }

                public int getRatingAvg() {
                    return this.ratingAvg;
                }

                public void setTotalRating(int totalRating) {
                    this.totalRating = totalRating;
                }

                public int getTotalRating() {
                    return this.totalRating;
                }

                public void setRatingScore(int ratingScore) {
                    this.ratingScore = ratingScore;
                }

                public int getRatingScore() {
                    return this.ratingScore;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String get_id() {
                    return this._id;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getCity() {
                    return this.city;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public String getArea() {
                    return this.area;
                }

                public void setPhoneNumber(String phoneNumber) {
                    this.phoneNumber = phoneNumber;
                }

                public String getPhoneNumber() {
                    return this.phoneNumber;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getEmail() {
                    return this.email;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return this.name;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getCountry() {
                    return this.country;
                }

                public void setOwnerId(String ownerId) {
                    this.ownerId = ownerId;
                }

                public String getOwnerId() {
                    return this.ownerId;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getAddress() {
                    return this.address;
                }

                public void setCreatedAt(String createdAt) {
                    this.createdAt = createdAt;
                }

                public String getCreatedAt() {
                    return this.createdAt;
                }

                public void setUpdatedAt(String updatedAt) {
                    this.updatedAt = updatedAt;
                }

                public String getUpdatedAt() {
                    return this.updatedAt;
                }

                public void setReturnAddress(String returnAddress) {
                    this.returnAddress = returnAddress;
                }

                public String getReturnAddress() {
                    return this.returnAddress;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

                public String getAlias() {
                    return this.alias;
                }

                public void set__v(int __v) {
                    this.__v = __v;
                }

                public int get__v() {
                    return this.__v;
                }

                public void setLogoId(String logoId) {
                    this.logoId = logoId;
                }

                public String getLogoId() {
                    return this.logoId;
                }

                public void setBannerId(String bannerId) {
                    this.bannerId = bannerId;
                }

                public String getBannerId() {
                    return this.bannerId;
                }

                public void setShopDelivery(String shopDelivery) {
                    this.shopDelivery = shopDelivery;
                }

                public String getShopDelivery() {
                    return this.shopDelivery;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return this.id;
                }
            }
        }


    }
}

