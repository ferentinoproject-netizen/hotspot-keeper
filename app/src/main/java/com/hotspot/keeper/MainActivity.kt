package com.hotspot.keeper

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnAccessibility).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            Toast.makeText(this, "Find 'Hotspot Keeper' in the list and turn it ON", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.btnBattery).setOnClickListener {
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Already allowed", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val serviceIntent = Intent(this, MonitorService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
            Toast.makeText(this, "Monitoring started. Hotspot will stay on.", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.btnTest).setOnClickListener {
            HotspotHelper.openHotspotSettings(this)
        }
    }
}
