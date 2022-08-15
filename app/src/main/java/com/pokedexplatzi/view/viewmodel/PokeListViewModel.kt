package com.pokedexplatzi.view.viewmodel

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.domain.GetPokesUseCase
import com.pokedexplatzi.utils.AppSession.theAppNeedsToUpdateTheListOfPokemon
import com.pokedexplatzi.utils.UseCaseResult
import com.pokedexplatzi.utils.ext.resultLiveData
import com.pokedexplatzi.utils.ext.resultLiveDataWithoutLoading

class PokeListViewModel(private val getPokesUseCase: GetPokesUseCase) : ViewModel() {
    var rcvState: Parcelable? = null

    fun getPokes(): LiveData<UseCaseResult<List<Pokemon>>> {
        return if (theAppNeedsToUpdateTheListOfPokemon) {
            theAppNeedsToUpdateTheListOfPokemon = false
            getRemotePokes()
        } else {
            getCachedPokes()
        }
    }

    private fun getRemotePokes() = resultLiveData {
        getPokesUseCase(isFromCache = false)
    }

    private fun getCachedPokes() = resultLiveDataWithoutLoading {
        getPokesUseCase(isFromCache = true)
    }
}