package com.pokedexplatzi.data.repository

import com.pokedexplatzi.data.datasource.local.LocalPokesDataSource
import com.pokedexplatzi.data.datasource.remote.RemotePokesDataSource
import com.pokedexplatzi.data.model.enums.ErrorCode
import com.pokedexplatzi.data.model.enums.ErrorCode.*
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.utils.InternetUtils
import com.pokedexplatzi.utils.PokedexException

class PokeRepository(
    private val remoteDataSource: RemotePokesDataSource,
    private val localDataSource: LocalPokesDataSource,
    private val internetUtils: InternetUtils
) {
    suspend fun getPokes(theAppNeedsToShowTheDataFromCache: Boolean): List<Pokemon> {
        return when {
            theAppNeedsToShowTheDataFromCache -> {
                getLocalPokes()
            }
            internetUtils.isThePhoneConnectedToTheInternet() -> {
                val pokes = remoteDataSource.getPokes()
                if (pokes.isNullOrEmpty()) {
                    throw PokedexException(POKEMON_LIST_NOT_FOUND)
                }
                localDataSource.savePokes(pokes)
                pokes
            }
            else -> {
                getLocalPokes()
            }
        }
    }

    private suspend fun getLocalPokes(): List<Pokemon> {
        val pokes = localDataSource.getPokes()
        if (pokes.isEmpty()) {
            throw PokedexException(POKEMON_LIST_NOT_FOUND)
        }
        return pokes
    }

    suspend fun getPokemonDetail(name: String): Pokemon {
        return if (internetUtils.isThePhoneConnectedToTheInternet()) {
            val pokemon = remoteDataSource.getPokemonDetail(name)
            localDataSource.updateItem(pokemon)
            pokemon
        } else {
            localDataSource.getPokemonDetail(name) ?: throw PokedexException(
                POKEMON_DETAIL_NOT_FOUND
            )
        }
    }

    suspend fun getEvolutionChain(evolutionChainUrl: String): List<Pokemon> {
        return if (internetUtils.isThePhoneConnectedToTheInternet()) {
            val evolutionChain = remoteDataSource.getEvolutionChain(evolutionChainUrl)
            val pokes = mutableListOf<Pokemon>()
            evolutionChain.forEach { name ->
                val pokemon = remoteDataSource.getPokemonDetail(name)
                localDataSource.savePokemon(pokemon)
                pokes.add(pokemon)
            }
            pokes
        } else {
            val evolutionChain = localDataSource.getEvolutionChain(evolutionChainUrl)
            if (evolutionChain.isEmpty()) {
                throw PokedexException(EVOLUTION_CHAIN_NOT_FOUND)
            }
            evolutionChain
        }
    }
}