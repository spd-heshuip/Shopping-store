package com.learn.shuip.yayashop.system;

/**
 * Created by Ivan on 15/10/5.
 */
public class Constant {

    public static final String CAMPAIGN_ID = "campaign_id";
    public static final String WARE = "ware";
    public static final String PHONE = "phone";
    public static final String PASSWD = "password";
    public static final String DES_KEY = "Cniao5_123456";
    public static final String USERMSG = "USERMSG";
    public static final String TOKEN = "Token";
    public static final int REQUEST_CODE = 1;

    public static class API{
        public static final String BASE_URL="http://112.124.22.238:8081/course_api/";

        public static final String CAMPAIGN_HOME=BASE_URL + "campaign/recommend";

        public static final String WARES = BASE_URL + "wares/hot";

        public static final String CategoryList = BASE_URL + "category/list";

        public static final String WARES_LIST=BASE_URL +"wares/list";

        public static final String BANNER=BASE_URL +"banner/query";

        public static final String WARES_CAMPAIN_LIST = BASE_URL + "wares/campaign/list";

        public static final String WARE_DETAIL = BASE_URL + "wares/detail.html";

        public static final String LOGIN = BASE_URL + "auth/login";
    }
}
