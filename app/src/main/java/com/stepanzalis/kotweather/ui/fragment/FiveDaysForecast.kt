package com.stepanzalis.kotweather.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stepanzalis.kotweather.R

class FiveDaysForecast : Fragment() {

    var fragment: OverviewFragment? = null
    var city: String? = null
    var lat: String? = null
    var lot: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_five_days_forecast, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    fun newInstance(): FiveDaysForecast {
        val fragment = FiveDaysForecast()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }
}
