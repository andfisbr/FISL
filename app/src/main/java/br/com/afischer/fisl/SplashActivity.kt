package br.com.afischer.fisl

import android.os.Bundle
import br.com.afischer.fisl.enums.ResultType
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread

class SplashActivity: ParentActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splash)
        
        
        
                
                
                
                
                splash_version.text = BuildConfig.VERSION_NAME
        
        
        

                
                
                
                
                
                //
                // reprograma os alarms
                //
                app.alarm.schedule()
        
        
                
                
        
                
                
                
                //
                // obtém a agenda completa da internet
                //
                doAsync {
                        val result = app.agenda.retrieve()
                        
                        
                        uiThread {
                                if (result.type == ResultType.SUCCESS) {
                                        app.agenda.doKeywords()
                                        app.agenda.doSave()
                                }
        
        
                                startActivity<MainActivity>()
                                finish()
                        }
                }
        }
}
