package com.stepanzalis.kotweather.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.stepanzalis.kotweather.R
import com.stepanzalis.kotweather.api.FactoryAPI
import com.stepanzalis.kotweather.model.Forecast
import com.stepanzalis.kotweather.model.WeatherAPI
import com.stepanzalis.kotweather.ui.fragment.FiveDaysForecast
import com.stepanzalis.kotweather.ui.fragment.OverviewFragment
import com.stepanzalis.kotweather.ui.fragment.interfaces.MyLocationListener
import com.stepanzalis.kotweather.ui.fragment.interfaces.SyncListener
import com.stepanzalis.kotweather.utils.Constants
import com.stepanzalis.kotweather.utils.Utils
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {

    var city: String? = null
    var lat: String? = null
    var lon: String? = null

    var weatherApi: WeatherAPI? = null
    var forecast: Forecast? = null

    internal var syncListener: SyncListener? = null
    internal var locationListener: MyLocationListener? = null

    val PERMISSION_ACCESS_COARSE_LOCATION: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set orientation to portrait
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        //check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_ACCESS_COARSE_LOCATION)
        } else {
            viewPagerSetup()
            //Utils.savePreference("city", "London", this)
            //callCity()
            callCoord()
            //longCallCity()
            //longCoord()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_refresh -> { syncListener?.sync() }
            R.id.action_my_location -> { locationListener?.myLocation() }
            R.id.action_settings -> { val intent = Intent(this, SettingsActivity::class.java); startActivity(intent) }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_ACCESS_COARSE_LOCATION -> if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission", "succeed")
                Utils.savePreference("city", "London", this)
                viewPagerSetup()
            } else {
                toast("Permission failed")
                finish()
            }
        }
    }

    fun viewPagerSetup() {
        val viewPager = findViewById(R.id.main_container) as ViewPager
        val viewPagerTab = findViewById(R.id.viewpagertab) as SmartTabLayout

        val adapter = FragmentPagerItemAdapter(
                supportFragmentManager, FragmentPagerItems.with(this)
                .add("Přehled", OverviewFragment::class.java)
                .add("5 dní", FiveDaysForecast::class.java).create())

        viewPager.offscreenPageLimit = 2 // "cached" two fragments in memory
        viewPager.adapter = adapter
        viewPagerTab.setDistributeEvenly(true)

        viewPagerTab.setViewPager(viewPager)
    }

    fun callCity() {
        city = Utils.getPreferenceString("city", this)
        val call = FactoryAPI.get().getWeatherCity("London", "metric", Constants.API_KEY)
        call.enqueue(object : Callback<WeatherAPI> {
            override fun onResponse(call: Call<WeatherAPI>, response: Response<WeatherAPI>) {
                response.raw().toString()
                if (response.isSuccessful) {
                    weatherApi = response.body()
                }
            }
            override fun onFailure(call: Call<WeatherAPI>?, t: Throwable?) {
                Log.i("error", t.toString())
            }
        })
    }

    fun callCoord() {
        val call = FactoryAPI.get().getWeatherCoord("15.8", "50.2", "metric", Constants.API_KEY)
        call.enqueue(object : Callback<WeatherAPI> {
            override fun onResponse(call: Call<WeatherAPI>, response: Response<WeatherAPI>) {
                response.raw().toString()
                if (response.isSuccessful) {
                    weatherApi = response.body()
                }
            }
            override fun onFailure(call: Call<WeatherAPI>?, t: Throwable?) {
                Log.i("error", t.toString())
            }
        })
    }

    fun longCallCity() {
        val call = FactoryAPI.get().getLongWeatherCity(city, "metric", Constants.API_KEY)
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                response.raw().toString()
                if (response.isSuccessful) {
                    forecast = response.body()
                }
            }
            override fun onFailure(call: Call<Forecast>?, t: Throwable?) {
                Log.i("error", t.toString())
            }
        })
    }

    fun longCoord() {
        val call = FactoryAPI.get().getLongWeatherCoord(lat, lon, "metric", Constants.API_KEY)
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                response.raw().toString()
                if (response.isSuccessful) {
                    forecast = response.body()
                }
            }
            override fun onFailure(call: Call<Forecast>?, t: Throwable?) {
                Log.i("error", t.toString())
            }
        })
    }

    fun showFragment(fragment: Fragment, ID: Int, tag: String) {
        supportFragmentManager.beginTransaction()
                .replace(ID, fragment, tag)
                .commit()
    }

    fun setSyncListener(syncListener: SyncListener) {
        this.syncListener = syncListener
    }

    fun setLocationListener(locationListener: MyLocationListener) {
        this.locationListener = locationListener
    }
}




