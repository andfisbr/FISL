package br.com.afsystems.japassou.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import br.com.afischer.fisl.broadcasts.AlarmReceiver
import br.com.afischer.fisl.extensions.min
import br.com.afischer.fisl.extensions.toJson
import org.jetbrains.anko.alarmManager

class AlarmBase {
        var id: Int = -1
        var alarmID: Int = -1
        var hour: Long = 0L
        var title: String = ""
        var owner: String = ""
        var track: String = ""
        var room: String = ""
        
 
        
        
        
        
        
        fun notificationCreate(ctx: Context?) {
                //
                // cria os intents
                //
                val alarmIntent = Intent(ctx!!, AlarmReceiver::class.java)
                alarmIntent.putExtra("alarm", this.toJson())
                
                
                
                
                //
                // aciona os métodos corretos
                //
                val pendingIntent = PendingIntent.getBroadcast(ctx, alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        
                when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> ctx.alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, hour - 15.min(), pendingIntent)
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> ctx.alarmManager.setExact(AlarmManager.RTC_WAKEUP, hour - 15.min(), pendingIntent)
                        else -> ctx.alarmManager.set(AlarmManager.RTC_WAKEUP, hour - 15.min(), pendingIntent)
                }
        }
        
        
        
        fun notificationDelete(ctx: Context?) {
                //
                // cria os intents
                //
                val alarmIntent = Intent(ctx!!, AlarmReceiver::class.java)
                val pending = PendingIntent.getBroadcast(ctx, alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        
        
                //
                // cancela alarms
                //
                ctx.alarmManager.cancel(pending)
                pending.cancel()
        }
}
