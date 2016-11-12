package com.stepanzalis.kotweather.utils


object Constants {

    val API_KEY = "46135f58888b9aea7eb7a98a39e85c6e"

    //actual weather forecast
    val WEATHER_API_CITY = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&"
    val WEATHER_API_COORDINATES = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&"

    //five days weather forecast
    val WEATHER_API_CITY_DAYS = "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&"
    val WEATHER_API_COORDINATES_DAYS = "http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&"
}
