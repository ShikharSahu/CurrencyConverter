package com.example.currencyconverter.data

import com.example.currencyconverter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET

// interface for the retrofit API calling

interface CurrencyApi {
    // Headers did not work here. Further testing required in logic!!
    // @Headers("access_key: aa132471d09b372d756551bf34332078")
    // the url from which we are getting the data from
    // preferably the access key is not passed like below but it works here
    @GET("/latest?access_key=aa132471d09b372d756551bf34332078")
    suspend fun getRates(
        // The annotation below is used to pass the queries in the request
        // here we are passing a string as base currency name like 'eur'

        //@Query("base") base: String
    // We wrap currency response wrapped in Response (class in retrofit)
    // is returned
    ) : Response<CurrencyResponse>
}