package com.pokedexplatzi.utils

sealed class UseCaseResult<out T : Any> {
    object Loading : UseCaseResult<Nothing>()
    data class Success<out T : Any>(val payload: T?) : UseCaseResult<T>()
    data class Error(val exception: PokedexException) : UseCaseResult<Nothing>()
}