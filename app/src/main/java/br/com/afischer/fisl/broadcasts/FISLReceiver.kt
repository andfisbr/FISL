package br.com.afischer.fisl.broadcasts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.afischer.fisl.app.FISLApplication


class FISLReceiver: BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
                when (intent?.action) {
                        Intent.ACTION_BOOT_COMPLETED,
                        Intent.ACTION_MY_PACKAGE_REPLACED -> {
                                val app = ctx!!.applicationContext as FISLApplication
                                
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
                        }
                }
        }
}