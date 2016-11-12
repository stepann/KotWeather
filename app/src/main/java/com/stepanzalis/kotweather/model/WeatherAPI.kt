package com.stepanzalis.kotweather.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class WeatherAPI : RealmObject() {

    @PrimaryKey
    var id: String? = null

    var dt: String? = null

    var dt_text: String? = null

    var clouds: Clouds? = null

    var coord: Coord? = null

    var wind: Wind? = null

    var cod: String? = null

    var sys: Sys? = null

    var name: String? = null

    var base: String? = null

    var weather: RealmList<Weather>? = null

    var main: Main? = null

    var requestTime: String? = null
}
