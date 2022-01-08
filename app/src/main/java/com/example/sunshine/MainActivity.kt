package com.example.sunshine

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.sunshine.data.SunshinePreferences
import com.example.sunshine.utilities.NetworkUtils
import java.io.IOException
import java.net.URL
import com.example.sunshine.R.id.action_refresh

class MainActivity : AppCompatActivity() {

    private lateinit var _weatherDisplayTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        _weatherDisplayTextView = findViewById(R.id.tv_weather_data)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.forecast,menu)
        return true
    }

    inner class WeatherQueryTask : AsyncTask<URL, Void, String>() {
        override fun doInBackground(vararg params: URL?): String? {
            val searchUrl = params[0]
            var weatherRequestResult: String? = null
            try {
                weatherRequestResult = searchUrl?.let { NetworkUtils.getResponseFromHttpUrl(it) }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return weatherRequestResult
        }

        override fun onPostExecute(result: String?) {
            _weatherDisplayTextView.text = result
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == action_refresh) {
            loadWeatherData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadWeatherData() {
        val userPreferredLocation = SunshinePreferences. getPreferredWeatherLocation(this)
        val url = NetworkUtils.buildUrl(userPreferredLocation)
        WeatherQueryTask().execute(url)
    }

}