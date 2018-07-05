package br.com.afischer.fisl.util


import java.util.*


object Consts {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val TIME_FORMAT = "HH:mm:ss"
        const val FULL_DATE_FORMAT = "\$DATE_FORMAT \$TIME_FORMAT"
        val ptBR = Locale("pt", "BR")
        
        
        
        
        
        const val PREFS_LOCAL = "br.com.afischer.fisl.prefs"
        const val PREFS_VERSION = "version"
        

        
        const val PREFS_SETTINGS_DISMISSED_MESSAGES = "settings.dismissedMessages"
        const val PREFS_SETTINGS_FAVORITIES = "settings.favorities"
        const val PREFS_SETTINGS_ALARMS = "settings.alarms"
        const val PREFS_SETTINGS_AGENDA = "settings.agenda"
        
        
        const val ACTION_MAIN = "br.com.afischer.fisl.action.main"
        const val ACTION_STOP = "br.com.afischer.fisl.action.stop"
        const val ACTION_START = "br.com.afischer.fisl.action.start"
        const val ACTION_FIND = "br.com.afischer.fisl.action.find"
        const val ACTION_FIND_ALL = "br.com.afischer.fisl.action.findall"
        const val ACTION_SHARE = "br.com.afischer.fisl.action.share"
        const val ACTION_ALARM = "br.com.afischer.fisl.action.alarm"
        const val ACTION_ALARM_TRIGGERED = "br.com.afischer.fisl.action.alarmtriggered"
}
