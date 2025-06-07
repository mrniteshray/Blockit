package xcom.niteshray.xapps.xblockit.util

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class BlockAccessibility : AccessibilityService() {
    lateinit var notificationHelper: NotificationHelper

    override fun onServiceConnected() {
        super.onServiceConnected()
        notificationHelper = NotificationHelper(this)
        notificationHelper.createChannel()
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

        if (event.packageName == "xcom.niteshray.xapps.xblockit" || event.packageName == "com.android.systemui") {
            return
        }


        //App Blocking Functionality
        val blockedApps = BlockAppsUtil.getBlockApps(this)
        val pkgName = event.packageName?.toString() ?: return
        Log.d("BlockedApp",blockedApps.toString())
        if (blockedApps.contains(pkgName)) {
            block()
            Log.d("BlockerService", "Blocked App $pkgName")
        }

        val blockedUrls = BlockWesiteUtil.GetWebsite(this)
        val blockedDomains = blockedUrls.mapNotNull { getDomainFromUrl(it) }



        val rootNode = rootInActiveWindow ?: return

        //Website Blocking Functionality
        val urls = findUrlsFromScreen(rootNode)
        for (url in urls) {
            if (blockedDomains.contains(url)) {
                Log.d("BlockIt-Web", "Blocked URL detected: $url")
                performGlobalAction(GLOBAL_ACTION_BACK) 
            }
        }

        //Short Blocking Functionality
        val blockedshorts = getSharedPreferences("BlockedApps", Context.MODE_PRIVATE)
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
    fun findUrlsFromScreen(node: AccessibilityNodeInfo): List<String> {
        val urls = mutableListOf<String>()

        if (node.text != null && node.isClickable) {
            val text = node.text.toString()
            if (text.startsWith("http") || text.contains(".com")) {
                urls.add(text)
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                urls.addAll(findUrlsFromScreen(child))
            }
        }

        return urls
    }



    private fun block() {
        notificationHelper.showBlockedNotification()
        Toast.makeText(this@BlockAccessibility, "Feature Blocked", Toast.LENGTH_SHORT).show()
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onInterrupt() {
        Log.d("AddictionBlocker", "Service Interrupted ⚠️")
    }

    fun getDomainFromUrl(url: String): String? {
        return try {
            val uri = Uri.parse(url)
            uri.host?.replace("www.", "")
        } catch (e: Exception) {
            null
        }
    }

}