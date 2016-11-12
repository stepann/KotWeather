package com.stepanzalis.kotweather.model

import io.realm.RealmObject

open class Weather : RealmObject() {
    var id: String? = null

    var icon: String? = null

    var description: String? = null

    var main: String? = null

}
