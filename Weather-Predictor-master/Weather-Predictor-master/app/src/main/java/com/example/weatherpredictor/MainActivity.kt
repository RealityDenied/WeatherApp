package com.example.weatherpredictor

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


//API id: b2ad40928f619ca5ea974b80cf33c490

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        fetchWeatherData("Jamshedpur")
        searcCity()
    }

    private fun searcCity() {
        val City= findViewById<SearchView>(R.id.SVlocation)
        City.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    fetchWeatherData(p0)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return true

            }

        } )
    }


    private fun fetchWeatherData(CityNme:String){
        val response= WeatherService.ApiInstance.getWeatherDAta("$CityNme","metric")
        response.enqueue(object :Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody=response.body()
                if ( response.isSuccessful && responseBody!=null )
                {
                    val currTemp=findViewById<TextView>(R.id.CurrTemp)
                    val maxTemp= findViewById<TextView>(R.id.Max)
                    val minTemp=findViewById<TextView>(R.id.Min)
                    val CityName= findViewById<TextView>(R.id.TVcityName)
                    val humidity=findViewById<TextView>(R.id.TVhumidityValue)
                    val wind= findViewById<TextView>(R.id.TVwindValue)
                    val type=findViewById<TextView>(R.id.TVweather)
                    val feelLike= findViewById<TextView>(R.id.FeelsLike)
                    val sunriseTime= findViewById<TextView>(R.id.TVsunrisevalue)
                    val sunsetTime= findViewById<TextView>(R.id.TVsunsetValue)
                    val Pressure= findViewById<TextView>(R.id.tvPaValue)
                    val Visibilty= findViewById<TextView>(R.id.TVVisibilityValue)
                    val temperature = responseBody.main.temp.toString()
                    currTemp.text= "$temperature°C"
                    val maxTemperature= responseBody.main.temp_max.toString()
                    maxTemp.text="Max: $maxTemperature°C"
                    val minTemperature= responseBody.main.temp_min.toString()
                    minTemp.text="Mix: $minTemperature°C"
                    val humidityValue= responseBody.main.humidity.toString()
                    humidity.text="$humidityValue%"
                    val windSpeed= responseBody.wind.speed.toString()
                    wind.text="$windSpeed m/s"
                    CityName.text= CityNme
                    val feelsLike = responseBody.main.feels_like.toString()
                    feelLike.text="Feels like $feelsLike°C"
                    val Sunrise= responseBody.sys.sunrise
                    sunriseTime.text=time(Sunrise)
                    val Sunset= responseBody.sys.sunset
                    sunsetTime.text=time(Sunset)
                    val WeatherType=responseBody.weather[responseBody.weather.size-1].description.toString()
                    type.text=WeatherType.uppercase()
                    val PaVAlue=responseBody.main.pressure.toString()
                    Pressure.text="$PaVAlue hPa"
                    val VisibilityValue= responseBody.visibility.toString()
                    Visibilty.text="$VisibilityValue m"


                    //Log.d("response","$feelsLike")
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Log.d("response","Error in data",t)
            }

        })

    }




    private fun hasForegroundLocation()=
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED

    private fun hasBackgroundLocation()=
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)==PackageManager.PERMISSION_GRANTED



    private fun requestPermissions()
    {
        var permissionToReq= mutableListOf<String>()
        if(!hasForegroundLocation())
            permissionToReq.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        if(!hasBackgroundLocation())
            permissionToReq.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

        if(permissionToReq.isNotEmpty())
            ActivityCompat.requestPermissions(this,permissionToReq.toTypedArray(),0)
    }

    private fun time(timestamp: Int): String{
        val sdf= SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(timestamp*1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==0 && grantResults.isNotEmpty())
        {
            for(i in grantResults.indices)
                Log.d("Permissions Request", "${permissions[i]} granted")
        }
    }
}