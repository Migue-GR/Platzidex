package com.pokedexplatzi.domain

import com.pokedexplatzi.data.datasource.local.LocalPokesDataSource
import com.pokedexplatzi.data.datasource.remote.RemotePokesDataSource
import com.pokedexplatzi.data.model.enums.ErrorCode.POKEMON_DETAIL_NOT_FOUND
import com.pokedexplatzi.data.model.enums.ErrorCode.UNKNOWN_ERROR
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.data.repository.PokeRepository
import com.pokedexplatzi.utils.InternetUtils
import com.pokedexplatzi.utils.PokedexException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetPokemonDetailUseCaseTest {
    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    private lateinit var repository: PokeRepository
    private lateinit var localDataSource: LocalPokesDataSource
    private lateinit var remoteDataSource: RemotePokesDataSource
    private lateinit var internetUtils: InternetUtils
    private lateinit var dataFromTheLocalDataSource: Pokemon
    private lateinit var dataFromTheRemoteDataSource: Pokemon

    @Before
    fun setup() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        internetUtils = mockk()
        dataFromTheLocalDataSource = Pokemon(id = 1, name = PIKACHU)
        dataFromTheRemoteDataSource = Pokemon(id = 1, name = RATTATA)
        repository = PokeRepository(remoteDataSource, localDataSource, internetUtils)

        coEvery { localDataSource.getPokemonDetail(PIKACHU) } returns dataFromTheLocalDataSource
        coEvery { remoteDataSource.getPokemonDetail(RATTATA) } returns dataFromTheRemoteDataSource
        coEvery { localDataSource.getPokemonDetail(CHARIZARD) } returns null
        coEvery { localDataSource.updateItem(dataFromTheRemoteDataSource) } returns true

        getPokemonDetailUseCase = GetPokemonDetailUseCase(repository)
    }

    @Test
    fun `When getPokemonDetailUseCase() is called and the phone is offline, then return the data from the local data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns false
            val result = getPokemonDetailUseCase(PIKACHU)
            coVerify { localDataSource.getPokemonDetail(PIKACHU) }
            assert(result == dataFromTheLocalDataSource)
        }

    @Test
    fun `When getPokemonDetailUseCase() is called and the phone is online, then return the data from the remote data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns true
            val result = getPokemonDetailUseCase(RATTATA)
            coVerify { remoteDataSource.getPokemonDetail(RATTATA) }
            assert(result == dataFromTheRemoteDataSource)
        }

    @Test
    fun `When getPokemonDetailUseCase() is called, the phone is offline, and the pokemon was not found in the local data source, then a POKEMON_DETAIL_NOT_FOUND exception must be thrown`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns false
            var exception = PokedexException(UNKNOWN_ERROR)
            try {
                getPokemonDetailUseCase(CHARIZARD)
            } catch (e: PokedexException) {
                exception = e
            }
            coVerify { localDataSource.getPokemonDetail(CHARIZARD) }
            assert(exception.errorCode == POKEMON_DETAIL_NOT_FOUND)
        }

    companion object {
        private const val PIKACHU = "PIKACHU"
        private const val RATTATA = "RATTATA"
        private const val CHARIZARD = "CHARIZARD"
    }
}