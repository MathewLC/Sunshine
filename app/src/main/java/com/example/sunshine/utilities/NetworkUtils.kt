package com.example.sunshine.utilities

import android.net.Uri
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*


/**
 * These utilities will be used to communicate with the weather servers.
 */
object NetworkUtils {
    private val TAG = NetworkUtils::class.java.simpleName
    private const val DYNAMIC_WEATHER_URL = "https://andfun-weather.udacity.com/weather"
    private const val STATIC_WEATHER_URL = "https://andfun-weather.udacity.com/staticweather"
    private const val FORECAST_BASE_URL = STATIC_WEATHER_URL

    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */
    /* The format we want our API to return */
    private const val format = "json"

    /* The units we want our API to return */
    private const val units = "metric"

    /* The number of days we want our API to return */
    private const val numDays = 14
    const val QUERY_PARAM = "q"
    const val LAT_PARAM = "lat"
    const val LON_PARAM = "lon"
    const val FORMAT_PARAM = "mode"
    const val UNITS_PARAM = "units"
    const val DAYS_PARAM = "cnt"

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    fun buildUrl(locationQuery: String?): URL? {
        val builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            .appendQueryParameter(QUERY_PARAM, locationQuery)
            .appendQueryParameter(FORMAT_PARAM, format)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(DAYS_PARAM, numDays.toString())
            .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (ex: MalformedURLException){
            ex.printStackTrace()
        }

        return url
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    fun buildUrl(lat: Double?, lon: Double?): URL? {
        /** This will be implemented in a future lesson  */
        return null
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection = url.openConnection() as HttpURLConnection
        return try {
            val `in` = urlConnection.inputStream
            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}