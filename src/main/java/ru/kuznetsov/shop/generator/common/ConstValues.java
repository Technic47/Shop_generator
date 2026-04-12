package ru.kuznetsov.shop.generator.common;

public interface ConstValues {

    String AUTH_URI = "/auth";
    String ADDRESS_URI = "/address";
    String PRODUCT_URI = "/product";
    String PRODUCT_CATEGORY_URI = "/product-category";
    String STOCK_URI = "/stock";
    String STORE_URI = "/store";
    String OPERATION_URI = "/operation";
    String ORDER_URI = "/order";
    String ORDER_STATUS_URI = "/order/status";
    String ORDER_BUCKET_URI = "/order/bucket";
    String NOTIFICATION_URI = "/notification";
    String PAYMENT_URI = "/payment";
    String SHIPMENT_URI = "/shipment";

    String ADMIN_LOGIN = "shop_admin";
    String ADMIN_PASSWORD = "admin";

    String USER_LOGIN = "shop_user";
    String USER_PASSWORD = "1999";
    String USER2_LOGIN = "shop_user2";
    String USER2_PASSWORD = "1999";
    String USER3_LOGIN = "user1";
    String USER3_PASSWORD = "new_user";
    String USER4_LOGIN = "user2";
    String USER4_PASSWORD = "new_user";

    String SELLER_LOGIN = "shop_seller";
    String SELLER_PASSWORD = "1999";
    String SELLER2_LOGIN = "shop_seller2";
    String SELLER2_PASSWORD = "1999";

    String PARAMETER_LOGIN = "login";
    String PARAMETER_PASSWORD = "password";

    int STORE_AMOUNT = 7;
    int CATEGORY_AMOUNT = 10;
    int PRODUCT_AMOUNT = 25;
    int STORE_MAX_AMOUNT = 5000;
}
