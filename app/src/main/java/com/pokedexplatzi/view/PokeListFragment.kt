package com.pokedexplatzi.view

import android.view.View.VISIBLE
import com.pokedexplatzi.data.model.enums.ErrorCode.POKEMON_LIST_NOT_FOUND
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.databinding.FragmentPokeListBinding
import com.pokedexplatzi.utils.BaseFragmentBinding
import com.pokedexplatzi.utils.ext.beginDelayedTransitionWithDelay
import com.pokedexplatzi.utils.ext.navigateSafelyWithDirections
import com.pokedexplatzi.utils.ext.observeWith
import com.pokedexplatzi.view.PokeListFragmentDirections.Companion.actionPokeListFragmentToPokeDetailFragment
import com.pokedexplatzi.view.adapter.PokesAdapter
import com.pokedexplatzi.view.viewmodel.PokeListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokeListFragment : BaseFragmentBinding<FragmentPokeListBinding>() {
    override fun bind() = FragmentPokeListBinding.inflate(layoutInflater)

    private val viewModel: PokeListViewModel by viewModel()

    override fun onViewCreated() {
        getPokes()
    }

    private fun getPokes() {
        viewModel.getPokes().observeWith(viewLifecycleOwner, { pokes ->
            if (pokes.isNullOrEmpty()) {
                showNotFoundUi()
            } else {
                binding.rcvPokes.adapter = PokesAdapter(pokes, ::onItemClicked)
                binding.rcvPokes.layoutManager?.onRestoreInstanceState(viewModel.rcvState)
            }
        }, { e ->
            if (e.errorCode == POKEMON_LIST_NOT_FOUND) {
                showNotFoundUi()
            }
        })
    }

    private fun showNotFoundUi() {
        binding.layoutFragmentPokeList.beginDelayedTransitionWithDelay()
        binding.tvNotFound.visibility = VISIBLE
        binding.ivNotFound.visibility = VISIBLE
    }

    private fun onItemClicked(pokemon: Pokemon) {
        val directions = actionPokeListFragmentToPokeDetailFragment(pokemon)
        navigateSafelyWithDirections(directions)
    }

    override fun onDestroyView() {
        viewModel.rcvState = binding.rcvPokes.layoutManager?.onSaveInstanceState()
        super.onDestroyView()
    }
}