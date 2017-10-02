package tr.xip.scd.tensuu.student.data.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tr.xip.scd.tensuu.student.App
import tr.xip.scd.tensuu.student.R
import tr.xip.scd.tensuu.student.ui.feature.main.StudentActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val notification = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_person_white_24dp)
                    .setColor(App.context.resources.getColor(R.color.accent_200))
                    .setContentTitle(it.title)
                    .setContentText(it.body)
                    .setContentIntent(PendingIntent.getActivity(
                            this,
                            0,
                            Intent(this, StudentActivity::class.java),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    ))
                    .build()
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).notify(1, notification)
        }
    }
}