package com.example.currencyconverter.utils

// we use a sealed class so that only the classes
// inside of the Resource class are able to extend it

sealed class Resource <T> (val data : T?, val message : String?){
    // T?, String? mean that the va of that value can be null

    // On Success we return the data of the type T,
    // (we use T as CurrencyResponse in our project)
    class Success<T> (data : T) : Resource<T>(data, null)

    // On Failure/  Error, we just require to return the error message
    class Error<T> (message: String) : Resource<T>(null, message)

}