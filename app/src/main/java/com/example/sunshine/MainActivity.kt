package com.example.sunshine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var  _weatherDisplayTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        _weatherDisplayTextView = findViewById(R.id.tv_weather_data)

        WeatherMock.getDummyWeatherData().forEach { stringLine ->
            _weatherDisplayTextView.append("$stringLine \n\n\n")
        }


    }
}