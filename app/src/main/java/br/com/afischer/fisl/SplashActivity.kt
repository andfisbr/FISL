package br.com.afischer.fisl

import android.os.Bundle
import br.com.afischer.fisl.extensions.pad
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.util.*

class SplashActivity: ParentActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splash)
        
        
        
        
                /**
                 * verifica qual é o dia atual para poder abrir a agenda corretamente
                 */
                val dd = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
                app.agenda.day = if (dd.pad("<00") !in arrayOf("11", "12", "13", "14"))
                        "11"
                else
                        dd.pad("<00")
        
                
                
                
                
        
                /**
                 * obtém a agenda completa da internet
                 */
                doAsync {
                        app.agenda.retrieve()
                        app.agenda.doKeywords()
                        app.agenda.doSave()
        

                        uiThread {
                                startActivity<MainActivity>()
                                finish()
                        }
                }
        }
}
