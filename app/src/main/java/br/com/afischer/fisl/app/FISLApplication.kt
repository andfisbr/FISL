package br.com.afischer.fisl.app

import android.content.Context
import android.content.SharedPreferences
import android.support.multidex.MultiDexApplication
import br.com.afischer.fisl.BuildConfig
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.Keyword
import br.com.afischer.fisl.models.Settings
import br.com.afischer.fisl.models.TalkDetail
import br.com.afischer.fisl.util.Consts
import br.com.afsystems.japassou.models.AlarmBase
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class FISLApplication: MultiDexApplication() {
        var agenda: MutableList<Item> = mutableListOf()
        var aux: MutableList<Item> = mutableListOf()
        var tracks: MutableList<String> = mutableListOf()
        var talk: TalkDetail = TalkDetail()
        var keywords: MutableList<Keyword> = mutableListOf()
        var keyword = ""

        var alarms: MutableMap<Int, AlarmBase> = mutableMapOf()
        lateinit var prefs: SharedPreferences
        lateinit var settings: Settings
        
        
        var filter = ""
        var yearMonth = "2018-07-"
        var day: String = "11"
        var date: String = ""
                get() = "$yearMonth$day"
        
        
        
        
        
        
        override fun onCreate() {
                super.onCreate()
        
                Fabric.with(this, Crashlytics())
        
        
        
                prefs = applicationContext.getSharedPreferences(Consts.PREFS_LOCAL, Context.MODE_PRIVATE)
        
                settings = Settings(prefs)
                settings.load()
        
        
                if (prefs.getInt(Consts.PREFS_VERSION, 0) < BuildConfig.VERSION_CODE) {
                        prefs.edit().clear().apply()
                        prefs.edit().putInt(Consts.PREFS_VERSION, BuildConfig.VERSION_CODE).apply()
                
                
                        alarms = settings.alarms
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
}