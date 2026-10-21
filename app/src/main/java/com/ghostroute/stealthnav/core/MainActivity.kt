package com.ghostroute.stealthnav.core

import android.os.Bundle
import android.app.Activity
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import com.ghostroute.stealthnav.R

class MainActivity : Activity() {
    private lateinit var map: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Налаштування для офлайн-карт OSMDroid
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = ctx.packageName
        
        setContentView(R.layout.activity_main)
        
        // Ініціалізація карти
        map = findViewById(R.id.mapView)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        
        // Центрування карти (Київ за замовчуванням)
        val mapController = map.controller
        mapController.setZoom(12.0)
        val startPoint = org.osmdroid.util.GeoPoint(50.4501, 30.5234)
        mapController.setCenter(startPoint)
        
        // Кнопка VIP та текст статусу
        val vipButton = findViewById<Button>(R.id.vipButton)
        val statusText = findViewById<TextView>(R.id.statusText)
        
        vipButton.setOnClickListener {
            Toast.makeText(this, "Тестовий режим: Stealth Mode активовано!", Toast.LENGTH_LONG).show()
            vipButton.text = "STEALTH MODE АКТИВНО"
            vipButton.setBackgroundColor(Color.parseColor("#333333"))
            
            statusText.text = "Сигнал GPS: ІГНОРУЄТЬСЯ (Інерціальна навігація)"
            statusText.setTextColor(Color.parseColor("#4CAF50"))
        }
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