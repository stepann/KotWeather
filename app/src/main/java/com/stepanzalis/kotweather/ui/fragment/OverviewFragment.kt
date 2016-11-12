package com.stepanzalis.kotweather.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.stepanzalis.kotweather.R
import com.stepanzalis.kotweather.api.RemoteFetch
import com.stepanzalis.kotweather.model.Forecast
import com.stepanzalis.kotweather.model.WeatherAPI
import com.stepanzalis.kotweather.ui.activity.MainActivity
import com.stepanzalis.kotweather.ui.fragment.interfaces.MyLocationListener
import com.stepanzalis.kotweather.ui.fragment.interfaces.SyncListener
import com.stepanzalis.kotweather.utils.Utils
import com.stepanzalis.kotweather.utils.WeatherIcon
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_overview.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.properties.Delegates

open class OverviewFragment : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    object TAG { val TAG = "TAG" }

    var forecast: Forecast? = null
    var weather: WeatherAPI? = null
    var longitude: String? = null
    var latitude: String? = null
    var mGoogleApiClient: GoogleApiClient? = null
    var realm: Realm by Delegates.notNull()

    var todayTemp: TextView? = null
    var lastUpdate: TextView? = null
    var weatherIcon: ImageView? = null

    var city: String? = null
    var icon: String? = null

    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildGoogleApiClient()

        //if device is not connected, show data from database and show message
        if (!Utils.isConnected(context)) {
            dataFromDatabase()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_overview, container, false)

        todayTemp = activity.findViewById(R.id.todayTemperature) as TextView
        lastUpdate = activity.findViewById(R.id.lastUpdateTimeMark) as TextView
        weatherIcon = activity.findViewById(R.id.ivWeatherIcon) as ImageView

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh.setOnRefreshListener {
            swipeRefreshUpdate()
        }

        fab.setOnClickListener { showAlertDialog() }

        //click to sync in toolbar
        (activity as MainActivity).setSyncListener(object : SyncListener {
            override fun sync() {
                swipeRefreshUpdate()
            }
        })

        //click to myLocation in toolbar,finding device location
        (activity as MainActivity).setLocationListener(object : MyLocationListener {
            override fun myLocation() {
                onConnected(Bundle())
                Utils.savePreference("city", "", activity)
            }
        })
    }

    fun renderWeather(weather: WeatherAPI?) {
        activity.runOnUiThread {
            (activity as MainActivity).supportActionBar?.title = weather?.name + ", " + weather?.sys?.country
            todayTemp?.text = weather?.main?.temp()
            lastUpdate?.text = weather?.requestTime

            todayTempMax?.text = weather?.main?.tempMax()
            todayTempMin?.text = weather?.main?.tempMin()
            tvTodayWind?.text = weather?.wind?.speed
            tvTodayPressure?.text = weather?.main?.pressure
            tvTodayHumidity?.text = weather?.main?.humidity
            icon = weather?.weather?.get(0)?.icon

            setWeatherIcon()

            if (swipeRefresh != null) {
                if (swipeRefresh.isRefreshing) {
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    fun setWeatherIcon() {
        when (icon) {
            WeatherIcon.SUNNY.iconID -> {
                weatherIcon?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_weather_sunny))
            }
            WeatherIcon.CLOUDY.iconID -> {
                weatherIcon?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_weather_cloudy))
            }
            WeatherIcon.STORM.iconID -> {
                weatherIcon?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_weather_lightning))
            }
            WeatherIcon.SNOWY.iconID -> {
                weatherIcon?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_weather_snowy))
            }
            WeatherIcon.RAINY.iconID -> {
                weatherIcon?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_weather_rainy))
            }
            else -> {
                weatherIcon?.setImageDrawable(null)
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(context)

        val dialogLayout = getLayoutInflater(Bundle()).inflate(R.layout.dialog_search_city, null)
        alertDialogBuilder.setView(dialogLayout)
        val image = dialogLayout.findViewById(R.id.imageViewCity) as ImageView
        val city = dialogLayout.findViewById(R.id.et_city) as AutoCompleteTextView

        Glide.with(this).load(R.drawable.city_img).centerCrop().into(image)

        alertDialogBuilder.setPositiveButton(R.string.OK) { dialog, which ->
            val mCity = city.text.toString().replace(" ", "").toLowerCase()
            getWeatherData(true, mCity, null, null)
            Utils.savePreference("city", mCity, activity)
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton(R.string.cancel) { dialog, which -> dialog.dismiss() }
        alertDialogBuilder.create().show()
    }

    fun dataFromDatabase() {
        toast(R.string.error_network)
        weather = realm.where(WeatherAPI::class.java).findFirst()
        renderWeather(weather)
    }

    fun getWeatherData(isCity: Boolean, city: String?, lat: String?, lon: String?) {
        if (Utils.isConnected(activity)) {
            doAsync {
                weather = RemoteFetch.getForecast(isCity, city, lat, lon)
                //Utils.savePreference("lat", weather?.coord?.lat!!, activity)
                //Utils.savePreference("lon", weather?.coord?.lon!!, activity)
                if (true) {
                    renderWeather(weather)
                } else {
                    swipeRefresh.isRefreshing = false
                    toast(R.string.error)
                }
            }
        } else {
            dataFromDatabase()
        }
    }

    override fun onConnected(bundle: Bundle?) {
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
        latitude = String.format(Locale.US, "%.2f", mLastLocation.latitude)
        longitude = String.format(Locale.US, "%.2f", mLastLocation.longitude)
        getWeatherData(false, null, latitude, longitude)
        //longForecast(false, null, latitude, longitude)
        Utils.savePreference("city", "", activity)
    }

    @Synchronized private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient
                .Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient?.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.i("Location: failed", "Location access failed ")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    fun swipeRefreshUpdate() {
        if (Utils.isConnected(activity)) {
            //get searched city name from sharedPreferences and update it
            city = Utils.getPreferenceString("city", activity)
            if (city!!.isEmpty()) {
                getWeatherData(false, null, weather?.coord?.lat, weather?.coord?.lon)
            } else {
                getWeatherData(true, city, null, null)
            }
        } else {
            dataFromDatabase()
        }
    }

    fun longForecast(isCity: Boolean, city: String?, lat: String?, lon: String?) {
        doAsync {
            forecast = RemoteFetch.getFiveDaysForecast(isCity, city, lat, lon)
        }
    }

    fun newInstance(): OverviewFragment {
        val fragment = OverviewFragment()
        val args = Bundle()
        fragment.arguments = args
        return fragment
    }
}

