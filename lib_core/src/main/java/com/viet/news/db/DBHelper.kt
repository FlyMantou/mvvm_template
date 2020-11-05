package com.viet.news.db

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.viet.news.core.config.Config
import com.viet.news.core.ui.App


/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/9/12
 * @Description
 */
@Database(entities = [FavoriteEntity::class, NullEntity::class], version = 1)
abstract class DBHelper : RoomDatabase() {
    abstract fun getSourceDao(): SourceDao
    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun getNull(): OnlyNullDao

    companion object {
        private var db: DBHelper? = null

        fun getInstance(context: Context = App.instance): DBHelper {
            if (db == null) {
                //数据库升级
//                db = Room.databaseBuilder(context, DBHelper::class.java, Config.DB_NAME).addMigrations(MIGRATION_1_2).build()  //数据库升级
                db = Room.databaseBuilder(context, DBHelper::class.java, Config.DB_NAME).build()
            }
            return db!!
        }



        var MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(@NonNull database: SupportSQLiteDatabase) {
                database.execSQL("alter table user add column sex text")
            }
        }
    }

}