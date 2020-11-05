package com.viet.news.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.viet.news.core.config.Config

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/9/12
 * @Description
 */
@Dao
interface SourceDao {

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

//    fun insertSources(source: List<Source>) {
//
//        insertSources(*sourceEntityArray.toTypedArray())
//    }
}