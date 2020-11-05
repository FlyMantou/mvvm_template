package com.viet.mine.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.viet.news.core.api.ApiRepository
import com.viet.news.core.api.ApiResponse
import com.viet.news.core.config.Config
import com.viet.news.core.config.LoginEnum
import com.viet.news.core.config.VerifyCodeTypeEnum
import com.viet.news.core.domain.request.LoginParams
import com.viet.news.core.domain.request.ResetPwdParams
import com.viet.news.core.domain.request.SignInParams
import com.viet.news.core.domain.request.VerifyCodeParams
import com.viet.news.core.domain.response.LoginRegisterResponse
import com.viet.news.core.ext.uiThread
import com.viet.news.core.repository.NetworkOnlyResource
import com.viet.news.core.vo.Resource
import com.viet.news.db.DBHelper
import com.viet.news.db.FavoriteEntity
import kotlin.concurrent.thread

/**
 * @Description
 * @Author sean
 * @Email xiao.lu@magicwindow.cn
 * @Date 2018/9/10 下午1:30
 * @Version
 */
class LoginRepository : ApiRepository() {


    fun test() {
        thread {
            DBHelper.getInstance().getFavoriteDao().deleteAllSource(SimpleSQLiteQuery("DROP TABLE " + Config.FAVORITE_TABLE_NAME))
            val list = arrayListOf<FavoriteEntity>()
            for (i in 1..10000) {
                list.add(FavoriteEntity("$i",
                        "name-$i",
                        "des-$i",
                        "url-$i",
                        "avatar-$i",
                        "img1-$i 哈哈",
                        "read-$i",
                        "like-$i"))
            }
            var time = System.currentTimeMillis()
            DBHelper.getInstance().getSourceDao().insertSources(list)
            uiThread {
                ToastUtils.showShort("插入完成，用时${(System.currentTimeMillis()-time)/1000}秒")
                LogUtils.e("huanghai","插入10000条数据用时：${(System.currentTimeMillis()-time)}毫秒")
            }
        }
    }
    fun test2() {
        thread {
            var time = System.currentTimeMillis()
            val list = DBHelper.getInstance().getSourceDao().getAllNewsSource()
            uiThread {
                LogUtils.e("huanghai","获取全部数据用时：${(System.currentTimeMillis()-time)}毫秒")
            }
        }

    }
    fun test3() {
        thread {
            var time = System.currentTimeMillis()
            val count = DBHelper.getInstance().getSourceDao().getCount()
            uiThread {
                LogUtils.e("huanghai","获取数量用时：${(System.currentTimeMillis()-time)}毫秒","count:",count)
            }
        }
    }


    /**
     * login_type:登录类型 1 手机号密码登录 2 手机号验证码登录 3 facebook登录
     */
    fun loginByPwd(phoneNumber: String?, password: String?): LiveData<Resource<LoginRegisterResponse>> {
        val params = LoginParams()
        params.phone_number = phoneNumber
        params.password = password
        params.setType(LoginEnum.PASSWORD)
        return object : NetworkOnlyResource<LoginRegisterResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.login(params)
        }.asLiveData()
    }

    /**
     * login_type:登录类型 1 手机号密码登录 2 手机号验证码登录 3 facebook登录
     */
    fun loginBySMS(phoneNumber: String?, vCode: String?): LiveData<Resource<LoginRegisterResponse>> {
        val params = LoginParams()
        params.phone_number = phoneNumber
        params.validation_code = vCode
        params.setType(LoginEnum.VALIDATION_CODE)
        return object : NetworkOnlyResource<LoginRegisterResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.login(params)
        }.asLiveData()
    }

    /**
     * login_type:登录类型 1 手机号密码登录 2 手机号验证码登录 3 facebook登录
     */
    fun loginByFacebook(oauthUserId: String?, oauthToken: String?, oauthExpires: String?): LiveData<Resource<LoginRegisterResponse>> {
        val params = LoginParams()
        params.oauth_user_id = oauthUserId
        params.oauth_token = oauthToken
        params.oauth_expires = oauthExpires
        params.setType(LoginEnum.FACEBOOK)
        return object : NetworkOnlyResource<LoginRegisterResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.login(params)
        }.asLiveData()
    }

    /**
     * 【发送】 验证码
     */
    fun sendSMS(phoneNumber: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource<Any>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.sendSMS(params)
        }.asLiveData()
    }

    /**
     * 【校验】 验证码
     */
    fun checkVerifyCode(phoneNumber: String?, verifyCode: String?, zone_code: String?, type: VerifyCodeTypeEnum): LiveData<Resource<Any>> {
        val params = VerifyCodeParams()
        params.phone_number = phoneNumber
        params.validation_code = verifyCode
        params.zone_code = zone_code
        params.setType(type)
        return object : NetworkOnlyResource<Any>() {
            override fun createCall(): LiveData<ApiResponse<Any>> = apiInterface.checkVerifyCode(params)
        }.asLiveData()
    }

    /**
     * 注册
     */
    fun signIn(params: SignInParams): LiveData<Resource<LoginRegisterResponse>> =
            object : NetworkOnlyResource<LoginRegisterResponse>() {
                override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.register(params)
            }.asLiveData()

    /**
     * 设置密码
     */
    fun setPassword(phoneNumber: String?, verifyCode: String?, password: String?): LiveData<Resource<LoginRegisterResponse>> {
        val params = ResetPwdParams()
        params.phone_number = phoneNumber
        params.validation_code = verifyCode
        params.password = password
        return object : NetworkOnlyResource<LoginRegisterResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginRegisterResponse>> = apiInterface.setPassword(params)
        }.asLiveData()
    }
}