package com.pokedexplatzi.domain

import com.pokedexplatzi.data.repository.PokeRepository
import com.pokedexplatzi.utils.ext.useCaseExecution
import kotlinx.coroutines.Dispatchers

class GetPokesUseCase(private val repository: PokeRepository) {
    suspend operator fun invoke(isFromCache: Boolean) = useCaseExecution(Dispatchers.IO) {
        repository.getPokes(isFromCache)
    }
}