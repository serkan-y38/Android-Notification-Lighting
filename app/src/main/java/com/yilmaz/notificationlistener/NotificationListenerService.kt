package com.yilmaz.notificationlistener

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.PowerManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if (!isScreenOn() && !sbn!!.isOngoing)
            lockScreenLightning(sbn.packageName)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    private fun isScreenOn(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        @Suppress("DEPRECATION")
        return powerManager.isScreenOn
    }

    private fun lockScreenLightning(packageName: String) {
        val intent = Intent(this, WakeupActivity::class.java).apply {
            putExtra(PACKAGE_NAME, packageName)
            addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

    companion object {
        const val PACKAGE_NAME = "PACKAGE_NAME"
    }

}