package com.pokedexplatzi.data.model.remote

import com.google.gson.annotations.SerializedName
import com.pokedexplatzi.data.model.local.Pokemon
import java.util.*

data class RemotePokemon(
    val id: Int?,
    val name: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("base_happiness")
    val baseHappiness: Int?,
    @SerializedName("capture_rate")
    val captureRate: Int?,
    @SerializedName("egg_groups")
    val eggGroups: List<RemoteEggGroup?>?,
    @SerializedName("evolution_chain")
    val evolutionChainUrl: EvolutionChainUrl?
)

fun List<RemotePokemon>.toLocalObjects(): List<Pokemon> {
    val pokes = mutableListOf<Pokemon>()
    for (item in this) {
        val id = item.id ?: item.url?.removeSurrounding(ID_PREFIX, ID_SUFFIX)?.toIntOrNull() ?: 0
        pokes.add(
            Pokemon(
                id = id,
                name = item.name ?: "MissingNo",
                spriteUrl = SPRITE_URL + id + SPRITE_FORMAT
            )
        )
    }
    return pokes
}

fun RemotePokemon.toLocalObject(): Pokemon {
    var eggGroupsStr = ""
    val id = id ?: url?.removeSurrounding(ID_PREFIX, ID_SUFFIX)?.toIntOrNull() ?: 0

    eggGroups?.forEachIndexed { index, egg ->
        eggGroupsStr += if (index == 0) {
            egg?.name
        } else {
            ", ${egg?.name}"
        }
    }
    return Pokemon(
        id = id,
        name = name ?: "MissingNo",
        spriteUrl = SPRITE_URL + id + SPRITE_FORMAT,
        baseHappiness = baseHappiness?.toString() ?: "----",
        captureRate = captureRate?.toString() ?: "----",
        eggGroups = eggGroupsStr.ifEmpty { "----" },
        evolutionChainUrl = evolutionChainUrl?.url ?: ""
    )
}

private const val ID_PREFIX = "https://pokeapi.co/api/v2/pokemon/"
private const val ID_SUFFIX = "/"
private const val SPRITE_URL = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
private const val SPRITE_FORMAT = ".png"