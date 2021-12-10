package com.example.currencycoverter.data.models


import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    val rates: Rates,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int
){
    override fun toString(): String {
        return "$base $date $rates $success $timestamp"

    }
}