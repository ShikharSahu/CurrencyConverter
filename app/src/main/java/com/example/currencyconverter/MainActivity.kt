package com.example.currencyconverter

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.currencycoverter.R
import com.example.currencycoverter.databinding.ActivityMainBinding
import com.example.currencyconverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

// For every activity using Hilt,
// we require this at the top

// Reminder : to use different colors in different themes, create 2 different color files
// 1 normal and other in a folder name similar to night
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // get the view Model by the factory
    private val viewModel : MainViewModel by viewModels ()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCovert.setOnClickListener{

            viewModel.convert(
                binding.etAmountIp.text.toString(),
                binding.spFromCurrency.selectedItem.toString(),
                binding.spToCurrency.selectedItem.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is MainViewModel.CurrencyEvent.Success ->{
                        binding.progressCircular.isVisible = false
                        binding.tvResult.isVisible = true
                        binding.tvResult.setTextColor(baseContext.getColor(R.color.textColor))
                        binding.tvResult.text = event.resultText
                    }
                    is MainViewModel.CurrencyEvent.Failure ->{
                        binding.progressCircular.isVisible = false
                        binding.tvResult.isVisible = true
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorText
                    }
                    is MainViewModel.CurrencyEvent.Loading ->{
                        binding.progressCircular.isVisible = true
                        binding.tvResult.isVisible = false
                    }
                    MainViewModel.CurrencyEvent.Empty -> Unit
                }
            }
        }

    }
}