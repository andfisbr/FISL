package br.com.afischer.fisl.models

import android.content.SharedPreferences
import br.com.afischer.fisl.extensions.fromJson
import br.com.afischer.fisl.util.Consts
import com.google.gson.Gson


class Settings(private var prefs: SharedPreferences) {
        var loading: Boolean = false
        
        var alarmItems: MutableMap<Int, AlarmBase> = mutableMapOf()
                set(value) {
                        field = value
                        if (!loading) prefs.edit().putString(Consts.PREFS_SETTINGS_ALARMS, Gson().toJson(field)).apply()
                }
        
        
        
        var agendaItems: MutableList<Item> = mutableListOf()
                set(value) {
                        field = value
                        if (!loading) prefs.edit().putString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(field)).apply()
                }
        
        
        
        
        fun load() {
                loading = true
                
                val t1a = mutableListOf<Item>()
                agendaItems = Gson().fromJson(prefs.getString(Consts.PREFS_SETTINGS_AGENDA, Gson().toJson(t1a)))
                
                val t2a = mutableMapOf<Int, AlarmBase>()
                alarmItems = Gson().fromJson(prefs.getString(Consts.PREFS_SETTINGS_ALARMS, Gson().toJson(t2a)))
                
                loading = false
        }
        
}
