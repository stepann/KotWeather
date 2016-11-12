package com.stepanzalis.kotweather.model

import io.realm.RealmObject

open class Sys : RealmObject() {

    var message: String? = null

    var sunset: String? = null

    var sunrise: String? = null

    var country: String? = null

}