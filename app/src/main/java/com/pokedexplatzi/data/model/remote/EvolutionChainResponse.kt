package com.pokedexplatzi.data.model.remote

data class EvolutionChainResponse(val chain: RemoteEvolution)

fun EvolutionChainResponse.toLocalObjects(): List<String> {
    val evolutionsToReturn = mutableListOf<String>()

    evolutionsToReturn.add(chain.species?.name ?: "")

    fun addEvolutionToTheList(evolutions: List<RemoteEvolution>) {
        for (evolution in evolutions) {
            evolutionsToReturn.add(evolution.species?.name ?: "MissingNo")
            if (!evolution.evolutions.isNullOrEmpty()) {
                addEvolutionToTheList(evolution.evolutions.filterNotNull())
            }
        }
    }

    addEvolutionToTheList(chain.evolutions?.filterNotNull() ?: listOf())
    return evolutionsToReturn
}