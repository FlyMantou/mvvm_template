package com.viet.news.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.viet.news.core.config.Config

/**
 * @Author huanghai
 * @Email 1165441461@qq.com
 * @Date 2020/11/5
 * @Description
 */
@Entity(indices = [Index(value = ["name"])],tableName = Config.FAVORITE_TABLE_NAME)
class FavoriteEntity(
        @PrimaryKey
        var id: String = "",
        var name: String? = "",
        var description: String? = "",
        var url: String? = "",
        var avatarUrl: String? = "",
        var img1Url: String? = "",
        var img2Url: String? = "",
        var img3Url: String? = "",
        var readNo: String? = "",
        var likeNo: String? = ""
)