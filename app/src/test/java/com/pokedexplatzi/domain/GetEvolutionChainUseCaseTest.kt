package com.pokedexplatzi.domain

import com.pokedexplatzi.data.datasource.local.LocalPokesDataSource
import com.pokedexplatzi.data.datasource.remote.RemotePokesDataSource
import com.pokedexplatzi.data.model.enums.ErrorCode.EVOLUTION_CHAIN_NOT_FOUND
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

class GetEvolutionChainUseCaseTest {
    private lateinit var getEvolutionChainUseCase: GetEvolutionChainUseCase

    private lateinit var repository: PokeRepository
    private lateinit var localDataSource: LocalPokesDataSource
    private lateinit var remoteDataSource: RemotePokesDataSource
    private lateinit var internetUtils: InternetUtils
    private lateinit var pichuEvolutionChain: List<Pokemon>
    private lateinit var rattataEvolutionChain: List<Pokemon>
    private lateinit var evolutionChainOfPokemonNames: List<String>

    @Before
    fun setup() {
        val rattata = Pokemon(id = 1, name = RATTATA)
        val raticate = Pokemon(id = 2, name = RATICATE)

        localDataSource = mockk()
        remoteDataSource = mockk()
        internetUtils = mockk()
        pichuEvolutionChain = listOf(
            Pokemon(id = 1, name = PICHU),
            Pokemon(id = 2, name = PIKACHU),
            Pokemon(id = 3, name = RAICHU)
        )
        rattataEvolutionChain = listOf(rattata, raticate)
        evolutionChainOfPokemonNames = listOf(RATTATA, RATICATE)
        repository = PokeRepository(remoteDataSource, localDataSource, internetUtils)

        coEvery { localDataSource.getEvolutionChain(EVOLUTION_CHAIN_OF_PICHU) } returns pichuEvolutionChain
        coEvery { remoteDataSource.getEvolutionChain(EVOLUTION_CHAIN_OF_RATTATA) } returns evolutionChainOfPokemonNames

        coEvery { remoteDataSource.getPokemonDetail(RATTATA) } returns rattata
        coEvery { remoteDataSource.getPokemonDetail(RATICATE) } returns raticate

        coEvery { localDataSource.savePokemon(rattata) } returns true
        coEvery { localDataSource.savePokemon(raticate) } returns true

        getEvolutionChainUseCase = GetEvolutionChainUseCase(repository)
    }

    @Test
    fun `When GetEvolutionChainUseCase() is called and the phone is offline, then return the data from the local data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns false
            val result = getEvolutionChainUseCase(EVOLUTION_CHAIN_OF_PICHU)
            coVerify { localDataSource.getEvolutionChain(EVOLUTION_CHAIN_OF_PICHU) }
            assert(result == pichuEvolutionChain)
        }

    @Test
    fun `When GetEvolutionChainUseCase() is called and the phone is online, then return the data from the remote data source`() =
        runBlocking {
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns true
            val result = getEvolutionChainUseCase(EVOLUTION_CHAIN_OF_RATTATA)
            coVerify { remoteDataSource.getEvolutionChain(EVOLUTION_CHAIN_OF_RATTATA) }
            coVerify { remoteDataSource.getPokemonDetail(RATTATA) }
            coVerify { remoteDataSource.getPokemonDetail(RATICATE) }
            assert(result == rattataEvolutionChain)
        }

    @Test
    fun `When GetEvolutionChainUseCase() is called, the phone is offline, and the evolution chain was not found in the local data source, then a EVOLUTION_CHAIN_NOT_FOUND exception must be thrown`() =
        runBlocking {
            coEvery { localDataSource.getEvolutionChain(EVOLUTION_CHAIN_OF_CHARMANDER) } returns emptyList()
            coEvery { internetUtils.isThePhoneConnectedToTheInternet() } returns false
            var exception = PokedexException(UNKNOWN_ERROR)
            try {
                getEvolutionChainUseCase(EVOLUTION_CHAIN_OF_CHARMANDER)
            } catch (e: PokedexException) {
                exception = e
            }
            coVerify { localDataSource.getEvolutionChain(EVOLUTION_CHAIN_OF_CHARMANDER) }
            assert(exception.errorCode == EVOLUTION_CHAIN_NOT_FOUND)
        }

    companion object {
        private const val EVOLUTION_CHAIN_OF_PICHU = "EVOLUTION_CHAIN_OF_PICHU"
        private const val EVOLUTION_CHAIN_OF_RATTATA = "EVOLUTION_CHAIN_OF_RATTATA"
        private const val EVOLUTION_CHAIN_OF_CHARMANDER = "EVOLUTION_CHAIN_OF_CHARMANDER"
        private const val PICHU = "PICHU"
        private const val PIKACHU = "PIKACHU"
        private const val RAICHU = "RAICHU"
        private const val RATTATA = "RATTATA"
        private const val RATICATE = "RATICATE"
    }
}