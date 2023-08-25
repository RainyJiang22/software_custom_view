package com.rainy.customview.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.rainy.customview.R
import com.rainy.customview.base.BaseActivity
import com.rainy.customview.base.EmptyViewModel
import com.rainy.customview.databinding.ActivityTestBinding
import com.rainy.customview.hencoder.MainActivity

/**
 * @author jiangshiyu
 * @date 2022/11/16
 */
class TestActivity : BaseActivity<ActivityTestBinding, EmptyViewModel>() {

    companion object {
        const val TAG = "TestActivity"


        const val mHighNotificationId = 9002
        const val mStopAction = "com.widget.stop" // 暂停继续action
        const val mDoneAction = "com.widget.done" // 完成action
        private var mFlag = 0
        private var mIsStop = false // 是否在播放 默认未开始

    }


    private val mHighChannelId = "重要渠道id"
    private val mHighChannelName = "重要通知"
    private var mReceiver: NotificationReceiver? = null

    private lateinit var mManager: NotificationManager
    private lateinit var mBuilder: NotificationCompat.Builder


    override fun init(savedInstanceState: Bundle?) {
        mManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createReceiver()

        binding?.btnNotification?.setOnClickListener {
            createNotificationForMessage()
        }
    }


    /**
     * 发起重要通知
     */
    private fun createNotificationForMessage() {

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(mHighChannelId,
                mHighChannelName,
                NotificationManager.IMPORTANCE_HIGH)
            channel.setBypassDnd(true)
            channel.setShowBadge(true)
            mManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        val remoteView = RemoteViews(application.packageName, R.layout.layout_notification_message)

//        remoteView.setOnClickPendingIntent(R.id.notification_bg,
//            PendingIntent.getActivity(this, 0,
//                intent,
//                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//        )

        mBuilder = NotificationCompat.Builder(this, mHighChannelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setCategory(NotificationCompat.CATEGORY_REMINDER) // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
            .setCustomBigContentView(remoteView)
            .setCustomBigContentView(remoteView)
            .setCustomHeadsUpContentView(remoteView)
            .setAutoCancel(false)

        mManager.notify(mHighNotificationId, mBuilder.build())
    }


    /**
     * 创建广播接收器
     */
    private fun createReceiver() {
        val intentFilter = IntentFilter()
        // 添加接收事件监听
        intentFilter.addAction(mStopAction)
        intentFilter.addAction(mDoneAction)
        mReceiver = NotificationReceiver()
        // 注册广播
        registerReceiver(mReceiver, intentFilter)
    }

    private class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 拦截接收事件
            if (intent.action == mStopAction) {
                // 改变状态
                mIsStop = !mIsStop

            } else if (intent.action == mDoneAction) {
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

    override fun createObserver() {

    }


}