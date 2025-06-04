package xcom.niteshray.xapps.xblockit.util

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class BlockAccessibility : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        serviceInfo = info
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) {
            return
        }

        Log.d("BlockerService", "📱 Package: ${event.packageName}")

        val blockedshorts = getSharedPreferences("BlockedApps", Context.MODE_PRIVATE)
        val blockedApps = BlockAppsUtil.getBlockApps(this)
        val pkgName = event.packageName?.toString() ?: return
        Log.d("BlockedApp",blockedApps.toString())
        if (blockedApps.contains(pkgName)) {
            block()
            Log.d("BlockerService","Blocked App "+pkgName)
        }

        val rootNode = rootInActiveWindow ?: return

        val isBlocked = blockedshorts.getBoolean(pkgName, false)
        if (!isBlocked) return
        when (pkgName) {
            "com.google.android.youtube" -> {
                Log.d("BlockerService", "🎯 YouTube detected")
                if (isYouTubeShortsScreen(rootNode)) {
                    Log.d("BlockerService", "🎯 YouTube Shorts detected")
                    block()
                }
            }

            "com.instagram.android" -> {
                if (isInstagramScreen(rootNode)) {
                    block()
                }
            }

            "com.snapchat.android" -> {
                if (isSnapchatScreen(rootNode)) {
                    block()
                }
            }
        }
    }

    private fun isYouTubeShortsScreen(rootNode: AccessibilityNodeInfo): Boolean {
        val shorts = rootNode.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/reel_recycler")
        return shorts.isNotEmpty()
    }

    private fun isInstagramScreen(rootNode: AccessibilityNodeInfo): Boolean {
        val reels = rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/clips_viewer_view_pager")
        return reels.isNotEmpty()
    }

    private fun isSnapchatScreen(rootNode: AccessibilityNodeInfo): Boolean {
        val spotlight = rootNode.findAccessibilityNodeInfosByViewId("com.snapchat.android:id/spotlight_container")
        return spotlight.isNotEmpty()
    }

    private fun block() {
        performGlobalAction(GLOBAL_ACTION_BACK)
        Toast.makeText(this, "Feature Blocked", Toast.LENGTH_SHORT).show()
    }

    override fun onInterrupt() {
        Log.d("AddictionBlocker", "Service Interrupted ⚠️")
    }
}