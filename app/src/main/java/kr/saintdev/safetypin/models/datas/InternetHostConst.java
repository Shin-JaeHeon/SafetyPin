package kr.saintdev.safetypin.models.datas;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public interface InternetHostConst {
    String SERVER_HOST = "https://letscoding.kr:8888/api/v1/";

    /**
     * 06.10
     * User 가 Account 관련 조작을 가할때 Host
     */
    String ACCOUNT_REGISTER = SERVER_HOST + "account/register";
    String ACCOUNT_LOGIN = SERVER_HOST + "account/login";

    /**
     * User 가 Pin 관련 조작을 가할 때
     */
    String MY_PIN_REQUEST = SERVER_HOST + "pin/mypin";
}
