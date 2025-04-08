package com.example.weatherapiworkmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.workDataOf

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var textviews= arrayOf(findViewById<TextView>(R.id.text1),findViewById<TextView>(R.id.text2),findViewById<TextView>(R.id.text3))
        val citiesData = workDataOf("cities" to arrayOf("Irkutsk", "Moscow", "Vladivostok"))
        val workRequest = OneTimeWorkRequest.Builder(WeatherWorker::class.java)
            .setInputData(citiesData)
            .build()
        val worker=WorkManager.getInstance(this)
        worker.enqueue(workRequest)
        worker.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, androidx.lifecycle.Observer {
                if (it != null) {
                    if (it.state==WorkInfo.State.SUCCEEDED) {
                        for(i in 0..2){
                            textviews[i].text= (it.outputData.getStringArray("weath")?.get(i) ?:"None")+" "+(it.outputData.getFloatArray("temp")?.get(i).toString())
                        }
                    }

                    Log.d("MyTag",it.outputData.toString())
                }
            })



        // TODO: реализовать запрос погоды
        // TODO: какие параметры позволяет задать WorkManager (макс число одновр заданий)
        // TODO: реализовать выполнение заданий по очереди

    }
}