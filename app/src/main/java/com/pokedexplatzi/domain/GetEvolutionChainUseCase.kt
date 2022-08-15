package com.pokedexplatzi.domain

import com.pokedexplatzi.data.repository.PokeRepository
import com.pokedexplatzi.utils.ext.useCaseExecution
import kotlinx.coroutines.Dispatchers

class GetEvolutionChainUseCase(private val repository: PokeRepository) {
    suspend operator fun invoke(evolutionChainUrl: String) = useCaseExecution(Dispatchers.IO) {
        repository.getEvolutionChain(evolutionChainUrl)
    }
}