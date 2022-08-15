package com.pokedexplatzi.app.di

import android.app.Application
import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pokedexplatzi.BuildConfig
import com.pokedexplatzi.data.datasource.local.db.AppDataBase
import com.pokedexplatzi.data.datasource.local.db.PokeDao
import com.pokedexplatzi.data.datasource.local.LocalPokesDataSource
import com.pokedexplatzi.data.datasource.remote.PokeService
import com.pokedexplatzi.data.datasource.remote.RemotePokesDataSource
import com.pokedexplatzi.data.repository.PokeRepository
import com.pokedexplatzi.domain.GetEvolutionChainUseCase
import com.pokedexplatzi.domain.GetPokemonDetailUseCase
import com.pokedexplatzi.domain.GetPokesUseCase
import com.pokedexplatzi.utils.InternetUtils
import com.pokedexplatzi.view.viewmodel.PokeDetailViewModel
import com.pokedexplatzi.view.viewmodel.PokeListViewModel
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelsModule = module {
    viewModel { PokeListViewModel(get()) }
    viewModel { PokeDetailViewModel(get(), get()) }
}

val useCasesModule = module {
    factory { GetPokesUseCase(get()) }
    factory { GetPokemonDetailUseCase(get()) }
    factory { GetEvolutionChainUseCase(get()) }
}

val repositoriesModule = module {
    factory { PokeRepository(get(), get(), get()) }
}

val remoteDataSourceModule = module {
    fun providePokeService(retrofit: Retrofit) = retrofit.create(PokeService::class.java)
    single { providePokeService(get()) }
    factory { RemotePokesDataSource(get()) }
}

val localDataSourceModule = module {
    single { LocalPokesDataSource(get()) }
}

val dataBaseDataSourceModule = module {
    fun provideAppDatabase(context: Context): AppDataBase {
        return AppDataBase.getInstance(context)
    }

    fun providePokeDao(db: AppDataBase): PokeDao {
        return db.pokeDao
    }

    single { provideAppDatabase(androidApplication()) }
    single { providePokeDao(get()) }
}

val netModule: Module = module {
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    fun provideHttpClient(
        cache: Cache
    ): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .cache(cache)

        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    single { provideCache(androidApplication()) }
    single { provideHttpClient(get()) }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }
}

val utilsModule = module {
    single { InternetUtils(androidApplication()) }
}