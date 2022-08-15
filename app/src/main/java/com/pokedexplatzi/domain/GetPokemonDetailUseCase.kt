package com.pokedexplatzi.domain

import com.pokedexplatzi.data.repository.PokeRepository
import com.pokedexplatzi.utils.ext.useCaseExecution
import kotlinx.coroutines.Dispatchers

class GetPokemonDetailUseCase(private val repository: PokeRepository) {
    suspend operator fun invoke(name: String) = useCaseExecution(Dispatchers.IO) {
        repository.getPokemonDetail(name)
    }
}