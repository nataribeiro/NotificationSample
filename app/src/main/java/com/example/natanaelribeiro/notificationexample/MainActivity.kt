package com.example.natanaelribeiro.notificationexample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (intent.getBooleanExtra("CANCEL_NOTIFICATION", false)) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelId = CHANNEL_ID
            val channelName = "Alarme"

            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notification.setOnClickListener {
            Handler().postDelayed({

                val acceptIntent = Intent(this, MainActivity::class.java).apply {
                    putExtra("CANCEL_NOTIFICATION", true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val acceptPendingIntent: PendingIntent =
                    PendingIntent.getActivity(this, 0, acceptIntent, 0)

                var mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Teste")
                    .setContentText("Descrição da notificação teste")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setContentIntent(acceptPendingIntent)
                    .addAction(R.drawable.abc_ic_go_search_api_material, "Aceitar", acceptPendingIntent)
                    .setAutoCancel(true)

                var notification = mBuilder.build()
                notification.flags = Notification.FLAG_INSISTENT

                NotificationManagerCompat.from(this).apply {
                    notify(1, notification)
                }

            }, 10000)
        }
    }
}
