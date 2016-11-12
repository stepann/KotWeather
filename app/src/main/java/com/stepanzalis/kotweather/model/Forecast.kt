package com.stepanzalis.kotweather.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Forecast : RealmObject() {

    @PrimaryKey
    var message: String? = null

    var cnt: String? = null

    var cod: String? = null

    var list: RealmList<WeatherAPI>? = null

    var city: City? = null

    var sys: Sys? = null
}