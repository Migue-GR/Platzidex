package com.pokedexplatzi.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI

class InternetUtils(private val appContext: Context) {
    fun isThePhoneConnectedToTheInternet(): Boolean {
        val manager = appContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetwork ?: return false
        val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}