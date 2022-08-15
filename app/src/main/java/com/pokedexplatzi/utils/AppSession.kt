package com.pokedexplatzi.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object AppSession {
    var theAppNeedsToUpdateTheListOfPokemon = true

    private val mGlobalProgressBar = MutableLiveData<Boolean>()
    val globalProgressBar = mGlobalProgressBar as LiveData<Boolean>
    fun showGlobalProgressBar(showLoading: Boolean) = mGlobalProgressBar.postValue(showLoading)
}
