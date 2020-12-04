package id.refactory.androidmaterial.day25

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ydh.chatyok.ConstantUtil
import com.ydh.chatyok.LocalSession
import com.ydh.chatyok.R

class NotificationService : FirebaseMessagingService() {
    private val localSession by lazy { LocalSession(applicationContext) }
    private val db by lazy { Firebase.firestore }

    override fun onNewToken(token: String) {
        localSession.token = token

        db.collection(ConstantUtil.COLLECTION).document(localSession.uid)
            .update(mapOf("token" to token))
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(remoteMessage.data)
    }

    private fun showNotification(data: MutableMap<String, String>) {
        val title = data["title"] ?: ""
        val body = data["body"] ?: ""
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                ConstantUtil.NOTIFICATION_CHANNEL_ID,
                ConstantUtil.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, ConstantUtil.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setColor(getColor(R.color.teal_700))
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(ConstantUtil.NOTIFICATION_ID, notification)
    }
}