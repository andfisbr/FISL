package br.com.afischer.fisl.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import com.blankj.utilcode.util.AppUtils
import org.jetbrains.anko.browse





fun Context.hasPermission(permission: String): Boolean {
        val res = this.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
}

fun Context.hasPermissions(permissions: MutableList<String>): Boolean {
        var hasAllPermissions = true
        
        permissions.forEach {
                //you can return false instead of assigning, but by assigning you can log all permission values
                if (!hasPermission(it))
                        hasAllPermissions = false
        }
        
        return hasAllPermissions
}




fun Context.whatsapp(phone: String, message: String) {
        if (AppUtils.isAppInstalled("com.whatsapp")) {
                browse("https://api.whatsapp.com/send?phone=$phone&text=${message.encoded()}")
        
        } else {
                Toast.makeText(this, "WhatsApp não está instalado", Toast.LENGTH_SHORT).show()
        
                try {
                        browse("market://details?id=com.whatsapp")
                } catch (anfe: android.content.ActivityNotFoundException) {
                        browse("https://play.google.com/store/apps/details?id=com.whatsapp")
                }
        }
}