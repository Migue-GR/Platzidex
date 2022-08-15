package com.pokedexplatzi.utils.ext

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

infix fun AppCompatActivity.showToast(message: String) = try {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
} catch (e: IllegalArgumentException) {
    Timber.e(e, "Error while trying to show toast")
}