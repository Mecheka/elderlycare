package besmart.elderlycare

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.graphics.Color
import android.os.Build



@SuppressLint("Registered")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remote: RemoteMessage) {
        super.onMessageReceived(remote)
        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(remote.notification?.title)
            .setContentText(remote.notification?.body)
            .setAutoCancel(true)
            .setContentInfo(remote.notification?.title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
