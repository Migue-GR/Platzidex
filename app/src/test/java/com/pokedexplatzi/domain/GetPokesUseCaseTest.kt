package com.pokedexplatzi.domain

import com.pokedexplatzi.data.datasource.local.LocalPokesDataSource
import com.pokedexplatzi.data.datasource.remote.RemotePokesDataSource
import com.pokedexplatzi.data.model.enums.ErrorCode.POKEMON_LIST_NOT_FOUND
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

class GetPokesUseCaseTest {
    private lateinit var getPokesUseCase: GetPokesUseCase

    private lateinit var repository: PokeRepository
    private lateinit var localDataSource: LocalPokesDataSource
    private lateinit var remoteDataSource: RemotePokesDataSource
    private lateinit var internetUtils: InternetUtils
    private lateinit var dataFromTheLocalDataSource: List<Pokemon>
    private lateinit var dataFromTheRemoteDataSource: List<Pokemon>

    @Before
    fun setup() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        internetUtils = mockk()
        dataFromTheLocalDataSource = listOf(Pokemon(id = 1, name = "Pikachu"))
        dataFromTheRemoteDataSource = listOf(Pokemon(id = 1, name = "Rattata"))
        repository = PokeRepository(remoteDataSource, localDataSource, internetUtils)

        coEvery { localDataSource.getPokes() } returns dataFromTheLocalDataSource
        coEvery { remoteDataSource.getPokes() } returns dataFromTheRemoteDataSource
        coEvery { localDataSource.savePokes(dataFromTheRemoteDataSource) } returns true

        getPokesUseCase = GetPokesUseCase(repository)
    }

    @Test
    fun `When getPokesUseCase() is called and the phone is offline, then return the data from the local data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns false
            val result = getPokesUseCase(isFromCache = false)
            coVerify { localDataSource.getPokes() }
            assert(result == dataFromTheLocalDataSource)
        }

    @Test
    fun `When getPokesUseCase() is called and the phone is online, then return the data from the remote data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns true
            val result = getPokesUseCase(isFromCache = false)
            coVerify { remoteDataSource.getPokes() }
            assert(result == dataFromTheRemoteDataSource)
        }

    @Test
    fun `When getPokesUseCase() is called with the isFromCache flag as true, then return the data from the local data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns true
            val result = getPokesUseCase(isFromCache = true)
            coVerify { localDataSource.getPokes() }
            assert(result == dataFromTheLocalDataSource)
        }

    @Test
    fun `When getPokesUseCase() is called, the phone is offline and there is no data in the local data source, then a POKEMON_LIST_NOT_FOUND exception must be thrown`() =
        runBlocking {
            coEvery { localDataSource.getPokes() } returns emptyList()
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns false
            var exception = PokedexException(UNKNOWN_ERROR)
            try {
                getPokesUseCase(isFromCache = false)
            } catch (e: PokedexException) {
                exception = e
            }
            coVerify { localDataSource.getPokes() }
            assert(exception.errorCode == POKEMON_LIST_NOT_FOUND)
        }
}