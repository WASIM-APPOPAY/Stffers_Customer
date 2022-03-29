package com.stuffer.stuffers.api;



import com.stuffer.stuffers.models.shop_model.ItemDetails;
import com.stuffer.stuffers.models.shop_model.ShopItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainShopAPIInterface<R extends Retrofit> {
    @GET(Constants.SHOP_CATEGORY_ITEM)
    Call<ShopItem> getShopCategoryItems();

    /*@GET(Constants.GET_LOGIN_DETAILS)
    Call<LoginResponse> getLoginDetails(
            @Path("mobileno") long mobileNumber,
            @Path("areacode") int areacode,
            @Query("access_token") String token
    );*/
    //https://cerca24.com/v1/products/search?
    // page=1&
    // take=1&
    // sort=&
    // sortType=&
    // q=&
    // categoryId=5f1ad1385a679b68c152a957&
    // shopId=&
    // featured=&
    // hot=&
    // bestSell=&
    // dailyDeal=&
    // soldOut=&
    // discounted=
    @GET(Constants.SHOP_CATEGORY_ITEM_DETAILS)
    Call<ItemDetails> getShopCategoryItemsDetails(@Query("page") String mPage,
                                                  @Query("take") String mTake,
                                                  @Query("sort") String mSort,
                                                  @Query("sortType") String sortType,
                                                  @Query("q") String mQ,
                                                  @Query("categoryId") String mCategoryId,
                                                  @Query("shopId") String mShopId,
                                                  @Query("featured") String mFeatured,
                                                  @Query("hot") String mHot,
                                                  @Query("bestSell") String mBestSell,
                                                  @Query("dailyDeal") String mDailyDeal,
                                                  @Query("soldOut") String mSoldOut,
                                                  @Query("discounted") String mDiscounted);



}
