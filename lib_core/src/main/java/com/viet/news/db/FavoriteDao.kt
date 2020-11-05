package com.viet.news.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.viet.news.core.config.Config

/**
 * @Author huanghai
 * @Email 1165441461@qq.com
 * @Date 2020/11/5
 * @Description
 */
@Dao
interface FavoriteDao {

    @Query("SELECT * FROM " + Config.FAVORITE_TABLE_NAME)
    fun getAllNewsSource(): LiveData<List<FavoriteEntity>>

    @Query("SELECT COUNT(id) FROM " + Config.FAVORITE_TABLE_NAME + " WHERE img1Url = '哈哈'")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(favorite: List<FavoriteEntity>)

    @Delete
    fun deleteSource(favorite: List<FavoriteEntity>)


    @RawQuery
    fun deleteAllSource(sql: SupportSQLiteQuery):Cursor

}