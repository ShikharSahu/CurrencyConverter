package com.example.currencyconverter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


// This is basically the application class.
// It is required to create this when working with dagger hilt
// we also need to annotate it with the below
// In AndroidManifest file remember to add this class
// as "android:name=".CurrencyApplication"

@HiltAndroidApp
class CurrencyApplication : Application() {}