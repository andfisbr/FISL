package br.com.afischer.fisl.services

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.content.WakefulBroadcastReceiver
import android.widget.RemoteViews
import br.com.afischer.fisl.MainActivity
import br.com.afischer.fisl.R
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.models.AlarmBase
import br.com.afischer.fisl.util.Consts
import com.google.gson.Gson
import org.jetbrains.anko.notificationManager
import java.util.*


class AlarmNotificationService: IntentService("AlarmNotificationService") {
        private var alarmNotificationManager: NotificationManager? = null
        
        //Notification ID for Alarm
        val NOTIFICATION_ID = 1
        
        
        
        override fun onHandleIntent(intent: Intent?) {
                //
                // send notification
                //
                val alarm = Gson().fromJson<AlarmBase>(intent!!.getStringExtra("alarm"))
        
                val cal = Calendar.getInstance()
                cal.timeInMillis = alarm.hour
        
                val yyyy = cal[Calendar.YEAR]
                val MM = cal[Calendar.MONTH] + 1
                val dd = cal[Calendar.DAY_OF_MONTH]
                val hh = cal[Calendar.HOUR_OF_DAY]
                val mm = cal[Calendar.MINUTE]
        
        
        
        
        
                alarmNotificationManager = this.notificationManager
        
        
        
                //
                // preenche campos da notificação customizada
                //
                val contentCollapsed = RemoteViews(this.packageName, R.layout.notification_alarm_collapsed)
                contentCollapsed.setTextViewText(R.id.na_col_hour, "${hh.pad("<00")}:${mm.pad("<00")}")
                contentCollapsed.setTextViewText(R.id.na_col_date, "${dd.pad("<00")}/${MM.pad("<00")}/$yyyy")
                contentCollapsed.setTextViewText(R.id.na_col_room, alarm.room)
                contentCollapsed.setTextViewText(R.id.na_col_title, alarm.title)
        
        
        
                val contentExpanded = RemoteViews(this.packageName, R.layout.notification_alarm_expanded)
                contentExpanded.setTextViewText(R.id.na_exp_hour, "${hh.pad("<00")}:${mm.pad("<00")}")
                contentExpanded.setTextViewText(R.id.na_exp_date, "${dd.pad("<00")}/${MM.pad("<00")}/$yyyy")
                contentExpanded.setTextViewText(R.id.na_exp_room, alarm.room)
                contentExpanded.setTextViewText(R.id.na_exp_title, alarm.title)
                contentExpanded.setTextViewText(R.id.na_exp_owner, alarm.owner)
                contentExpanded.setTextViewText(R.id.na_exp_track, if (alarm.track.contains("-")) alarm.track.split(" - ")[1] else alarm.track)
        
                
                
                
                
                
                //
                // get pending intent
                //
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.action = Consts.ACTION_ALARM
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                
                
                val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                
                
                //
                // create notification
                //
                val alamNotificationBuilder = NotificationCompat.Builder(this)
                        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setCustomContentView(contentCollapsed)
                        .setCustomBigContentView(contentExpanded)
                        .setAutoCancel(true)
                alamNotificationBuilder.setContentIntent(pendingIntent)
                
                
                //
                // notiy notification manager about new notification
                //
                alarmNotificationManager!!.notify(System.currentTimeMillis().toInt(), alamNotificationBuilder.build())
        
        
        
        
                WakefulBroadcastReceiver.completeWakefulIntent(intent)
                
                
                
                
                //
                // remove o alarm após o disparo
                //
                (application as FISLApplication).alarm.notificationDelete(alarm)
                (application as FISLApplication).alarm.delete(alarm)
        }
}