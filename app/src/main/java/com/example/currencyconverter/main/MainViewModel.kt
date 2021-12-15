package com.example.currencyconverter.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.models.Rates
import com.example.currencyconverter.utils.DispatcherProvider
import com.example.currencyconverter.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

// we need this annotation when working with viewModels in daggerHilt
// We inject MainRepository and Dispatcher providers via hilt in the constructor
// When we test coroutines, we require special coroutine test dispatchers

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository : MainRepository,
    private val dispatchers: DispatcherProvider
        ) : ViewModel() {

    // sealed class to work with stateFlow
    // StateFlow is better than LiveData in many aspects so we use that
    sealed class CurrencyEvent(){
        // when concurrency is successfully converted
        class Success (val resultText : String) : CurrencyEvent()
        // when concurrency is not successfully converted
        class Failure (val errorText : String) : CurrencyEvent()
        // when concurrency is being converted
        object Loading : CurrencyEvent()
        // when nothing has happened
        object Empty : CurrencyEvent()

        // the states that require parameters need to be classes,
        // but the states that are static can be kotlin singletons
    }

    // Whenever we are using binding, LiveData, StateFlow etc, use it in this manner
    // it permits the way it can be changed in places we don't want it to change
    // we only expose the part of it that is necessary for it to work

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion : StateFlow<CurrencyEvent> = _conversion

    fun convert (
        amountStr : String,
        fromCurrency : String,
        toCurrency : String
    ){

        Log.d(TAG, "convert: init values $amountStr $fromCurrency $toCurrency")
        val fromAmount = amountStr.toFloatOrNull()
        //   set state to failure --> that the number can't be converted to float
        if (fromAmount == null){
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        // if it was valid, we launch a coroutine in io dispatcher
        viewModelScope.launch(dispatchers.io){
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)){
                is Resource.Error -> {
                    _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                    Log.d(TAG, "convert: error Resource error")
                }
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val toRate = getRateForCurrency(toCurrency, rates)
                    val fromRate = getRateForCurrency(fromCurrency, rates)

                    if(toRate == null || fromRate == null){
                        Log.d(TAG, "convert: input format conversion error")
                        _conversion.value = CurrencyEvent.Failure("Unexpected Error")
                    }
                    else{
                        // since the api only returns in base EUR, we apply some simple maths to convert
                        // the answer to any base.
                        // at last, we set the state of the stateflow to Success and return the message of conversion
                        val convertedCurrency = round((fromAmount * toRate)/fromRate *100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }

                }
            }


        }
    }


    // Maps the currency name to its exchange rate from the rates list
    private fun getRateForCurrency(currency: String, rates: Rates) = when (currency) {
        "CAD" -> rates.cAD
        "HKD" -> rates.hKD
        "ISK" -> rates.iSK
        "EUR" -> rates.eUR
        "PHP" -> rates.pHP
        "DKK" -> rates.dKK
        "HUF" -> rates.hUF
        "CZK" -> rates.cZK
        "AUD" -> rates.aUD
        "RON" -> rates.rON
        "SEK" -> rates.sEK
        "IDR" -> rates.iDR
        "INR" -> rates.iNR
        "BRL" -> rates.bRL
        "RUB" -> rates.rUB
        "HRK" -> rates.hRK
        "JPY" -> rates.jPY
        "THB" -> rates.tHB
        "CHF" -> rates.cHF
        "SGD" -> rates.sGD
        "PLN" -> rates.pLN
        "BGN" -> rates.bGN
        "CNY" -> rates.cNY
        "NOK" -> rates.nOK
        "NZD" -> rates.nZD
        "ZAR" -> rates.zAR
        "USD" -> rates.uSD
        "MXN" -> rates.mXN
        "ILS" -> rates.iLS
        "GBP" -> rates.gBP
        "KRW" -> rates.kRW
        "MYR" -> rates.mYR
        else -> null
    }

}