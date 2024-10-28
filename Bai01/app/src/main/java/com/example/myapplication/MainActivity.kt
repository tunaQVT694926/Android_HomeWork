package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextSource: EditText
    private lateinit var editTextTarget: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner

    // Giả lập tỷ giá
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "VND" to 23000.0,
        "JPY" to 110.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ các view với ID từ layout XML
        editTextSource = findViewById(R.id.editTextSource)
        editTextTarget = findViewById(R.id.editTextTarget)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerTargetCurrency = findViewById(R.id.spinnerTargetCurrency)

        // Cài đặt các loại tiền tệ vào Spinner
        val currencyList = exchangeRates.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSourceCurrency.adapter = adapter
        spinnerTargetCurrency.adapter = adapter

        // Lắng nghe sự thay đổi của số tiền nguồn
        editTextSource.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = updateConversion()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Lắng nghe sự thay đổi của các Spinner
        spinnerSourceCurrency.setOnItemSelectedListener { updateConversion() }
        spinnerTargetCurrency.setOnItemSelectedListener { updateConversion() }
    }

    // Hàm cập nhật kết quả chuyển đổi
    private fun updateConversion() {
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val targetCurrency = spinnerTargetCurrency.selectedItem.toString()

        val sourceAmount = editTextSource.text.toString().toDoubleOrNull()
        if (sourceAmount != null) {
            val sourceRate = exchangeRates[sourceCurrency] ?: 1.0
            val targetRate = exchangeRates[targetCurrency] ?: 1.0
            val convertedAmount = sourceAmount * (targetRate / sourceRate)

            val df = DecimalFormat("#.##")
            editTextTarget.setText(df.format(convertedAmount))
        } else {
            editTextTarget.setText("")
        }
    }
}
