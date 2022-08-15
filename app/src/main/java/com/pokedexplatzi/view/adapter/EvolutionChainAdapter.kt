package com.pokedexplatzi.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.databinding.ItemEvolutionChainBinding
import com.pokedexplatzi.utils.ext.setImageWithGlide
import com.pokedexplatzi.utils.setOnSingleClickListener

class EvolutionChainAdapter(
    private val items: List<Pokemon>,
    private val onItemClicked: (Pokemon) -> Unit
) : RecyclerView.Adapter<EvolutionChainViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EvolutionChainViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvolutionChainViewHolder {
        return EvolutionChainViewHolder(
            ItemEvolutionChainBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemClicked
        )
    }
}

class EvolutionChainViewHolder(
    private val binding: ItemEvolutionChainBinding,
    private val onItemClicked: (Pokemon) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Pokemon) {
        binding.tvName.text = item.name
        binding.sprite.setImageWithGlide(item.spriteUrl)
        binding.cardItem.setOnSingleClickListener { onItemClicked(item) }
    }
}