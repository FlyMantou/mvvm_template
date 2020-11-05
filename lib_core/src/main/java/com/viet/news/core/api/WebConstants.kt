package com.viet.news.core.api

import com.viet.news.core.BuildConfig


/**
 *
 * @Description:
 * @Author: dichaolong
 * @CreateDate: 2018/11/20 16:52
 */
object WebConstants {

//    val CIRCLE_URL =   if (BuildConfig.TEST_ENV) "http://test.point.lanjiyin.com.cn" else "http://point.lanjiyin.com.cn"
//    val IM_URL = if (BuildConfig.TEST_ENV) "http://test.im.lanjiyin.com.cn" else "http://im.lanjiyin.com.cn"
//    val PAY_URL = if (BuildConfig.TEST_ENV) "http://test.pay.lanjiyin.com.cn" else "http://pay.lanjiyin.com.cn"
//    val USER_URL = if (BuildConfig.TEST_ENV) "http://test.user.lanjiyin.com.cn" else "http://user.lanjiyin.com.cn"
    const val IDN_URL = "https://idn.lanjiyin.com.cn"


    const val SUCCESS_CODE = "200"

    const val AES_KEY = "lanjiyin20500423"
    const val AES_IV = "lanjiyin20500423"

    const val AES_PHONE_KEY = "lanjiyin20500723"
    const val AES_PHONE_IV = "lanjiyin20500723"

    const val AES_MODE = "AES/CBC/PKCS5Padding"

    const val ERROR_TIPS = "网络异常，您的答题记录存在丢失风险"


    const val FAIL_CODE = "202"


    /**
     * 环信客服appkey
     */
    const val HX_KEY = "1434191226068414#kefuchannelapp77003"//已修改
    const val HX_TENANT_ID = "77003"//已修改
    const val HX_PROJECT_ID = "3573423"//已修改
    const val PASSWORD = "password"//已修改
    const val HUANXIN_ACCOUNT = "huanxin_account"//已修改


}