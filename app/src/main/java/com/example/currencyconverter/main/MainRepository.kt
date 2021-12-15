package com.example.currencyconverter.main

import com.example.currencyconverter.data.models.CurrencyResponse
import com.example.currencyconverter.utils.Resource
// we used an interface here so that it becomes easy to test this
// a testing class extends this interface in the testing part.
// Unit tests don't require the instance of the repository
// as we avoid making network calls in testing
interface MainRepository  {

    // some error may happen but we won't be able to detect them
    // if we directly just return Currency Response
    // So we just a generic class to wrap this class
    // that generic class can have 2 parts
    // 1. Success 2. Error
    // we create this in a package named Utils

    suspend fun getRates (base : String) : Resource<CurrencyResponse>
}