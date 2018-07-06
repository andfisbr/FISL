package br.com.afischer.fisl.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.broadcasts.AlarmReceiver
import br.com.afischer.fisl.extensions.min
import br.com.afischer.fisl.extensions.toJson
import org.jetbrains.anko.alarmManager

class Alarm(var app: FISLApplication) {
        var items: MutableMap<Int, AlarmBase> = mutableMapOf()
        
        
        
        
        
        
        
        
        
        
        fun notificationCreate(alarm: AlarmBase) {
                //
                // cria os intents
                //
                val alarmIntent = Intent(app.baseContext, AlarmReceiver::class.java)
                alarmIntent.putExtra("alarm", alarm.toJson())
                
                
                
                
                //
                // aciona os métodos corretos
                //
                val pendingIntent = PendingIntent.getBroadcast(app.baseContext, alarm.alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                
                when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> app.baseContext.alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.hour - 15.min(), pendingIntent)
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> app.baseContext.alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.hour - 15.min(), pendingIntent)
                        else -> app.baseContext.alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.hour - 15.min(), pendingIntent)
                }
        }
        
        
        
        fun notificationDelete(alarm: AlarmBase) {
                //
                // cria os intents
                //
                val alarmIntent = Intent(app.baseContext, AlarmReceiver::class.java)
                val pending = PendingIntent.getBroadcast(app.baseContext, alarm.alarmID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                
                
                //
                // cancela alarms
                //
                app.baseContext.alarmManager.cancel(pending)
                pending.cancel()
        }
        
        
        
        
        
        
        
        fun schedule() {
                items.values.forEach {
                        notificationCreate(it)
                }
        }
        
        fun update() {
                app.settings.alarmItems = items
        }
        
        fun clear(onlyNotifications: Boolean = false) {
                items.values.forEach {
                        notificationDelete(it)
                        
                        if (onlyNotifications)
                                return@forEach
                        
                        items.remove(it.id)
                        app.settings.alarmItems = items
                }
        }
        
        
        
        
        
        fun add(alarm: AlarmBase) {
                items[alarm.id] = alarm
        }
        
        fun delete(alarm: AlarmBase) {
                items.remove(alarm.id)
        }
        
        fun save() {
                app.settings.alarmItems = items
        }
        
        fun contains(id: Int) = items.containsKey(id)
        
        fun get(id: Int) = items[id]!!
}