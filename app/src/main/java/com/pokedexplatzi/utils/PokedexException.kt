package com.pokedexplatzi.utils

import com.pokedexplatzi.data.model.enums.ErrorCode

class PokedexException(val errorCode: ErrorCode = ErrorCode.UNKNOWN_ERROR) : Exception() {
    override fun getLocalizedMessage(): String = errorMessageFactory(errorCode)

    /**
     * @param errorCode Code describing the exception.
     * @return an error message by a given [ErrorCode].
     */
    private fun errorMessageFactory(errorCode: ErrorCode): String {
        return when (errorCode) {
            ErrorCode.UNKNOWN_ERROR -> "Unknown error"
            ErrorCode.HTTP_NOT_FOUND -> "404 from the backend"
            ErrorCode.HTTP_SERVER_ERROR -> "500 from the backend"
            ErrorCode.POKEMON_LIST_NOT_FOUND -> "Pokemon list not found, verify the internet connection"
            ErrorCode.POKEMON_DETAIL_NOT_FOUND -> "Pokemon detail not found, verify the internet connection"
            ErrorCode.EVOLUTION_CHAIN_NOT_FOUND -> "Evolution chain not found, verify the internet connection"
        }
    }
}