package com.stepanzalis.kotweather.model

import io.realm.RealmObject
import io.realm.annotations.Ignore
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

open class Main : RealmObject() {

    @Ignore
    var otherSymbols = DecimalFormatSymbols(Locale.US)

    @Ignore
    private val df = DecimalFormat(".##", otherSymbols)

    var humidity: String? = null

    var pressure: String? = null

    var temp_max: Double? = null

    var seaLevel: String? = null

    var temp_min: Double? = null

    var temp: Double? = null

    var grndLevel: String? = null

    fun tempMin(): String? {
        return df.format(temp_min)
    }

    fun temp(): String? {
        return df.format(temp)
    }

    fun tempMax(): String? {
        return df.format(temp_max)
    }

}