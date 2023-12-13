
package com.stuffer.stuffers.models.all_restaurant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class RestaurentModels {
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("totalPage")
    @Expose
    private Integer totalPage;
    @SerializedName("array")
    @Expose
    private List<Array> array;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<Array> getArray() {
        return array;
    }

    public void setArray(List<Array> array) {
        this.array = array;
    }


    public class Array {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("description")
            @Expose
            private String description;
            @SerializedName("price")
            @Expose
            private Double price;
            @SerializedName("imageUrl")
            @Expose
            private String imageUrl;
            @SerializedName("category")
            @Expose
            private String category;
            @SerializedName("stockNum")
            @Expose
            private Integer stockNum;
            @SerializedName("infodelete")
            @Expose
            private Boolean infodelete;
            @SerializedName("type")
            @Expose
            private Integer type;
            @SerializedName("swiperImage")
            @Expose
            private String swiperImage;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public Double getPrice() {
                return price;
            }

            public void setPrice(Double price) {
                this.price = price;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public Integer getStockNum() {
                return stockNum;
            }

            public void setStockNum(Integer stockNum) {
                this.stockNum = stockNum;
            }

            public Boolean getInfodelete() {
                return infodelete;
            }

            public void setInfodelete(Boolean infodelete) {
                this.infodelete = infodelete;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }

            public String getSwiperImage() {
                return swiperImage;
            }

            public void setSwiperImage(String swiperImage) {
                this.swiperImage = swiperImage;
            }

        }
}



