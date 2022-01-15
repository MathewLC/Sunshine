package com.example.sunshine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ShareCompat
import com.example.sunshine.MainActivity.Companion.WEATHER_STRING

class DetailActivity : AppCompatActivity() {

    private lateinit var weatherTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        weatherTextView = findViewById(R.id.weatherTextView)

        if (intent.hasExtra(WEATHER_STRING)) {
            weatherTextView.text = intent.getStringExtra(WEATHER_STRING)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            val mimeType = "text/plain"
            val title = "Choose the app to share"
            val intent = ShareCompat.IntentBuilder(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(weatherTextView.text)
                .intent

            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}