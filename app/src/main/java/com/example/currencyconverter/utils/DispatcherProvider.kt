package com.example.currencyconverter.utils

import kotlinx.coroutines.CoroutineDispatcher

// Dispatcher provider interface
// used in coroutine testing

interface DispatcherProvider {
    val main : CoroutineDispatcher
    val io : CoroutineDispatcher
    val default : CoroutineDispatcher
    val unconfined : CoroutineDispatcher
}