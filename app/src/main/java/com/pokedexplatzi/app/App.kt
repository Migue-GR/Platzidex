package com.pokedexplatzi.app

import android.app.Application
import com.pokedexplatzi.BuildConfig
import com.pokedexplatzi.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(
                utilsModule,
                netModule,
                dataBaseDataSourceModule,
                remoteDataSourceModule,
                localDataSourceModule,
                repositoriesModule,
                useCasesModule,
                viewModelsModule,
            )
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return super.createStackElementTag(element) + ':' + element.lineNumber + '#' + element.methodName + "()"
                }
            })
        }
    }
}