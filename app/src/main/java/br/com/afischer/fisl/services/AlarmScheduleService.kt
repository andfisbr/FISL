package br.com.afischer.fisl.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import br.com.afischer.fisl.app.FISLApplication


class AlarmScheduleService: IntentService("AlarmScheduleService") {

        override fun onHandleIntent(intent: Intent?) {
                val app = applicationContext as FISLApplication
        
        
                //
                // apaga todos os alarms atuais
                //
                app.alarm.clear(true)
                
                
                
                //
                // recria os alarms
                //
                app.alarm.schedule()
                
                
                
                //
                // atualiza os alarms no firebase
                //
                app.alarm.update()
                
                
                

                
                WakefulBroadcastReceiver.completeWakefulIntent(intent)
        }
}