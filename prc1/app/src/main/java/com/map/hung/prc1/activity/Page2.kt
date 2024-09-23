package com.map.hung.prc1

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Page2 : Activity() {
    private lateinit var height : EditText
    private lateinit var weight : EditText
    private lateinit var calculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page2)

        // Add your code here
        // create a back end file for creating a calculator to count BMI point with height and weight
        height = findViewById(R.id.height)
        weight = findViewById(R.id.weight)
        calculate = findViewById(R.id.calculate)

        calculate.setOnClickListener {
            val h = height.text.toString().toDouble()
            val w = weight.text.toString().toDouble()
            val bmi = w / (h * h)
            println("BMI: $bmi")
        }



    }
}