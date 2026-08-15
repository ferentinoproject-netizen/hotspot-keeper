package com.hotspot.keeper

import android.content.Context
import android.content.Intent
import java.util.concurrent.atomic.AtomicBoolean

object HotspotHelper {

    // Set to true right before we launch Settings, so the AccessibilityService
    // knows this window was opened by us and it's safe to act on the switch.
    val autoTriggerActive = AtomicBoolean(false)

    fun openHotspotSettings(context: Context) {
        autoTriggerActive.set(true)
        val intent = Intent()
        // Try the direct Wi-Fi tethering screen first (works on stock/AOSP Android 9-14)
        intent.action = "android.settings.WIFI_TETHER_SETTINGS"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback: general tether settings screen
            val fallback = Intent("android.settings.TETHER_SETTINGS")
            fallback.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            try {
                context.startActivity(fallback)
            } catch (e2: Exception) {
                autoTriggerActive.set(false)
            }
        }
    }
}
