package com.example.sunshine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshine.ForecastAdapter.ForecastAdapterViewHolder

class ForecastAdapter(

) : RecyclerView.Adapter<ForecastAdapterViewHolder>() {
    private var mWeatherData: List<String>? = null

    inner class ForecastAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mWeatherTextView: TextView = itemView.findViewById(R.id.tv_weather_data)

    }

    fun setWeatherData(data: List<String>?){
        mWeatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.forecast_list_item, parent, false)

        return ForecastAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastAdapterViewHolder, position: Int) {
        holder.mWeatherTextView.text = mWeatherData?.let { it[position] }
    }

    override fun getItemCount(): Int {
        return mWeatherData?.let { it.size } ?: 0
    }
}