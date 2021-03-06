package com.example.uppgift3

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*


lateinit var fusedLocationClient: FusedLocationProviderClient
lateinit var locationRequest: LocationRequest
lateinit var locationCallback: LocationCallback



class MainActivity : AppCompatActivity() {

    val requestPermissionCode=1
    lateinit var mCurrentLocation: Location
    lateinit var newLocation: LocationResult
    var resultDistance: Double = 0.0


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(this)
        locationRequest= LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=1000
        locationRequest.fastestInterval=1000
        locationRequest.numUpdates=100
        getLastLocation()

        locationCallback=object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                onLocationChanged(p0!!.getLastLocation())
            }


        }

        val locationButton = findViewById<Button>(R.id.location_button)
        locationButton.setOnClickListener {
            getLastLocation()
            uppdateViewlocation()



        }
        val distanceButton = findViewById<Button>(R.id.distance_button)
        distanceButton.setOnClickListener{
            clearView()
            getLastLocation()
            countDistance()
            uppdateViewDistance()


        }

    }
    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }


    private fun countDistance() {
        var longitud=findViewById<EditText>(R.id.longitude).text.toString()
        var latitude=findViewById<EditText>(R.id.latitude).text.toString()
        val startPoint = Location("locationA")
        startPoint.latitude = latitude.toDouble()
        startPoint.longitude = longitud.toDouble()
        val endPoint = Location("locationA")
        endPoint.latitude = mCurrentLocation.latitude
        endPoint.longitude = mCurrentLocation.longitude
        resultDistance=0.0
        resultDistance = startPoint.distanceTo(endPoint).toDouble()/1000
        println("startlat: "+startPoint.latitude+"startLong: "+startPoint.longitude+"end lat: "+endPoint.latitude+" end long:"+endPoint.latitude)
    }

    private fun clearView() {
        findViewById<TextView>(R.id.longitude_readout).setText("")
        findViewById<TextView>(R.id.latitude_readout).setText("")
        findViewById<TextView>(R.id.distance_result).setText("")
    }

    private fun uppdateViewlocation() {
        findViewById<TextView>(R.id.longitude_readout).setText("")
        findViewById<TextView>(R.id.latitude_readout).setText("")
        latitude_readout.append(mCurrentLocation.latitude.toString())
        longitude_readout.append(mCurrentLocation.longitude.toString())

    }
    private fun uppdateViewDistance() {
        findViewById<TextView>(R.id.longitude_readout).setText("")
        findViewById<TextView>(R.id.latitude_readout).setText("")
        findViewById<TextView>(R.id.distance_result).setText("")
        latitude_readout.append(mCurrentLocation.latitude.toString())
        longitude_readout.append(mCurrentLocation.longitude.toString())
        distance_result.append(resultDistance.toString())

    }


    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest= LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=1

    }


    private fun onLocationChanged(lastLocation: Location?) {
        if (lastLocation != null) {
            mCurrentLocation=lastLocation

        }
    }

    private fun getLastLocation() {
        println("2h")
        if (checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions()
        }else{
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location?->
                    println(location)
                    if(location!=null){
                        mCurrentLocation=location
                    }
                }
        }
    }

    private fun requestPermissions() {
        requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestPermissionCode
        )
        this.recreate()
    }
    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }







    }


