package com.example.weatherapiworkmanager

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

class WeatherWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams)
{
    var weath=Array<String?>(3,{"a";"b";"c"})
    var temp:FloatArray = floatArrayOf(0F, 1F, 2F)
    var ind=0;
override fun doWork(): Result {
        val cityIDs = inputData.getStringArray("cities")
        cityIDs?.let {
            for (c in it) {
                loadWeather(c)
                Log.d("mytag", "city $c")
            }
        }
        Log.d("mytag", "work success")
        val data = Data.Builder().putFloatArray("temp", temp).putStringArray("weath", weath).build()
        return Result.success(data)
    }
    fun loadWeather(city:String) {
        val API_KEY = "6aba2600613368466e54022a798f2fe9" // TODO: ключ загрузить из строковых ресурсов
        // TODO: в строку подставлять API_KEY и город (выбирается из списка или вводится в поле)
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_KEY+"&units=metric";
        val stream = URL(weatherURL).getContent() as InputStream
        // JSON отдаётся одной строкой,
        //val data = Scanner(stream).nextLine()
        val gson = Gson() // конвеер для (де)сериализации
        val weather_data= gson.fromJson(InputStreamReader(stream), WeatherObj::class.java)
        weath[ind]=weather_data.weather[0].main
        temp[ind]=weather_data.main.temp
        ind+=1
        // TODO: предусмотреть обработку ошибок (нет сети, пустой ответ)
        Log.d("mytag", weather_data.main.temp.toString())
    }
}
