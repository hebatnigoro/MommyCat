package com.example.mommycat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mommycat.databinding.ActivityAboutUsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AboutUsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val jollyCatStore = LatLng(-6.20175, 106.78208)
        googleMap.addMarker(MarkerOptions().position(jollyCatStore).title("JollyCat's Store"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jollyCatStore, 15f))
    }
}
