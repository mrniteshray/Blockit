package xcom.niteshray.xapps.xblockit.util

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class ShortsBlockAccessibility : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        serviceInfo = info
        Log.d("BlockerService", "Accessibility Service Connected ✅")
    }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("AddictionBlocker", "Accessibility Service Connected ✅")
        if (event == null) {
            Log.d("BlockerService", "⚠️ Null event")
            return
        }

        Log.d("BlockerService", "📱 Package: ${event.packageName}")

        val blockedApps = getSharedPreferences("BlockedApps", Context.MODE_PRIVATE)
        val pkgName = event.packageName?.toString() ?: return
        val isBlocked = blockedApps.getBoolean(pkgName, false)
        if (!isBlocked) return

        val rootNode = rootInActiveWindow ?: return

        when (pkgName) {
            "com.google.android.youtube" -> {
                Log.d("BlockerService", "🎯 YouTube detected")
                if (isYouTubeShortsScreen(rootNode)) {
                    Log.d("BlockerService", "🎯 YouTube Shorts detected")
                    blockShorts()
                }
            }

            "com.instagram.android" -> {
                if (isInstagramScreen(rootNode)) {
                    blockShorts()
                }
            }

            "com.snapchat.android" -> {
                if (isSnapchatScreen(rootNode)) {
                    blockShorts()
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

    private fun blockShorts() {
        performGlobalAction(GLOBAL_ACTION_BACK)
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(this, "Feature Blocked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInterrupt() {
        Log.d("AddictionBlocker", "Service Interrupted ⚠️")
    }
}