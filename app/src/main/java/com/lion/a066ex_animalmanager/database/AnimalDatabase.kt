package com.lion.a066ex_animalmanager.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lion.a066ex_animalmanager.dao.AnimalDao
import com.lion.a066ex_animalmanager.vo.AnimalVo

@Database(entities = [AnimalVo::class], version = 1, exportSchema = true)
abstract class AnimalDatabase : RoomDatabase() {
    abstract fun animalDAO() : AnimalDao

    companion object{
        var animalDatabase:AnimalDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : AnimalDatabase?{
            synchronized(AnimalDatabase::class){
                animalDatabase = Room.databaseBuilder(
                    context.applicationContext, AnimalDatabase::class.java,
                    "Animal.db"
                ).build()
            }
            return animalDatabase
        }

        fun destroyInstance(){
            animalDatabase = null
        }
    }
}