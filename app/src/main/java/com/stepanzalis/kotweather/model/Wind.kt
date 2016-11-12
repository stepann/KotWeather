package com.stepanzalis.kotweather.model

import io.realm.RealmObject

open class Wind : RealmObject() {
    var speed: String? = null

    var deg: String? = null

}