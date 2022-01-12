package com.example.sunshine

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshine.data.SunshinePreferences
import com.example.sunshine.utilities.NetworkUtils
import java.net.URL
import com.example.sunshine.R.id.action_refresh

import com.example.sunshine.utilities.OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson
import java.lang.Exception

class MainActivity : AppCompatActivity(), ForecastAdapter.ForecastAdapterOnClickHandler {
    private lateinit var _mRecyclerView: RecyclerView
    private lateinit var _mForecastAdapter: ForecastAdapter

    private lateinit var _errorMessageTextView: TextView

    private lateinit var _progressBar: ProgressBar

    private var mToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)


        _mRecyclerView = findViewById(R.id.recyclerview_forecast)

        _errorMessageTextView = findViewById(R.id.tv_error_message)


        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        _mRecyclerView.layoutManager = layoutManager
        _mRecyclerView.setHasFixedSize(true)

        _mForecastAdapter = ForecastAdapter(this)
        _mRecyclerView.adapter = _mForecastAdapter

        _progressBar = findViewById(R.id.loading_progress_bar)

        /* Once all of our views are setup, we can load the weather data. */
        loadWeatherData();

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.forecast,menu)
        return true
    }

    inner class WeatherQueryTask : AsyncTask<String, Void, List<String>?>() {
        override fun onPreExecute() {
            _progressBar.visibility = VISIBLE
        }

        override fun doInBackground(vararg params: String): List<String>? {
            /* If there's no zip code, there's nothing to look up. */
            if (params.isEmpty()) {
                return null
            }
            val location = params[0]
            val weatherRequestUrl = NetworkUtils.buildUrl(location)
            return try {
                val jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(weatherRequestUrl!!)

                getSimpleWeatherStringsFromJson(this@MainActivity, jsonWeatherResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: List<String>?) {
            _progressBar.visibility = INVISIBLE
            result?.let {
                showWeatherData()
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                if(result.any())
                    _mForecastAdapter.setWeatherData(result)
            }

            if(result.isNullOrEmpty())
                showErrorMessage()
        }



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == action_refresh) {
            _mForecastAdapter.setWeatherData(null)
            loadWeatherData()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadWeatherData() {
        showWeatherData()
        val userPreferredLocation = SunshinePreferences. getPreferredWeatherLocation(this)
        WeatherQueryTask().execute(userPreferredLocation)
    }

    override fun onClick(itemClicked: String) {
        mToast?.cancel()
        val toastMessage = " \"$itemClicked\" was clicked."
        mToast = Toast.makeText(this,toastMessage, Toast.LENGTH_SHORT)
        mToast?.show()
    }

    private fun showWeatherData(){
        _errorMessageTextView.visibility = INVISIBLE
        _mRecyclerView.visibility = VISIBLE
    }

    private fun showErrorMessage(){
        _mRecyclerView.visibility = INVISIBLE
        _errorMessageTextView.visibility = VISIBLE
    }

}