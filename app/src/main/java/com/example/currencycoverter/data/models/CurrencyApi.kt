package com.example.currencycoverter.data.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyApi {


//    @Headers("access_key: aa132471d09b372d756551bf34332078")
    @GET("/latest?access_key=aa132471d09b372d756551bf34332078")
    suspend fun getRates(
        //@Query("base") base: String
    ) : Response<CurrencyResponse>
}