package com.pokedexplatzi.data.model.remote

import com.google.gson.annotations.SerializedName

data class RemoteEvolution(
    @SerializedName("evolves_to")
    val evolutions: List<RemoteEvolution?>?,
    val species: RemoteSpecies?
)