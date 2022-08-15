package com.pokedexplatzi.view.viewmodel

import androidx.lifecycle.ViewModel
import com.pokedexplatzi.domain.GetEvolutionChainUseCase
import com.pokedexplatzi.domain.GetPokemonDetailUseCase
import com.pokedexplatzi.utils.ext.resultLiveData
import com.pokedexplatzi.utils.ext.resultLiveDataWithoutLoading

class PokeDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val getEvolutionChainUseCase: GetEvolutionChainUseCase
) : ViewModel() {
    fun getPokemonDetail(name: String) = resultLiveDataWithoutLoading {
        getPokemonDetailUseCase(name)
    }

    fun getEvolutionChain(evolutionChainUrl: String) = resultLiveData {
        getEvolutionChainUseCase(evolutionChainUrl)
    }
}