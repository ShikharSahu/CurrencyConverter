package com.example.currencycoverter.main

import android.content.ContentValues.TAG
import android.util.Log
import com.example.currencycoverter.data.models.CurrencyApi
import com.example.currencycoverter.data.models.CurrencyResponse
import com.example.currencycoverter.utils.Resource
import javax.inject.Inject

// Inject it in into currency
class DefaultMainRepository @Inject constructor(
    private val api : CurrencyApi
) : MainRepository {
    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates()
            val result = response.body()
            Log.d(TAG, "getRates: ${response.isSuccessful}")
            Log.d(TAG, "getRates: ${response.message()}")
            Log.d(TAG, "getRates: $result")


            if(response.isSuccessful && result != null && result.base!=null){
                Resource.Success(result)
            }
            else{
                Resource.Error(response.message() + " unwanted error occurred")
            }
        }
        catch (e : Exception){
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}