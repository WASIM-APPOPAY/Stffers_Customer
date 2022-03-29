package com.stuffer.stuffers.models.shop_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;


public class ShopItem {
    @SerializedName("code")
    @Expose
    public int code;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public List<Datum> data;
    @SerializedName("error")
    @Expose
    public boolean error;

    public class Datum {
        @SerializedName("metaSeo")
        @Expose
        public MetaSeo metaSeo;
        @SerializedName("totalProduct")
        @Expose
        public int totalProduct;
        @SerializedName("ordering")
        @Expose
        public int ordering;
        @SerializedName("_id")
        @Expose
        public String _id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("alias")
        @Expose
        public String alias;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("mainImage")
        @Expose
        public Object mainImage;
        @SerializedName("parentId")
        @Expose
        public Object parentId;
        @SerializedName("createdAt")
        @Expose
        public Date createdAt;
        @SerializedName("updatedAt")
        @Expose
        public Date updatedAt;
        @SerializedName("__v")
        @Expose
        public int __v;
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("children")
        @Expose
        public List<Child> children;

        public class Child {
            @SerializedName("metaSeo")
            @Expose
            public MetaSeo2 metaSeo;

            @SerializedName("totalProduct")
            @Expose
            public int totalProduct;
            @SerializedName("ordering")
            @Expose
            public int ordering;
            @SerializedName("_id")
            @Expose
            public String _id;
            @SerializedName("name")
            @Expose
            public String name;
            @SerializedName("alias")
            @Expose
            public String alias;
            @SerializedName("description")
            @Expose
            public String description;
            @SerializedName("mainImage")
            @Expose
            public Object mainImage;
            @SerializedName("parentId")
            @Expose
            public Object parentId;
            @SerializedName("createdAt")
            @Expose
            public Date createdAt;
            @SerializedName("updatedAt")
            @Expose
            public Date updatedAt;
            @SerializedName("__v")
            @Expose
            public int __v;
            @SerializedName("id")
            @Expose
            public String id;

            public class MetaSeo2 {
                public String keywords;
                public String description;
            }

            public MetaSeo2 getMetaSeo() {
                return metaSeo;
            }

            public void setMetaSeo(MetaSeo2 metaSeo) {
                this.metaSeo = metaSeo;
            }

            public int getTotalProduct() {
                return totalProduct;
            }

            public void setTotalProduct(int totalProduct) {
                this.totalProduct = totalProduct;
            }

            public int getOrdering() {
                return ordering;
            }

            public void setOrdering(int ordering) {
                this.ordering = ordering;
            }

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAlias() {
                return alias;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Object getMainImage() {
                return mainImage;
            }

            public void setMainImage(Object mainImage) {
                this.mainImage = mainImage;
            }

            public Object getParentId() {
                return parentId;
            }

            public void setParentId(Object parentId) {
                this.parentId = parentId;
            }

            public Date getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(Date createdAt) {
                this.createdAt = createdAt;
            }

            public Date getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(Date updatedAt) {
                this.updatedAt = updatedAt;
            }

            public int get__v() {
                return __v;
            }

            public void set__v(int __v) {
                this.__v = __v;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }

        public class MetaSeo {
            public String keywords;
            public String description;
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Datum> getData() {
        return data;
    }

    public boolean isError() {
        return error;
    }
}