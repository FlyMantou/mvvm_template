package com.viet.news.core.domain

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.blankj.utilcode.util.StringUtils
import com.safframework.delegate.prefs.initKey
import com.safframework.delegate.prefs.int
import com.safframework.delegate.prefs.string
import com.tencent.mmkv.MMKV
import com.viet.news.core.ui.App
import kotlin.reflect.KProperty

class Settings(bucketId:String = "") {



    lateinit var mmkv: MMKV
    init {
        if (StringUtils.isEmpty(bucketId)){
            mmkv = MMKV.defaultMMKV()
        }else{
            mmkv = MMKV.mmkvWithID(bucketId)
        }
    }

    var token by mmkv.string(SP_TOKEN, isEncrypt = false)

    var roleId by mmkv.string(SP_ROLE_ID, isEncrypt = false)

    var userName  by mmkv.string(SP_USERNAME, isEncrypt = false)

    //只在User内使用，其他地方调用
    var telephone by mmkv.string(SP_TELEPHONE, isEncrypt = false)
    var userId by mmkv.string(SP_USER_ID, isEncrypt = false)
    //只在User内使用
    var zoneCode by mmkv.string(SP_ZONECODE, isEncrypt = false)
    var avatar by mmkv.string(SP_AVATAR_URL, isEncrypt = false)
    var fansCount by mmkv.int(SP_FANS_COUNT, isEncrypt = false)
    var followCount by mmkv.int(SP_FOLLOW_COUNT, isEncrypt = false)
//    var countryAbbreviation by prefs.string(SP_COUNTRY_ABBREVIATION, isEncrypt = true)


    companion object {


        const val SP_TOKEN = "sp_token"
        const val SP_ROLE_ID = "sp_roleId"
        const val SP_USERNAME = "sp_username"
        const val SP_TELEPHONE = "sp_telephone"
        const val SP_USER_ID = "sp_user_id"
        const val SP_ZONECODE = "sp_zone_code"
        const val SP_AVATAR_URL = "sp_avatar_url"
        const val SP_FANS_COUNT = "sp_fans_count"
        const val SP_FOLLOW_COUNT = "sp_follow_count"


        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var defaultInstance: Settings? = null
        private var preferenceMode = Context.MODE_PRIVATE
        private const val SP_KEY_DEFAULT = "persistent_data"

        fun create(context: Context = App.instance): Settings {
            if (defaultInstance == null) {
                synchronized(Settings::class.java) {
                    if (defaultInstance == null) {
                        defaultInstance = Settings()
                    }
                }
            }
            return defaultInstance!!
        }
    }
}
