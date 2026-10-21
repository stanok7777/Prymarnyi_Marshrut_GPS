package com.ghostroute.stealthnav.core

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.ImageButton
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
import com.ghostroute.stealthnav.engine.SensorFusionEngine

class MainActivity : AppCompatActivity() {
    private lateinit var map: MapView
    private lateinit var locationOverlay: MyLocationNewOverlay
    private val PERMISSION_REQUEST_CODE = 1
    private val sensorEngine = SensorFusionEngine()

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
        
        findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // Кнопка REC (Запис логів та треку)
        val btnRec = findViewById<Button>(R.id.btnRec)
        var isRecording = false
        btnRec.setOnClickListener {
            isRecording = !isRecording
            if (isRecording) {
                btnRec.setBackgroundColor(Color.parseColor("#FF5252"))
                btnRec.text = "⏹ STOP"
                Toast.makeText(this, "Почато запис логів маршруту", Toast.LENGTH_SHORT).show()
            } else {
                btnRec.setBackgroundColor(Color.parseColor("#4CAF50"))
                btnRec.text = "⏺ REC"
                Toast.makeText(this, "Трек збережено в аналітику", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка Anti-РЕБ (Перемикання на інерціальну навігацію)
        val stealthBtn = findViewById<Button>(R.id.stealthButton)
        val statusText = findViewById<TextView>(R.id.statusText)
        var isStealth = false
        stealthBtn.setOnClickListener {
            isStealth = !isStealth
            if (isStealth) {
                stealthBtn.text = "ВИМКНУТИ ANTI-РЕБ (LBS)"
                stealthBtn.setBackgroundColor(Color.parseColor("#444444"))
                statusText.text = "ANTI-РЕБ: Позиція за вежею зв'язку та акселерометром"
                statusText.setTextColor(Color.parseColor("#4CAF50"))
                sensorEngine.activateInertialMode()
                Toast.makeText(this, "Сенсори активовано. Довіру до GPS знижено.", Toast.LENGTH_LONG).show()
            } else {
                stealthBtn.text = "АКТИВУВАТИ ANTI-РЕБ ЗАХИСТ"
                stealthBtn.setBackgroundColor(Color.parseColor("#4CAF50"))
                statusText.text = "СИГНАЛ GPS: ВРАЗЛИВИЙ (МОЖЛИВИЙ РЕБ)"
                statusText.setTextColor(Color.parseColor("#FF5252"))
                sensorEngine.deactivateInertialMode()
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        } else {
            setupLocation()
        }
    }
    
    private fun setupLocation() {
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        map.overlays.add(locationOverlay)
    }
    
    override fun onResume() { super.onResume(); map.onResume() }
    override fun onPause() { super.onPause(); map.onPause() }
}