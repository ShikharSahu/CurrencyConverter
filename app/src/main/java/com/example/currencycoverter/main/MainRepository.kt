package com.example.currencycoverter.main

import com.example.currencycoverter.data.models.CurrencyResponse
import com.example.currencycoverter.utils.Resource

interface MainRepository  {

    suspend fun getRates (base : String) : Resource< CurrencyResponse>
}