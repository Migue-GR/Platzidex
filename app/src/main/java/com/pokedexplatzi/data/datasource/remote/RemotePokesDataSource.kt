package com.pokedexplatzi.data.datasource.remote

import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.data.model.remote.toLocalObject
import com.pokedexplatzi.data.model.remote.toLocalObjects

class RemotePokesDataSource(private val pokeService: PokeService) {
    suspend fun getPokes(): List<Pokemon> {
        val response = pokeService.getPokes()
        val remotePokes = response.results?.filterNotNull() ?: listOf()
        return remotePokes.toLocalObjects()
    }

    suspend fun getPokemonDetail(name: String): Pokemon {
        val response = pokeService.getPokemonDetail(name)
        return response.toLocalObject()
    }

    suspend fun getEvolutionChain(evolutionChainUrl: String): List<String> {
        val response = pokeService.getEvolutionChain(evolutionChainUrl)
        return response.toLocalObjects()
    }
}