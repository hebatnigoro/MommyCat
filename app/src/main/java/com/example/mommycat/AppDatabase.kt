package com.example.mommycat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

@Database(entities = [User::class, Cat::class, CartData::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun catDao(): CatDao
    abstract fun cartDataDao(): CartDataDao
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE cats_new (
                id TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                price REAL NOT NULL,
                imageUrl TEXT NOT NULL,
                PRIMARY KEY(id)
            )
        """)
                database.execSQL("DELETE FROM cats")
                database.execSQL("""
            INSERT OR REPLACE INTO cats_new (id, name, description, price, imageUrl)
            SELECT id, name, description, price, imageUrl FROM cats
        """)
                database.execSQL("DROP TABLE cats")
                database.execSQL("ALTER TABLE cats_new RENAME TO cats")
            }
        }



        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mommycat_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
