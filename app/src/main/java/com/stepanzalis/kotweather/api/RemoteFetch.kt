package com.stepanzalis.kotweather.api

import com.google.gson.Gson
import com.stepanzalis.kotweather.model.Forecast
import com.stepanzalis.kotweather.model.WeatherAPI
import com.stepanzalis.kotweather.utils.Constants
import com.stepanzalis.kotweather.utils.Utils
import io.realm.Realm
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.properties.Delegates

object RemoteFetch {

    var realm: Realm by Delegates.notNull()
    var url: URL by Delegates.notNull()

    fun getForecast(isCity: Boolean, city: String?, lat: String?, lon: String?): WeatherAPI? {
        try {
            if (isCity) {
                url = URL(String.format(Constants.WEATHER_API_CITY, city))
            } else {
                url = URL(String.format(Constants.WEATHER_API_COORDINATES, lat, lon))
            }

            val connection = url.openConnection() as HttpURLConnection
            connection.addRequestProperty("x-api-key", Constants.API_KEY)

            realm = Realm.getDefaultInstance()

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val json: StringBuffer? = StringBuffer(1024)
            val tmp = reader.readLine()
            json?.append(tmp)?.append("\n")

            reader.close()

            val data = JSONObject(json.toString())
            val gson = Gson()
            val weather: WeatherAPI = gson.fromJson(json.toString(), WeatherAPI::class.java)
            weather.requestTime = Utils.getActualTime()

            realm.executeTransaction {
                realm.copyToRealmOrUpdate(weather)
            }

            if (data.getInt("cod") != 200) {
                return null
            }

            return weather

        } catch (e: Exception) {
            return null
        }
    }

    fun getFiveDaysForecast(isCity: Boolean, city: String?, lat: String?, lon: String?): Forecast? {
        try {
            if (isCity) {
                url = URL(String.format(Constants.WEATHER_API_CITY_DAYS, city))
            } else {
                url = URL(String.format(Constants.WEATHER_API_COORDINATES_DAYS, lat, lon))
            }

            realm = Realm.getDefaultInstance()

            val connection = url.openConnection() as HttpURLConnection
            connection.addRequestProperty("x-api-key", Constants.API_KEY)

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val json: StringBuffer? = StringBuffer(1024)
            val tmp = reader.readLine()
            json?.append(tmp)?.append("\n")

            reader.close()

            val data = JSONObject(json.toString())
            val gson = Gson()
            val forecast: Forecast? = gson.fromJson(json.toString(), Forecast::class.java)

            realm.executeTransaction { realm.copyToRealmOrUpdate(forecast) }

            if (data.getInt("cod") != 200) {
                return null
            }

            return forecast
        } catch (e: Exception) {
            return null
        }
    }
}




