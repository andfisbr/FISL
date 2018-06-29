package br.com.afischer.fisl.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import br.com.afischer.fisl.app.FISLApplication
import org.jetbrains.anko.doAsync


class AlarmScheduleService: IntentService("AlarmScheduleService") {

        override fun onHandleIntent(intent: Intent?) {
                val app = applicationContext as FISLApplication
        
        
                doAsync {
                        
                        /**
                         * apaga todos os alarms atuais
                         */
                        app.alarmsClear(true)
                        
                        
                        
                        /**
                         * recria os alarms
                         */
                        app.alarmsSchedule()
                        
                        
                        
                        /**
                         * atualiza os alarms no firebase
                         */
                        app.alarmsUpdate()
                        
                        
                        
        
                        
                        WakefulBroadcastReceiver.completeWakefulIntent(intent)
                }
        }
}