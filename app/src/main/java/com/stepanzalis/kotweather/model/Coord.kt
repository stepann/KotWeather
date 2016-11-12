package com.stepanzalis.kotweather.model

import io.realm.RealmObject

open class Coord : RealmObject() {
    var lon: String? = null

    var lat: String? = null
}