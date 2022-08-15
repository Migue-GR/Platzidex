package com.pokedexplatzi.utils.ext

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

fun ConstraintLayout.beginDelayedTransitionWithDelay(duration: Long = 400) {
    val transition = AutoTransition()
    transition.duration = duration
    TransitionManager.beginDelayedTransition(this, transition)
}