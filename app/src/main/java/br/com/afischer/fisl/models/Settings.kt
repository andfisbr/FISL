package br.com.afischer.fisl.models

import android.content.SharedPreferences
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.util.Consts
import br.com.afsystems.japassou.models.AlarmBase
import com.google.gson.Gson


class Settings(private var prefs: SharedPreferences) {
        var loading: Boolean = false
        
        var alarms: MutableMap<Int, AlarmBase> = mutableMapOf()
                set(value) {
                        field = value
                        if (!loading) prefs.edit().putString(Consts.PREFS_SETTINGS_ALARMS, Gson().toJson(field)).apply()
                }
        
        
        
        
        
        
        
        fun load() {
                loading = true
                
                val t3a = mutableMapOf<Int, AlarmBase>()
                alarms = Gson().fromJson(prefs.getString(Consts.PREFS_SETTINGS_ALARMS, Gson().toJson(t3a)))
                
                loading = false
        }
}
