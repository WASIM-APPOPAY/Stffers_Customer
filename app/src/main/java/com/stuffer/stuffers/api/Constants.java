package com.stuffer.stuffers.api;


public class Constants {
    //https://prodapi.appopay.com/api/users/savecardtype
    public static final String APPOPAY_BASE_URL = "https://prodapi.appopay.com/";
    //public static final String APPOPAY_BASE_URL = "https://labapi.appopay.com/";
    public static final String APPOPAY_BASE_NODE_URL = "https://prodapi-node.appopay.com/";
    //public static final String APPOPAY_BASE_NODE_URL = "https://labapi-node.appopay.com/";
    //public static final String BASE_UNION_PAY = "https://prodapi-union.appopay.com/";
    public static final String BASE_UNION_PAY = "https://labapi-union.appopay.com/";
    //public static final String UNION_PAY = "https://prodapi-union.appopay.com/scis/switch/";
    public static final String UNION_PAY = "https://labapi-union.appopay.com/scis/switch/";
    public static final String POST_JWE = "getJWEToken";
    public static final String POST_JWS = "getJWSToken";
    public static final String POST_QR_BAR_CODE = "generate/qrcodeandbarcode";
    public static final String CARD_FACE_DOWNLOAD = "cardfacedownloading";
    public static final String UNION_PAY_JWS = "api/configurations/generateJWS";
    public static final String POST_CARD_ENROLLMENT = "cardenrollment";
    public static final String POST_JWE_CONTENT = "parseJWEContent";
    public static final String POST_ENROLL_INFO = "getEnrolledCardInfo";
    public static final String QRCODE_DECODE = "appopay-scisgw/merchantqrcdecode";
    public static final String CONSUMER_QROCR = "appopay-scisgw/cpqrcgeneration";
    public static final String GET_MAPPING = "api/users/mobilemapping/{newnumber}/CUSTOMER";
    public static final String GET_TOKEN = "api/oauth/token";
    public static final String GET_LOGIN_DETAILS = "api/users/findbyMobile/{mobileno}/{areacode}/CUSTOMER";
    public static final String GET_COUNTRY_CODE = "api/configurations/country";
    public static final String POST_GENERATE_OTP = "api/otp/generateotp";
    public static final String POST_GENERATE_SMS = "api/sms/send";
    public static final String GET_VERIFY_SMS = "api/sms/verify/opt/{givenOtp}";

    public static final String POST_VERIFY_OTP = "api/otp/verifyOTP";
    public static final String CHECK_MOBILE_NUMBER = "api/users/checkmobile/{phone_number}/CUSTOMER";
    public static final String CHECK_EMIAL_ID = "api/users/verifyemail/{user_email}";
    public static final String CHECK_EMIAL_ID_NEW = "api/users/verifyuseremail";
    public static final String CREATE_USER = "api/users/createnewuser";


    public static final String GET_PROFILE = "api/users/findbyMobile/{mobileno}/{areacode}/CUSTOMER"; //change

    public static final String GET_PROFILE_MERCHANT = "api/users/findbyMobile/{mobileno}/{areacode}/MERCHANT";//change

    public static final String PUT_UPDATE_PROFILE = "api/users";//change
    public static final String POST_UPDATE_PROFILE = "api/users/updateprofile";
    public static final String POST_SETTING = "api/users/userchangepassword?";
    public static final String GET_CURRENCY = "api/configurations/currency";

    public static final String POST_CREATE_ACCOUNT = "api/wallet/createaccount?";// no need to change here

    public static final String POST_SAVE_CARD_TYPE = "api/users/savecardtype";//change

    public static final String POST_CHECK_VERSATEDCARD = "api/cardidentifications/checkversateccard";

    public static final String GET_CARDS = "api/users/getcards/{userid}";//change

    public static final String GET_VAULTBYID = "v2/getbyVaultID";

    public static final String POST_PRODUCT = "v2/getProducts";
    //public static final String POST_PRODUCT1 = "api/wallet/products/v2/{productType}/{countryCode}";
    public static final String POST_PRODUCT1 = "api/wallet/products/v2";


    //public static final String GET_CONVERSIONS = "api/configurations/currencyconversions/PAB?";
    public static final String GET_CONVERSIONS = "api/configurations/currencyconversions/{destinationCurrency}";//change

    public static final String GET_COMMISSIONS = "api/configurations/comissiontype/WALLET-TOPUP";//change

    public static final String GET_COMMISSIONS_MERCHANT = "api/configurations/comissiontype/WALLET-MERCHANT";//change

    //https://appopay.com/api/configurations/comissiontype/WALLET-OTHER-WALLET?

    public static final String GET_COMMISSION_TRANSFER = "api/configurations/comissiontype/WALLET-OTHER-WALLET";//change


    public static final String POST_RECHARGE_TOPUP = "api/wallet/top-up/v2";//change
    //https://appopay.com/api/wallet/transferfund?
    public static final String POST_TRANSFER_FUND = "api/wallet/transferfund";//change

    public static final String POST_MERCHANT_PAYMENT = "api/wallet/merchantPayment";//change

    public static final String GET_BASE_CONVERSION = "https://api.exchangeratesapi.io/latest?base=USD";

    public static final String POST_TRANSACTION_DETAILS = "api/wallet/transactions";//change

    public static final String GET_BANK_NAMES = "api/configurations/banknames/{id}";//change
    public static final String GET_ACCOUNT_TYPE = "api/configurations/accounttypes";//change
    public static final String GET_BANK_CURRENCY = "api/configurations/bankcurrency/{id}";//change
    public static final String POST_UPDATE_PASSWORD = "api/users/userchangepassword";//change

    public static final String GET_BANK_FUND_COMMISSION = "api/configurations/comissiontype/BANK-DEPOSIT";//change
    public static final String GET_CARD_FUND_COMMISSION = "api/configurations/comissiontype/CARD-WALLET";//change

    public static final String POST_ADD_WALLET_BY_CARD = "api/wallet/addfund";//change
    //https://appopay.com/api/users/finduser/{mobilenumber}/{areacode}
    public static final String GET_FINDUSER = "api/users/finduser/{mobilenumber}/{areacode}";
    //https://appopay.com/api/users/resetpassword
    public static final String POST_FORGOT_PASSWORD = "api/users/resetpassword";

    public static final String POST_RESET_PASSWORD = "api/users/password/update/v2/{areacode}/{mobile_no}/CUSTOMER/{password}";


    public static final String GET_ALL_GIFTCARD_LIST = "v2/getProductListForTopupAndEgift";
    public static final String POST_SENT_LUNEX_GIFT_CARD = "api/wallet/giftCardTopUp";//change
    //https://appopay.com/api/customers/uploadFile
    public static final String POST_UPLOAD_FILE = "api/customers/uploadFile";


    public static final String SERVER_URL = "http://workoholicks.com/demo/smartpay/admin/";

    public static final String USER_MOBILE_REGISTRATION = "Smartpay/user_phone_no_registration";
    public static final String VERIFY_USER_MOBILE = "Smartpay/verfiy_user_phone_no";
    public static final String USER_SIGNUP = "Smartpay/user_signup";
    public static final String USER_LOGIN = "Smartpay/user_login";


    public static final String GET_ALL_SELLER_PRODUCTS = "Product/get_all_product";
    public static final String GET_ALL_CATEGORIES = "Webservice_App/get_cat";
    public static final String GET_ALL_SUB_CAT = "Webservice_App/get_sub_cat/{cat_id}";

    public static final String GET_ALL_SERVICE_CATEGORIES = "Webservice_App/get_service_cat";

    public static final String GET_SUBCAT_PRODUCTS = "Product/get_all_product_subcat";

    public static final String GET_SINGLE_PRODUCT_DETAIL = "Product/get_single_product";

    public static final String BOOK_ORDER = "Webservice_App/bookorder";

    public static final String BOOK_APPOINTMENT = "Webservice_App/service_booking";


    public static final String UPLOAD_IMAGE_FOLDER = SERVER_URL + "assets/uploads/";
    public static final String PRODUCT_IMAGE_PATH = SERVER_URL + "assets/uploads/product_images/";
    public static final String SERVICE_IMAGE_PATH = SERVER_URL + "assets/uploads/service_images/";

    public static final String SELLER_IMAGE_PATH = SERVER_URL;

    public static final String GET_ALL_NEARBY_SELLERS = "Webservice_App/get_all_top_sellers";

    public static final String GET_ALL_SELLER_SERVICES = "Services/get_all_services";

    public static final String GET_ALL_NEARBY_SELLERS_FROM_CATEGORY = "Webservice_App/get_all_sellers_cat";


    public static final String GET_ALL_YOUR_ORDERS = "Webservice_App/userorders";

    public static final String GET_ALL_YOUR_APPOINTMENTS = "Webservice_App/userbooking";

    public static final String GET_ALL_TRENDING_PRODUCTS = "Product/get_all_trending_product";

    public static final String GET_TOP_BRANDS = "Webservice_App/get_all_brands_with_products";

    public static final String RATE_REVIEW_SELLER_URL = "Webservice_App/rate_seller";
    public static final String GET_SELLER_REVIEW_LIST = "Webservice_App/get_seller_review";

    public static final String SEARCH_ALL_PRODUCTS = "Webservice_App/searchproduct";

    public static final String ADD_PRODUCT_REVIEW = "Webservice_App/rate_product";
    public static final String GET_PRODUCT_REVIEW = "Webservice_App/get_all_product_reviews";

    public static final String GET_ACCOUNT_DETAILS = "Webservice_App/get_user_account_details";

    public static final String EDIT_USER_ACCOUNT = "Webservice_App/edit_user_account";

    public static final String FORGOT_PASSWORD_REQUEST = "Webservice_App/forgot_password";
    public static final String SEND_PASSWORD_OTP = "Webservice_App/send_change_password_otp";
    public static final String CHANGE_PASSWORD_URL = "Webservice_App/change_password";

    public static final String GET_SERVICES_BY_CATEGORY = "Webservice_App/get_list_of_services";

    public static final String GET_HOMEPAGE_SLIDER = "Webservice_App/get_all_homepage_slider";
    public static final String GET_HOMEPAGE_SLIDER2 = "Webservice_App/get_all_homepage_slider2";


    public static final String GET_REAL_TIME_DELIVERY_TRACKING = "https://api.trackingmore.com/v2/trackings/realtime";

    public static final String GET_SHIPMENT_DETAILS = "Webservice_App/get_shipment_details";

    public static final String GET_APP_SETTINGS = "Webservice_App/get_all_app_settings";

    public static final String GET_ALL_FLASH_DEALS = "Webservice_App/get_all_flash_deals";
    public static final String GET_FLASH_DEAL_PRODUCTS = "Webservice_App/get_all_flash_deal_products";

    public static final String GET_ALL_FASHION_OFFERS = "Webservice_App/get_fashion_offers";
    public static final String GET_ALL_TOP_OF_THE_DAY = "Webservice_App/get_top_of_the_day";

    public static final String GET_TOP_SHOPPING_OFFERS = "Webservice_App/get_top_shopping_offers";

    public static final String GET_ALL_PRODUCTS_FROM_CHILD_CATEGORY = "Product/get_all_product_child_cat";
    public static final String GET_ALL_PRODUCTS_FROM_BRAND = "Product/get_all_products_by_brand";

    public static final String GET_ALL_PRODUCTS_FROM_CHILD_CATEGORY_LIMITED = "Product/get_all_product_child_cat_limited";


    //For Adding to Product To Cart

    public static final String ADD_PRODUCT_TO_CART = "Neworders/add_product_to_cart";
    public static final String GET_ONLINE_CART_LIST = "Neworders/get_all_cart_list";
    public static final String UPDATE_INCREASE_CART = "Neworders/update_increase_product_qunatity";
    public static final String UPDATE_DECREASE_CART = "Neworders/update_decrease_product_qunatity";
    public static final String UPDATE_DELETE_CART = "Neworders/delete_product_from_cart";

    public static final String NEW_ORDER_BOOKING = "Neworders/add_new_booking";

    public static final String GET_CATEGORY_DETAIL = "Webservice_App/get_products_by_category";

    public static final String GET_ALL_CHILD_CATEGORIES = "Webservice_App/get_child_categories_by_category";

    public static final String GET_ALL_BRANDS = "Webservice_App/get_all_brands";


    //Smart Pay New API
    public static final String ADD_MONEY_TO_WALLET = "Smartpay/add_money_to_wallet";
    public static final String SEND_MONEY_TO_USERS = "Smartpay/send_money_to_user_wallet";
    public static final String GET_WALLET_TRANSACTIONS = "Smartpay/get_all_wallet_transactions";

    public static final String PLACE_MOBILE_RECHARGE_REQUEST = "Smartpay/add_mobile_recharge_request";

    //https://tu-api.lunextelecom.com/seller/COOPRED/products/?country_code=&product_code=
    public static final String GET_CARD = "/seller/COOPRED/products/{country_code}/{product_code}";

    //https://appopay.com/api/wallet/getgiftcardnumber/63516303/507/10/1/welcome?

    public static final String SENT_APPOPAY_GIFT_CARD = "api/wallet/getgiftcardnumber/{recMobileNumber}/{revCountryCode}/{amount}/{country}/{message}?";
    public static final String CERCA24 = "https://cerca24.com/";
    public static final String SHOP_CATEGORY_ITEM = "v1/products/categories/tree";
    //https://cerca24.com/v1/products/search?page=1&take=1&sort=&sortType=&q=&categoryId=5f1ad1385a679b68c152a957&shopId=&featured=&hot=&bestSell=&dailyDeal=&soldOut=&discounted=
    public static final String SHOP_CATEGORY_ITEM_DETAILS = "v1/products/search?";

    public static final String ALL_ITEM = "api/loyalty/product/page/all?";
    public static final String ALL_ITEM_SHOP = "api/loyalty/product/page/shop?";
    public static final String ALL_ITEM_FOOD = "api/loyalty/product/page/food?";
    public static final String ALL_ITEM_ENTERTAINMENT = "api/loyalty/product/page/entertainment?";


    public static final int REQUEST_CHECK_SETTINGS = 999;
    public static final int LOCATION_REQUEST_CODE = 777;
    public static final String PARAM1 = "param1";
    public static final String PARAM2 = "param2";

    public static final int CAPTURE_IMAGE_REQUEST = 8787;
    public static final String CARDINFO = "cardInfo";
    //http://54.253.233.223:8889/ocr/credit
    public static final String BASE_OCR = "http://3.141.54.113:8889/";
    public static final String BASE_LOAN = "https://en.corecoop.net";
    //public static final String BASE_OCR = "http://54.253.233.223:8889/";
    public static final String OCR_ID = "ocr/idcard_base64";
    public static final String OCR_BANK = "ocr/credit_base64";
    //http://3.141.54.113:8889/ocr/credit_base64


    public static final String ERRORCODE = "errorCode";
    public static final String CUSTOMERQRCODE = "api/qrcode/customer/{customerId}";
    public static final String CUSTOMERDYNAMICQRCODE = "api/qrcode/emv/customer/dynamic/v2/{userId}/{amount}/{isImage}";


    public static final String USERAVATAR = "api/users/avatar/update";
    public static final String TERM_AND_CONDITIONS = "api/term-condition";
    public static final String GET_MERCHANT_SHOPTYPE = "api/seller/{shopType}";
    public static final String GET_TRANSACTION_PIN = "api/users/update-trx-pin/{userId}/{pin}";
    public static final String GET_MAPPING2 = "api/users/mobileMapping";
    public static final String GET_MAPPING3 = "api/users/mobilemapping/{areacode}/{mobilenumber}/{usertype}";

    public static final String GET_MERCHANT_BUSINESSTYPES = "api/seller/businessTypes";
    public static final String GET_ALL_RESTAURANT = "api/odrRestaurant/pageRestaurant?";
    public static final String GET_RESTAURANT_ITEMS = "api/odrGoods/listAll?";
    public static final String IS_USER_REGISTER_OR_PROFILE = "/api/wSignUp/MyProfile";
    public static final String LOAN_CITY_LIST = "/api/wMasters/CityList";
    public static final String LOAN_RIGESTER = "/api/wSignUp/Register";

    public static final String LOAN_SET_PROFILE = "/api/wSignUp/UpdateProfileImage";
    public static final String LOAN_SET_ID = "/api/wSignUp/UpdateIDCARDImage";
    public static final String LOAN_SET_PAYSLIP = "/api/wSignUp/UpdatePAYSLIPImage";

    public static final String GET_LIST_RESTAURANT = "api/odrRestaurant/all";
}
