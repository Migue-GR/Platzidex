package com.pokedexplatzi.data.model.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "pokes")
@Parcelize
data class Pokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val spriteUrl: String = "",
    val baseHappiness: String = "----",
    val captureRate: String = "----",
    val eggGroups: String = "----",
    val evolutionChainUrl: String = "",
) : Parcelable