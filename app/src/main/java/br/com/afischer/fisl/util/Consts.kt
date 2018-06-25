package br.com.afischer.fisl.util


import java.util.*


object Consts {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val TIME_FORMAT = "HH:mm:ss"
        const val FULL_DATE_FORMAT = "\$DATE_FORMAT \$TIME_FORMAT"
        val ptBR = Locale("pt", "BR")
        
        
        
        
        
        const val PREFS_LOCAL = "br.com.afischer.fisl.prefs"
        const val PREFS_VERSION = "version"
        

        
        const val PREFS_SETTINGS_ADS_SHOW_AFTER = "settings.adsShowAfter"
        const val PREFS_SETTINGS_ADS_TIME_BETWEEN = "settings.adsTimeBetween"
        const val PREFS_SETTINGS_DISMISSED_MESSAGES = "settings.dismissedMessages"
        const val PREFS_SETTINGS_FAVORITIES = "settings.favorities"
        const val PREFS_SETTINGS_SHORTCUTS = "settings.shortcuts"
        const val PREFS_SETTINGS_LAYOUT = "settings.layout"
        const val PREFS_SETTINGS_ACCEPT_TERMS = "settings.acceptTerms"
        const val PREFS_SETTINGS_ACCEPT_PRIVACY = "settings.acceptPrivacy"
        const val PREFS_SETTINGS_INTRO_SHOWN = "settings.introShown"
        const val PREFS_SETTINGS_ALREADY_RATED = "settings.alreadyRated"
        const val PREFS_SETTINGS_RATE_TIMEOUT = "settings.rateTimeout"
        const val PREFS_SETTINGS_USER_TYPE = "settings.userType"
        const val PREFS_SETTINGS_USER_COMPANY = "settings.userCompany"
        const val PREFS_SETTINGS_USED_TIMES = "settings.userTimes"
        const val PREFS_SETTINGS_DONATE_DAYS = "settings.donateDays"
        const val PREFS_SETTINGS_CACHE = "settings.cache"
        const val PREFS_SETTINGS_ALARMS = "settings.alarms"
        
        
        const val ACTION_MAIN = "br.com.afischer.fisl.action.main"
        const val ACTION_STOP = "br.com.afischer.fisl.action.stop"
        const val ACTION_START = "br.com.afischer.fisl.action.start"
        const val ACTION_FIND = "br.com.afischer.fisl.action.find"
        const val ACTION_FIND_ALL = "br.com.afischer.fisl.action.findall"
        const val ACTION_SHARE = "br.com.afischer.fisl.action.share"
        const val ACTION_ALARM = "br.com.afischer.fisl.action.alarm"
        const val ACTION_ALARM_TRIGGERED = "br.com.afischer.fisl.action.alarmtriggered"
}
