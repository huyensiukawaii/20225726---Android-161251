package com.example.moneyexchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var editTextFrom: EditText
    private lateinit var editTextTo: EditText
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner

    private val exchangeRates: MutableMap<String, Double> = mutableMapOf()

    private lateinit var currencies: Array<String>

    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupExchangeRates()
        setupSpinners()
        setupListeners()
    }

    private fun initializeViews() {
        editTextFrom = findViewById(R.id.editTextFrom)
        editTextTo = findViewById(R.id.editTextTo)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)
    }

    private fun setupExchangeRates() {
        exchangeRates["USD"] = 1.0
        exchangeRates["VND"] = 25400.0
        exchangeRates["EUR"] = 0.92
        exchangeRates["JPY"] = 157.5
        exchangeRates["GBP"] = 0.79
        exchangeRates["AUD"] = 1.5
        exchangeRates["CAD"] = 1.37
        exchangeRates["CHF"] = 0.89
        exchangeRates["CNY"] = 7.25
        exchangeRates["KRW"] = 1380.0
        exchangeRates["SGD"] = 1.35
        exchangeRates["HKD"] = 8.5

        currencies = exchangeRates.keys.toTypedArray()
        currencies.sort()
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currencies
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFrom.adapter = adapter
        spinnerTo.adapter = adapter

        spinnerFrom.setSelection(adapter.getPosition("USD"))
        spinnerTo.setSelection(adapter.getPosition("VND"))
    }

    private fun setupListeners() {
        editTextFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return
                convertCurrency(true)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        editTextTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) return
                convertCurrency(false)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        val spinnerListener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertCurrency(true)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        spinnerFrom.onItemSelectedListener = spinnerListener
        spinnerTo.onItemSelectedListener = spinnerListener
    }

    private fun convertCurrency(fromTo: Boolean) {
        val currencyFromStr: String = spinnerFrom.selectedItem.toString()
        val currencyToStr: String = spinnerTo.selectedItem.toString()
        var rateFrom = exchangeRates.getValue(currencyFromStr)
        var rateTo = exchangeRates.getValue(currencyToStr)

        val input: EditText
        val output: EditText

        if (fromTo) {
            input = editTextFrom
            output = editTextTo
        } else {
            input = editTextTo
            output = editTextFrom
            val temp = rateFrom
            rateFrom = rateTo
            rateTo = temp
        }

        val amountStr = input.text.toString()

        if (amountStr.isEmpty() || amountStr == ".") {
            isUpdating = true
            output.setText("")
            isUpdating = false
            return
        }

        val amount = amountStr.toDoubleOrNull()
        if (amount == null) {
            isUpdating = true
            output.setText("")
            isUpdating = false
            return
        }

        val amountInBase = amount / rateFrom
        val resultAmount = amountInBase * rateTo

        isUpdating = true
        output.setText(String.format(Locale.US, "%.2f", resultAmount))
        isUpdating = false
    }
}