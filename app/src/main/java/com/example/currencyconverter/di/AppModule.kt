package com.example.currencyconverter.di

import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.main.DefaultMainRepository
import com.example.currencyconverter.main.MainRepository
import com.example.currencyconverter.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Generally declared in a different file.
// Since only one const we just declare it here.
private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

// This is the standard procedure for dependency interjection
// we create a new package and create a singleton class like this

// Below annotation indicates that it is a module
@Module
// Below indicates that it lives as long as our application
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    // provides the instance of the currency API,
    // hence we also annotate it with the same
    // Below is standard way to create a Retrofit Instance
    fun provideCurrencyApi() : CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    // provides the main repository and Dispatcher provider to the mainViewModel

    @Singleton
    @Provides
    fun providesMainRepository(api : CurrencyApi) : MainRepository = DefaultMainRepository(api)

    // The things below can be changed for when testing
    @Singleton
    @Provides
    fun providesDispatcherProvider() : DispatcherProvider = object :
        DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main

        override val default: CoroutineDispatcher
            get() = Dispatchers.Default

        override val io: CoroutineDispatcher
            get() = Dispatchers.IO

        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
        }

}