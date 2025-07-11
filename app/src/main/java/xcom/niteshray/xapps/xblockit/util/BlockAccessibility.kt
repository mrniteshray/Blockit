package xcom.niteshray.xapps.xblockit.util

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import java.net.URL

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
        Log.d("Blockit-web","Screen url : ${urls.toString()}")
        Log.d("Blockit-web","Block url : ${blockedDomains.toString()}")

        for (url in urls) {
            if (blockedDomains.contains(url)) {
                Log.d("BlockIt-Web", "Blocked URL detected: $url")
                block()
            }
        }

        //Short Blocking Functionality
        val blockSharedPref = BlockSharedPref(this)
        if (blockSharedPref.getBlock() && blockSharedPref.getPauseEndTime() == 0L) {
            if (isYouTubeShortsScreen(rootNode) || isInstagramScreen(rootNode) || isSnapchatScreen(rootNode)) {
                Log.d("BlockerService", "Blocking is Working")
                block()
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
    private fun findUrlsFromScreen(node: AccessibilityNodeInfo): List<String> {
        val urls = mutableListOf<String>()

        if (node.text != null && node.isClickable) {
            val text = node.text.toString()
            if (text.startsWith("http") ||
                text.contains(".com") ||
                text.contains(".net") ||
                text.contains(".org") ||
                text.contains(".in") ||
                text.contains(".edu") ||
                text.contains(".gov") ||
                text.contains(".info") ||
                text.contains(".biz") ||
                text.contains(".io") ||
                text.contains(".co")
            ) {
                val cleaned = when {
                    text.contains(".com") -> text.substringBefore(".com") + ".com"
                    text.contains(".net") -> text.substringBefore(".net") + ".net"
                    text.contains(".org") -> text.substringBefore(".org") + ".org"
                    text.contains(".in") -> text.substringBefore(".in") + ".in"
                    text.contains(".edu") -> text.substringBefore(".edu") + ".edu"
                    text.contains(".gov") -> text.substringBefore(".gov") + ".gov"
                    text.contains(".info") -> text.substringBefore(".info") + ".info"
                    text.contains(".biz") -> text.substringBefore(".biz") + ".biz"
                    text.contains(".io") -> text.substringBefore(".io") + ".io"
                    text.contains(".co") -> text.substringBefore(".co") + ".co"
                    else -> text
                }
                val base = getBaseUrl(cleaned)
                if (base.isNotEmpty()) {
                    urls.add(base)
                }
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

    companion object {
        private var lastBlockTime = 0L
    }
    private fun block() {
        Toast.makeText(this@BlockAccessibility, "Feature Blocked", Toast.LENGTH_SHORT).show()
        if (System.currentTimeMillis() - lastBlockTime > 2000) {
            notificationHelper.showBlockedNotification()
            performGlobalAction(GLOBAL_ACTION_BACK)
            lastBlockTime = System.currentTimeMillis()
        }
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

    private fun getBaseUrl(url: String): String {
        return try {
            val parsed = URL(url)
            "${parsed.protocol}://${parsed.host}".let {
                if (parsed.port == -1) it else "$it:${parsed.port}"
            }
        } catch (e: Exception) {
            url
        }
    }
}