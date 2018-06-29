package br.com.afischer.fisl.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.afischer.fisl.app.FISLApplication
import org.jetbrains.anko.doAsync


class FISLReceiver: BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
                when (intent?.action) {
                        Intent.ACTION_BOOT_COMPLETED,
                        Intent.ACTION_MY_PACKAGE_REPLACED -> {
                                val app = ctx!!.applicationContext as FISLApplication
                                
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
                                }
                        }
                }
        }
}