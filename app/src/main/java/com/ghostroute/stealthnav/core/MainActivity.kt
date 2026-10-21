package com.ghostroute.stealthnav

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private val PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // CRITICAL FIX: osmdroid requires user agent to download tiles
        Configuration.getInstance().userAgentValue = packageName
        
        setContentView(R.layout.activity_main)
        
        map = findViewById(R.id.mapView)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        
        // Default point (Kyiv) before GPS locks
        map.controller.setZoom(15.0)
        map.controller.setCenter(GeoPoint(50.4501, 30.5234))

        checkPermissions()
        
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        val btnRec = findViewById<Button>(R.id.btnRec)
        var isRecording = false
        btnRec.setOnClickListener {
            isRecording = !isRecording
            if (isRecording) {
                btnRec.setBackgroundColor(Color.parseColor("#FF5252"))
                btnRec.text = "⏹ STOP"
                Toast.makeText(this, "Запис логів маршруту почато", Toast.LENGTH_SHORT).show()
            } else {
                btnRec.setBackgroundColor(Color.parseColor("#4CAF50"))
                btnRec.text = "⏺ REC"
                Toast.makeText(this, "Трек збережено", Toast.LENGTH_SHORT).show()
            }
        }

        val stealthBtn = findViewById<Button>(R.id.stealthButton)
        val statusText = findViewById<TextView>(R.id.statusText)
        var isStealth = false
        stealthBtn.setOnClickListener {
            isStealth = !isStealth
            if (isStealth) {
                stealthBtn.text = "ВИМКНУТИ ANTI-РЕБ (LBS)"
                stealthBtn.setBackgroundColor(Color.parseColor("#444444"))
                statusText.text = "ANTI-РЕБ: Позиція за вежею зв'язку"
                statusText.setTextColor(Color.parseColor("#4CAF50"))
                Toast.makeText(this, "Імітація захисту від РЕБ активована.", Toast.LENGTH_SHORT).show()
            } else {
                stealthBtn.text = "АКТИВУВАТИ ANTI-РЕБ ЗАХИСТ"
                stealthBtn.setBackgroundColor(Color.parseColor("#4CAF50"))
                statusText.text = "СИГНАЛ GPS: ВРАЗЛИВИЙ (МОЖЛИВИЙ РЕБ)"
                statusText.setTextColor(Color.parseColor("#FF5252"))
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
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