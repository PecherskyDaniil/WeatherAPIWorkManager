package com.example.weatherapiworkmanager

class WeatherObj {
    val main: Main = Main()
    var weather:List<Weather> = listOf(Weather());
    val wind:Wind=Wind();
}