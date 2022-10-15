package com.stuffer.stuffers.utils;




import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public static MerchantInfoBean merchantInfoBean;

    public static MerchantInfoBean getMerchantInfoBean() {
        if (merchantInfoBean == null) {
            merchantInfoBean = new MerchantInfoBean();
        }
        return merchantInfoBean;
    }

    /**
        点击的下标位置
     */
    public static int index = -1;

    public static int clickIndex = -1;

    public static boolean isNeedRefresh = false;

    public static boolean isNeedRefreshForList = false;

    public static String merchantAccount = "";

    public static String ownMerchantAccount = "";

    //position relative
    public static String position = "";
    public static String curPosition = "";
    public static String curLatLog = "";
    public static String newPosition = "";
    public static String newLatLog = "";

    //input relative
    public static String curInputType = "";
    public static String curInputContent = "";
}
