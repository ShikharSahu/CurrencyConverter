package com.example.currencyconverter.main

import android.content.ContentValues.TAG
import android.util.Log
import com.example.currencyconverter.data.CurrencyApi
import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.utils.Resource
import javax.inject.Inject

// The actual repository for this project
// Used to make actual database requests

// Inject it in into currency
// Since we require the retrofit Currency API instance,
// we inject it into the constructor
// DaggerHilt automatically injects function that provides (returns) Currency API
class DefaultMainRepository @Inject constructor(
    private val api : CurrencyApi
) : MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            // gets the response from the API of the type Response (inbuilt in RetroFit)
            val response = api.getRates()

            // we get the CurrencyResponse (body) part of the response.
            val result = response.body()

            Log.d(TAG, "getRates: ${response.isSuccessful}")
            Log.d(TAG, "getRates: ${response.message()}")
            Log.d(TAG, "getRates: $result")

            // if the response was successful and we got a currencyResponse was not null
            // Then that means that the request was successful and we return the CurrencyResponse
            // Object wrapped as an Resource object
            // Resource is our user defined generic class

            if(response.isSuccessful && result != null){
                Resource.Success(result)
            }
            else{
                Resource.Error(response.message() ?: "an unwanted error occurred")
            }
        }
        catch (e : Exception){
            Resource.Error(e.message ?: "an error occurred")
        }
    }

}