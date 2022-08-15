package com.pokedexplatzi.data.datasource.local.db

import androidx.room.*
import com.pokedexplatzi.data.model.local.Pokemon

@Dao
interface PokeDao {
    @Query("SELECT * FROM pokes LIMIT 151")
    suspend fun getItems(): List<Pokemon>

    @Query("SELECT * FROM pokes WHERE name = :name LIMIT 1")
    suspend fun getItemByName(name: String): Pokemon?

    @Query("SELECT * FROM pokes WHERE evolutionChainUrl = :evolutionChainUrl")
    suspend fun getItemsByEvolutionChainUrl(evolutionChainUrl: String): List<Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Pokemon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Pokemon)

    @Update
    suspend fun updateItem(item: Pokemon)
}