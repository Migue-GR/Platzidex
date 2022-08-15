package com.pokedexplatzi.utils.ext

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay

fun LifecycleCoroutineScope.launchWithDelay(
    delay: Long = 400,
    block: suspend () -> Unit
) = launchWhenStarted {
    delay(delay)
    block()
}