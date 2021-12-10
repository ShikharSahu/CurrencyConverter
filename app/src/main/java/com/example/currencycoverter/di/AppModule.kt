package com.example.currencycoverter.di

import com.example.currencycoverter.data.models.CurrencyApi
import com.example.currencycoverter.main.DefaultMainRepository
import com.example.currencycoverter.main.MainRepository
import com.example.currencycoverter.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCurrencyApi() : CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    fun providesMainRepository(api : CurrencyApi) : MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun providesDispatcherProvider() : DispatcherProvider = object :
        DispatcherProvider{
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