package com.nammamela.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nammamela.app.data.local.dao.BookingDao
import com.nammamela.app.data.local.dao.CastDao
import com.nammamela.app.data.local.dao.CommentDao
import com.nammamela.app.data.local.dao.PlayDao
import com.nammamela.app.data.local.dao.UserDao
import com.nammamela.app.data.local.entity.BookingEntity
import com.nammamela.app.data.local.entity.CastEntity
import com.nammamela.app.data.local.entity.CommentEntity
import com.nammamela.app.data.local.entity.PlayEntity
import com.nammamela.app.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PlayEntity::class, BookingEntity::class, CommentEntity::class, CastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun playDao(): PlayDao
    abstract fun bookingDao(): BookingDao
    abstract fun commentDao(): CommentDao
    abstract fun castDao(): CastDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "namma_mela_db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
        }
    }
}
