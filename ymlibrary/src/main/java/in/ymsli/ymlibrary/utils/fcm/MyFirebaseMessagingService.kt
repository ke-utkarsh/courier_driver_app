/**
 * Project Name :YmLibrary
 * @company YMSLI
 * @author  Rubi Sharma
 * @date   July 27, 2018
 * Copyright (c) 2018, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * MyFirebaseMessagingService Class : responsible for handling message receive event showing
 * notification
 * -----------------------------------------------------------------------------------

 * Revision History

 * -----------------------------------------------------------------------------------

 * Modified By          Modified On         Description
-----------------------------------------------------------------------------------

 */


package `in`.ymsli.ymlibrary.utils.fcm



import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



open class MyFirebaseMessagingService : FirebaseMessagingService() {

    var title: String? = ""
    var body: String? = ""
    var intent: Intent?= null
    var notificationId: Int = 0
    var icon:Int?=0;

    override fun onMessageReceived(message: RemoteMessage?) {
        try {
            if (message != null && message.notification != null) {
                title = message.notification!!.title
                body = message.notification!!.body
            }
            sendMyNotification(body)
        } catch (e: Exception) {

        }

    }


     var target: Class<*>?=null


     fun sendMyNotification(message: String?) {

        //On click of notification it redirect to this Activity

        intent = Intent(applicationContext, target!!)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(icon!!)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId++, notificationBuilder.build())
        val v = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(1000)
    }


}