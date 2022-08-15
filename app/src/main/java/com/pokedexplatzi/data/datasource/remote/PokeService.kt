package com.pokedexplatzi.data.datasource.remote

import com.pokedexplatzi.data.model.remote.EvolutionChainResponse
import com.pokedexplatzi.data.model.remote.PokeListResponse
import com.pokedexplatzi.data.model.remote.RemotePokemon
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeService {
    @GET("pokemon")
    suspend fun getPokes(@Query("limit") limit: Int = 151): PokeListResponse

    @GET("pokemon-species/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String): RemotePokemon

    @GET
    suspend fun getEvolutionChain(@Url url: String): EvolutionChainResponse
}