package com.pokedexplatzi.utils.ext

import android.widget.ImageView
import androidx.annotation.ColorRes
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.pokedexplatzi.R
import com.pokedexplatzi.utils.GlideApp

private const val CROSS_FADE_DURATION = 400

fun ImageView.setCircleImageWithGlide(
    @ColorRes
    placeHolder: Int = R.color.color_bg_primary,
    uri: String
) {
    GlideApp
        .with(this.context)
        .load(uri)
        .circleCrop()
        .placeholder(placeHolder)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}

fun ImageView.setImageWithGlide(
    uri: String,
    @ColorRes
    placeHolder: Int = R.color.color_bg_primary,
) {
    GlideApp
        .with(this.context)
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_DURATION))
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .into(this)
}