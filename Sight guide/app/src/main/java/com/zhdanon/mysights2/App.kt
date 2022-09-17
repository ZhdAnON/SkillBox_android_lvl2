package com.zhdanon.mysights2

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotification()

        runBlocking(Dispatchers.IO) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                Log.d("registration_token", it.result)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotification() {
        val nameAssist = "Ассистент"
        val descriptionTextAssist = "Сообщение от гида"
        val importanceAssist = NotificationManager.IMPORTANCE_LOW

        val channelAssist =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, nameAssist, importanceAssist).apply {
                description = descriptionTextAssist
            }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channelAssist)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "sight_assist_channel"

        const val NOTIFICATION_ID = 1000
    }
}