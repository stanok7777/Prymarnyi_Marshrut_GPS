package com.ghostroute.stealthnav.core

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import com.ghostroute.stealthnav.R

class MainActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        
        setContentView(R.layout.activity_main)
        
        map = findViewById(R.id.mapView)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        map.controller.setZoom(15.0)
        
        checkPermissions()
        
        val stealthBtn = findViewById<Button>(R.id.vipButton)
        val statusText = findViewById<TextView>(R.id.statusText)
        
        var isStealth = false
        stealthBtn.setOnClickListener {
            isStealth = !isStealth
            if (isStealth) {
                stealthBtn.text = "ВИМКНУТИ STEALTH MODE"
                stealthBtn.setBackgroundColor(Color.parseColor("#444444"))
                statusText.text = "РЕБ-ЗАХИСТ: АКТИВНО (ІНЕРЦІЯ)"
                statusText.setTextColor(Color.parseColor("#4CAF50"))
                Toast.makeText(this, "Інерціальну навігацію активовано", Toast.LENGTH_SHORT).show()
            } else {
                stealthBtn.text = "АКТИВУВАТИ STEALTH MODE"
                stealthBtn.setBackgroundColor(Color.parseColor("#4CAF50"))
                statusText.text = "СИГНАЛ GPS: ВРАЗЛИВИЙ (МОЖЛИВИЙ РЕБ)"
                statusText.setTextColor(Color.parseColor("#FF5252"))
            }
        }
    }
    
    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        } else {
            setupLocation()
        }
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupLocation()
        }
    }
    
    private fun setupLocation() {
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        map.overlays.add(locationOverlay)
    }
    
    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}