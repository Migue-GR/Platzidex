package com.pokedexplatzi.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pokedexplatzi.BuildConfig
import com.pokedexplatzi.data.model.local.Pokemon

@Database(
    version = 1,
    exportSchema = false,
    entities = [Pokemon::class]
)
abstract class AppDataBase : RoomDatabase() {
    abstract val pokeDao: PokeDao

    companion object {
        private const val DATABASE_NAME = "${BuildConfig.APPLICATION_ID}.AppDataBase"

        private fun createInstance(ctx: Context): AppDataBase =
            Room.databaseBuilder(ctx, AppDataBase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(ctx: Context) = createInstance(ctx)
    }
}