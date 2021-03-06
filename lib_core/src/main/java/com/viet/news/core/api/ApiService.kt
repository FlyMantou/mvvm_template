package com.viet.news.core.api

import androidx.lifecycle.LiveData
import com.viet.news.core.domain.request.*
import com.viet.news.core.domain.response.*
import io.reactivex.Maybe
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by Tony Shen on 2017/8/15.
 */
interface ApiService {

    companion object {
        const val MAGICBOX_API = "http://94.191.23.229:9090"
    }

    /**
     * 频道文章列表相关
     */

    @POST("v1/channel/updateSort")
    fun updateSort(@Body param: UpdateChannelParam): LiveData<ApiResponse<Any>>

    @POST("v1/channel/allList")
    fun getChannelAllList(): LiveData<ApiResponse<ChannelAllListResponse>>

    @POST("v1/channel/list")
    fun getChannelList(): LiveData<ApiResponse<List<ChannelList>>>

    //根据频道查询文章列表
    @POST("v1/content/list4Channel")
    fun getlist4Channel(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //查询当前关注用户发表的文章列表
    @POST("v1/content/list4follow")
    fun getlist4Follow(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //查询收藏列表
    @POST("v1/content/collectionList")
    fun getCollectionList(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //查询指定用户发布的文章列表
    @POST("v1/content/list4user")
    fun getlist4User(@Body param: ListParams): LiveData<ApiResponse<NewsListResponse>>

    //获取用户信息
    @GET("v1/user/info/{userId}")
    fun getUserInfo(@Path("userId") userId: String?): LiveData<ApiResponse<UserInfoResponse>>

    /**
     * 登录注册相关
     */
    @POST("v1/login/login")
    fun login(@Body param: LoginParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/login/login")
    fun logins(@Body param: LoginParams): Call<LoginRegisterResponse>

    @POST("v1/login/register")
    fun register(@Body param: SignInParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/login/sendSms")
    fun sendSMS(@Body param: VerifyCodeParams): LiveData<ApiResponse<Any>>

    @POST("v1/login/checkValidationCode")
    fun checkVerifyCode(@Body param: VerifyCodeParams): LiveData<ApiResponse<Any>>

    @POST("v1/login/resetPassword")
    fun setPassword(@Body param: ResetPwdParams): LiveData<ApiResponse<LoginRegisterResponse>>

    @POST("v1/feedback/add")
    fun feedback(@Body param: FeedBackParams): LiveData<ApiResponse<Any>>

    @GET("v1/content/collection/{contentId}")
    fun collection(@Path("contentId") contentId: String): LiveData<ApiResponse<Int>>

    @GET("v1/content/like/{contentId}")
    fun like(@Path("contentId") contentId: String): LiveData<ApiResponse<Int>>

    @GET("v1/user/updateNickName/{nick_name}")
    fun updateNickName(@Path("nick_name") nick_name: String): LiveData<ApiResponse<Any>>

    @GET("v1/user/follow/{followUserId}")
    fun follow(@Path("followUserId") followUserId: String?): LiveData<ApiResponse<Any>>

    @GET("v1/user/cancelFollow/{followUserId}")
    fun cancelfollow(@Path("followUserId") followUserId: String?): LiveData<ApiResponse<Any>>

    @POST("v1/user/followList")
    fun followList(@Body param: ListParams): LiveData<ApiResponse<UserInfoListResponse>>

    @POST("v1/user/fansList")
    fun fansList(@Body param: ListParams): LiveData<ApiResponse<UserInfoListResponse>>

    //修改密码
    @POST("v1/user/replacePassword")
    fun resetPwdWithOldPwd(@Body param: ResetPwdWithOldpwdParams): LiveData<ApiResponse<Any>>
    //修改手机号
    @POST("v1/user/replacePhoneNumber")
    fun resetPwdWithOldPwd(@Body param: ResetPhoneNumParams): LiveData<ApiResponse<Any>>

    //多参数上传+多图片上传
    @Multipart
    @POST("userFeedback/uploadFeedback")
    fun uploadFile(@Part("email") email: String?,
                   @Part("subject") subject: String?,
                   @Part("content") content: String?,
                   @Part part: List<MultipartBody.Part>?): Maybe<Any>

    @Multipart
    @POST("v1/user/updateAvatar")
    fun uploadFile(@Part part: MultipartBody.Part): LiveData<ApiResponse<Any>>

    //用户反馈,未登录
    @Multipart
    @POST("userFeedback/uploadFeedbackWithoutLogin")
    fun uploadFileVisitor(@Part("email") email: String?,
                          @Part("subject") subject: String?,
                          @Part("content") content: String?,
                          @Part part: List<MultipartBody.Part>?): Maybe<Any>

}
