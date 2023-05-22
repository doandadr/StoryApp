package com.dicoding.doanda.storyapp.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.doanda.storyapp.R
import com.dicoding.doanda.storyapp.data.repository.Result
import com.dicoding.doanda.storyapp.data.response.partials.ListStoryItem
import com.dicoding.doanda.storyapp.databinding.ActivityMapsBinding
import com.dicoding.doanda.storyapp.ui.utils.factory.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel by viewModels<MapsViewModel> { ViewModelFactory.getInstance(this) }

    private lateinit var fusedLoc: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story Map"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this@MapsActivity, R.raw.map_style))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMarkers()
        getMyLocation()
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLoc = LocationServices.getFusedLocationProviderClient(this)
            fusedLoc.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val position = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 5f))
                } else {
                    Toast.makeText(this, "Location not saved", Toast.LENGTH_SHORT).show()
                }
            }
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun setMarkers() {
        mapsViewModel.getUser().observe(this) { user ->
            if (user.isLoggedIn) {
                val locationInfo = 1
                mapsViewModel.getAllStoriesLocation(user.bearerToken, locationInfo).observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            showMarkers(result.data.listStory)
                        }
                        is Result.Loading -> {}
                        is Result.Error -> {
                            Toast.makeText(this@MapsActivity, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showMarkers(listStory: List<ListStoryItem?>?) {
        if (listStory != null) {
            for (story in listStory) {
                if (story?.lat == null || story.lon == null) continue
                val position = LatLng(story.lat.toDouble(), story.lon.toDouble())
                mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(story.name)
                        .snippet(story.description)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )
            }
        }
    }
}