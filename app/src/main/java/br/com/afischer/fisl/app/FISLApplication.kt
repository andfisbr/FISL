package br.com.afischer.fisl.app

import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDexApplication
import br.com.afischer.fisl.BuildConfig
import br.com.afischer.fisl.models.Agenda
import br.com.afischer.fisl.models.Alarm
import br.com.afischer.fisl.models.Settings
import br.com.afischer.fisl.models.Summary
import br.com.afischer.fisl.util.Consts
import com.blankj.utilcode.util.Utils
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class FISLApplication: MultiDexApplication() {
        lateinit var agenda: Agenda
        lateinit var alarm: Alarm
        lateinit var summary: Summary
        

        lateinit var prefs: SharedPreferences
        lateinit var settings: Settings
        
        
        
        
        
        
        override fun onCreate() {
                super.onCreate()
                Fabric.with(this, Crashlytics())
                Utils.init(this)
                
                

                prefs = applicationContext.getSharedPreferences(Consts.PREFS_LOCAL, Context.MODE_PRIVATE)

                
                
                
                agenda = Agenda(this)
                alarm = Alarm(this)
                summary = Summary()
                
                


                settings = Settings(prefs)
                settings.load()
        

                alarm.items = settings.alarmItems
                agenda.items.addAll(settings.agendaItems)
        


                //
                // se mudou de versão e alguma pref não existe mais
                // a lógica abaixo ajuda a recriar um prefs novo
                //
                if (prefs.getInt(Consts.PREFS_VERSION, 0) < BuildConfig.VERSION_CODE) {
                        prefs.edit().clear().apply()
                        prefs.edit().putInt(Consts.PREFS_VERSION, BuildConfig.VERSION_CODE).apply()
        
                        alarm.items.clear()
                        agenda.items.clear()
                        
                        alarm.items = settings.alarmItems
                        agenda.items.addAll(settings.agendaItems)
                }
        }
}