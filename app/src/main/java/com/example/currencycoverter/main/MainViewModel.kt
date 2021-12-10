package com.example.currencycoverter.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencycoverter.data.models.Rates
import com.example.currencycoverter.utils.DispatcherProvider
import com.example.currencycoverter.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor (
    private val repository : MainRepository,
    private val dispatchers: DispatcherProvider
        ) : ViewModel() {

    sealed class CurrencyEvent(){
        class Success (val resultText : String) : CurrencyEvent()
        class Failure (val errorText : String) : CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion : StateFlow<CurrencyEvent> = _conversion

    fun convert (
        amountStr : String,
        fromCurrency : String,
        toCurrency : String
    ){

        Log.d(TAG, "convert: init values $amountStr $fromCurrency $toCurrency")
        val fromAmount = amountStr.toFloatOrNull()
        if (fromAmount == null){
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }

        viewModelScope.launch(dispatchers.io){
            _conversion.value = CurrencyEvent.Loading
            when(val ratesResponse = repository.getRates(fromCurrency)){
                is Resource.Error -> {
                    _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                    Log.d(TAG, "convert: error Resource error")
                }
                is Resource.Success -> {
                    Log.d(TAG, "convert: ${ratesResponse.data}")
                    Log.d(TAG, "convert: ${ratesResponse.data!!.success}")

                    val rates = ratesResponse.data.rates
                    Log.d(TAG, "convert: ${rates == null}")
                    val toRate = getRateForCurrency(toCurrency, rates)
                    val fromRate = getRateForCurrency(fromCurrency, rates)
                    if(toRate == null || fromRate == null){
                        Log.d(TAG, "convert: error here")
                        _conversion.value = CurrencyEvent.Failure("Unexpected Error")
                    }
                    else{
                        Log.d(TAG, "convert: do i reach here")
                        val convertedCurrency = round((fromAmount * toRate)/fromRate *100) / 100
                        _conversion.value = CurrencyEvent.Success (
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency")
                    }

                }
            }


        }
    }


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