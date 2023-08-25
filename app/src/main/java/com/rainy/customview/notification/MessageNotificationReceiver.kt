package com.rainy.customview.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log

/**
 * @author jiangshiyu
 * @date 2022/11/23
 */
class MessageNotificationReceiver : BroadcastReceiver() {

    companion object {
        const val clickAction = "ClickAction"
    }


    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == clickAction) {
            Log.d("Notification", "onReceive:cancel")
            val manager = context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(NotificationActivity.mHighNotificationId)
        }
    }

}