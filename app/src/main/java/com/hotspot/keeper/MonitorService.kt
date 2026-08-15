package com.hotspot.keeper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat

class MonitorService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private val CHANNEL_ID = "hotspot_keeper_channel"
    private val CHECK_INTERVAL_MS = 3 * 60 * 1000L // safety re-check every 3 minutes

    private val apStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // extra "wifi_state" == 11 means WIFI_AP_STATE_DISABLED (hidden constant, stable value)
            val state = intent.getIntExtra("wifi_state", -1)
            if (state == 11 || state == 10) { // DISABLED or DISABLING
                HotspotHelper.openHotspotSettings(applicationContext)
            }
        }
    }

    private val periodicCheck = object : Runnable {
        override fun run() {
            // Opens settings; the accessibility service turns it on only if it's off,
            // then backs out automatically. Harmless if it's already on.
            HotspotHelper.openHotspotSettings(applicationContext)
            handler.postDelayed(this, CHECK_INTERVAL_MS)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForeground(1, buildNotification())
        registerReceiver(apStateReceiver, IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED"))
        handler.postDelayed(periodicCheck, CHECK_INTERVAL_MS)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(apStateReceiver) } catch (e: Exception) {}
        handler.removeCallbacks(periodicCheck)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotification(): android.app.Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Hotspot Keeper", NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Hotspot Keeper running")
            .setContentText("Keeping your mobile hotspot always on")
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .setOngoing(true)
            .build()
    }
}
