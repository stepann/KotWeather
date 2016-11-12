package com.stepanzalis.kotweather.model

import com.stepanzalis.kotweather.model.Coord
import com.stepanzalis.kotweather.model.Sys
import io.realm.RealmObject

open class City : RealmObject() {

    var coord: Coord? = null

    var id: String? = null

    var sys: Sys? = null

    var name: String? = null

    var population: String? = null

    var country: String? = null
}