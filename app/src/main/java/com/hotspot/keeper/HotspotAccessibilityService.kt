package com.hotspot.keeper

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class HotspotAccessibilityService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var alreadyHandling = false

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (!HotspotHelper.autoTriggerActive.get()) return
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        if (event.packageName?.toString() != "com.android.settings") return
        if (alreadyHandling) return

        alreadyHandling = true
        // Give the settings screen a moment to fully render
        handler.postDelayed({
            try {
                val root = rootInActiveWindow
                if (root != null) {
                    val switchNode = findSwitch(root)
                    if (switchNode != null && !switchNode.isChecked) {
                        clickNode(switchNode)
                    }
                }
            } finally {
                // Return the user to whatever they were doing before
                handler.postDelayed({
                    performGlobalAction(GLOBAL_ACTION_BACK)
                    HotspotHelper.autoTriggerActive.set(false)
                    alreadyHandling = false
                }, 700)
            }
        }, 900)
    }

    private fun findSwitch(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (node.className == "android.widget.Switch" || node.className == "android.widget.ToggleButton") {
            return node
        }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val found = findSwitch(child)
            if (found != null) return found
        }
        return null
    }

    private fun clickNode(node: AccessibilityNodeInfo) {
        if (node.isClickable) {
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        } else {
            node.parent?.let { clickNode(it) }
        }
    }

    override fun onInterrupt() {}
}
