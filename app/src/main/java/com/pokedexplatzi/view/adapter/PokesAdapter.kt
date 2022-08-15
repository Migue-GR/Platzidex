package com.pokedexplatzi.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.databinding.ItemPokeBinding
import com.pokedexplatzi.utils.ext.setImageWithGlide
import com.pokedexplatzi.utils.setOnSingleClickListener

class PokesAdapter(
    private val items: List<Pokemon>,
    private val onItemClicked: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokesViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PokesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokesViewHolder {
        return PokesViewHolder(
            ItemPokeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )
    }
}

class PokesViewHolder(
    private val binding: ItemPokeBinding,
    private val onItemClicked: (Pokemon) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Pokemon) {
        binding.tvName.text = item.name
        binding.sprite.setImageWithGlide(item.spriteUrl)
        binding.cardItem.setOnSingleClickListener { onItemClicked(item) }
    }
}