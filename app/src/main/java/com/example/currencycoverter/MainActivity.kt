package com.example.currencycoverter

import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.currencycoverter.databinding.ActivityMainBinding
import com.example.currencycoverter.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is MainViewModel.CurrencyEvent.Success ->{
                        binding.progressCircular.isVisible = false
                        binding.tvResult.isVisible = true
                        binding.tvResult.setTextColor(baseContext.getColor(R.color.design_default_color_on_primary))
                        binding.tvResult.text = event.resultText
                        Toast.makeText(this@MainActivity, "yooo success "+event.resultText, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onCreate: event.resultText")
                    }
                    is MainViewModel.CurrencyEvent.Failure ->{
                        binding.progressCircular.isVisible = false
                        binding.tvResult.isVisible = true
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorText
                        Toast.makeText(this@MainActivity,"yooo " + event.errorText, Toast.LENGTH_SHORT).show();

                    }
                    is MainViewModel.CurrencyEvent.Loading ->{
                        binding.progressCircular.isVisible = true
                        binding.tvResult.isVisible = false

                    }
                }
            }
        }

    }
}