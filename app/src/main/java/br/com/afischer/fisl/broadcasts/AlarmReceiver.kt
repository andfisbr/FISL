package br.com.afischer.fisl.broadcasts

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver.startWakefulService
import br.com.afischer.fisl.services.AlarmNotificationService
import br.com.afischer.fisl.services.AlarmSoundService
import org.jetbrains.anko.vibrator


class AlarmReceiver: BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
                ctx?.vibrator?.vibrate(250)


                //
                // play sound
                //
                ctx?.startService(Intent(ctx, AlarmSoundService::class.java))


                //
                // This will send a notification message and show notification in notification tray
                //
                val comp = ComponentName(ctx?.packageName, AlarmNotificationService::class.java.name)
                startWakefulService(ctx, intent?.setComponent(comp))
        }
}