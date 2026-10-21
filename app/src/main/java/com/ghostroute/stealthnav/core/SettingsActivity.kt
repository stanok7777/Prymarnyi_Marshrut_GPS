package com.ghostroute.stealthnav.core

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ghostroute.stealthnav.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<Button>(R.id.btnDownloadAll).setOnClickListener {
            Toast.makeText(this, "Завантаження бази OSM (36.2 МБ)...", Toast.LENGTH_LONG).show()
        }
        
        findViewById<Button>(R.id.btnUpdateCatalogs).setOnClickListener {
            Toast.makeText(this, "Оновлення графів доріг...", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Button>(R.id.btnLogDev).setOnClickListener {
            Toast.makeText(this, "Логи та аналітику поїздок сформовано для відправки", Toast.LENGTH_SHORT).show()
        }
    }
}