package br.com.afischer.fisl.app

import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDexApplication
import br.com.afischer.fisl.BuildConfig
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.models.Agenda
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.Settings
import br.com.afischer.fisl.util.Consts
import br.com.afsystems.japassou.models.AlarmBase
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import io.fabric.sdk.android.Fabric


class FISLApplication: MultiDexApplication() {
        lateinit var agenda: Agenda
        

        var alarms: MutableMap<Int, AlarmBase> = mutableMapOf()
        lateinit var prefs: SharedPreferences
        lateinit var settings: Settings
        
        
        
        
        
        
        override fun onCreate() {
                super.onCreate()
        
                Fabric.with(this, Crashlytics())
                
                agenda = Agenda(this)
        
        
                prefs = applicationContext.getSharedPreferences(Consts.PREFS_LOCAL, Context.MODE_PRIVATE)
        
                settings = Settings(prefs)
                settings.load()
        
        
                if (prefs.getInt(Consts.PREFS_VERSION, 0) < BuildConfig.VERSION_CODE) {
                        prefs.edit().clear().apply()
                        prefs.edit().putInt(Consts.PREFS_VERSION, BuildConfig.VERSION_CODE).apply()
                
                
                        alarms = settings.alarms
                        agenda.items = settings.agendaItems
                }
        }
        
        
        
        
        
        
        
        fun alarmsSchedule() {
                alarms.values.forEach {
                        it.notificationCreate(baseContext)
                }
        }
        
        fun alarmsUpdate() {
                settings.alarms = alarms
        }
        
        fun alarmsClear(onlyNotifications: Boolean = false) {
                alarms.values.forEach {
                        it.notificationDelete(baseContext)
                        
                        if (onlyNotifications)
                                return@forEach
                        
                        alarms.remove(it.id)
                        settings.alarms = alarms
                }
        }
        
        
        
        
        
        
        
        fun agendaSave() {
                prefs.edit().putString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(agenda.items)).apply()
        }
        fun agendaLoad() {
                val t1a = mutableListOf<Item>()
                agenda.items = Gson().fromJson(prefs.getString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(t1a)))
        }
        
}