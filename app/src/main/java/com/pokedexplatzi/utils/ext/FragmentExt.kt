package com.pokedexplatzi.utils.ext

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import timber.log.Timber

infix fun Fragment.navigateSafelyTo(@IdRes destination: Int) = try {
    findNavController().navigate(destination)
} catch (e: IllegalArgumentException) {
    Timber.e(e, "Navigation attempted multiple times! Skipping...")
}

fun Fragment.navigateSafelyWithDirections(
    directions: NavDirections,
    extras: Navigator.Extras? = null
) = try {
    if (extras != null) {
        findNavController().navigate(directions, extras)
    } else {
        findNavController().navigate(directions)
    }
} catch (e: IllegalArgumentException) {
    Timber.e(e, "Navigation attempted multiple times! Skipping...")
}

infix fun Fragment.showToast(message: String) = try {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
} catch (e: IllegalArgumentException) {
    Timber.e(e, "Error while trying to show toast")
}