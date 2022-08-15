package com.pokedexplatzi.view

import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pokedexplatzi.R
import com.pokedexplatzi.data.model.enums.ErrorCode
import com.pokedexplatzi.data.model.enums.ErrorCode.*
import com.pokedexplatzi.data.model.local.Pokemon
import com.pokedexplatzi.databinding.FragmentPokeDetailBinding
import com.pokedexplatzi.utils.BaseFragmentBinding
import com.pokedexplatzi.utils.ext.*
import com.pokedexplatzi.utils.setOnSingleClickListener
import com.pokedexplatzi.view.PokeDetailFragmentDirections.Companion.actionPokeDetailFragmentToEvolutionChainDetailFragment
import com.pokedexplatzi.view.adapter.EvolutionChainAdapter
import com.pokedexplatzi.view.viewmodel.PokeDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokeDetailFragment : BaseFragmentBinding<FragmentPokeDetailBinding>() {
    override fun bind() = FragmentPokeDetailBinding.inflate(layoutInflater)

    private val viewModel: PokeDetailViewModel by viewModel()
    private val args: PokeDetailFragmentArgs by navArgs()

    override fun onViewCreated() {
        binding.btnGoBack.setOnSingleClickListener { findNavController().navigateUp() }
        binding.title.text = args.pokemon.name
        binding.name.text = args.pokemon.name

        binding.sprite.setImageWithGlide(args.pokemon.spriteUrl)
        lifecycleScope.launchWithDelay(700) {
            getPokemonDetail()
        }
    }

    private fun getPokemonDetail() {
        viewModel.getPokemonDetail(args.pokemon.name).observeWith(viewLifecycleOwner, { pokemon ->
            if (pokemon != null) {
                getEvolutionChain(pokemon.evolutionChainUrl)
                binding.baseHappiness.text = pokemon.baseHappiness
                binding.captureRate.text = pokemon.captureRate
                binding.eggGroups.text = pokemon.eggGroups
                binding.layoutFragmentPokeDetail.beginDelayedTransitionWithDelay(200)
                binding.baseHappiness.visibility = VISIBLE
                binding.captureRate.visibility = VISIBLE
                binding.eggGroups.visibility = VISIBLE
            }
        }, { e ->
            if (e.errorCode == POKEMON_DETAIL_NOT_FOUND || e.errorCode == HTTP_NOT_FOUND) {
                showToast(getString(R.string.error_getting_a_pokemon, args.pokemon.name))
                showPokemonDetailNotFoundUi()
                showEvolutionChainNotFoundUi()
            }
        })
    }

    private fun showPokemonDetailNotFoundUi() {
        binding.baseHappiness.text = "----"
        binding.captureRate.text = "----"
        binding.eggGroups.text = "----"
        binding.layoutFragmentPokeDetail.beginDelayedTransitionWithDelay(200)
        binding.baseHappiness.visibility = VISIBLE
        binding.captureRate.visibility = VISIBLE
        binding.eggGroups.visibility = VISIBLE
    }

    private fun getEvolutionChain(evolutionChainUrl: String) {
        viewModel.getEvolutionChain(evolutionChainUrl).observeWith(viewLifecycleOwner, { pokes ->
            if (pokes.isNullOrEmpty()) {
                showEvolutionChainNotFoundUi()
            } else {
                binding.layoutFragmentPokeDetail.beginDelayedTransitionWithDelay(350)
                binding.rcvEvolutionChain.adapter = EvolutionChainAdapter(pokes, ::onItemClicked)
                binding.rcvEvolutionChain.setHasFixedSize(true)
            }
        }, { e ->
            if (e.errorCode == HTTP_NOT_FOUND || e.errorCode == EVOLUTION_CHAIN_NOT_FOUND) {
                showEvolutionChainNotFoundUi()
            }
        })
    }

    private fun showEvolutionChainNotFoundUi() {
        binding.layoutFragmentPokeDetail.beginDelayedTransitionWithDelay()
        binding.tvNotFound.visibility = VISIBLE
        binding.ivNotFound.visibility = VISIBLE
    }

    private fun onItemClicked(pokemon: Pokemon) {
        if (pokemon.id != args.pokemon.id) {
            val directions = actionPokeDetailFragmentToEvolutionChainDetailFragment(pokemon)
            navigateSafelyWithDirections(directions)
        } else {
            showToast(getString(R.string.select_another_pokemon))
        }
    }
}