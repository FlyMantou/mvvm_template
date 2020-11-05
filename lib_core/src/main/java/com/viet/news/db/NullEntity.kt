package com.viet.news.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.viet.news.core.config.Config

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/9/12
 * @Description
 */
@Entity(tableName = Config.NULL_TABLE_NAME)
class NullEntity(
        @PrimaryKey()
        var id: String = ""
)