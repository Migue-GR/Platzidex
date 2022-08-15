package com.pokedexplatzi.data.datasource.local

import com.pokedexplatzi.data.datasource.local.db.PokeDao
import com.pokedexplatzi.data.model.local.Pokemon

class LocalPokesDataSource(private val pokeDao: PokeDao) {
    suspend fun savePokes(items: List<Pokemon>): Boolean {
        pokeDao.insertItems(items)
        return true
    }

    suspend fun savePokemon(item: Pokemon): Boolean {
        pokeDao.insertItem(item)
        return true
    }

    suspend fun getPokes(): List<Pokemon> {
        return pokeDao.getItems()
    }

    suspend fun getPokemonDetail(name: String): Pokemon? {
        return pokeDao.getItemByName(name)
    }

    suspend fun updateItem(pokemon: Pokemon): Boolean {
        pokeDao.updateItem(pokemon)
        return true
    }

    suspend fun getEvolutionChain(evolutionChainUrl: String): List<Pokemon> {
        return if (evolutionChainUrl.isNotEmpty()) {
            pokeDao.getItemsByEvolutionChainUrl(evolutionChainUrl)
        } else {
            listOf()
        }
    }
}