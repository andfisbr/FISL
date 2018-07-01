package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.models.AgendaRN
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.util.*

class SplashActivity: AppCompatActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splash)
        
        
                val app = application as FISLApplication
        
        
        
        
        
        
                /**
                 * verifica qual é o dia atual para poder abrir a agenda corretamente
                 */
                val dd = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
                app.day = if (dd.pad("<00") !in arrayOf("11", "12", "13", "14"))
                        "11"
                else
                        dd.pad("<00")
        
                
                
                
                
        
                /**
                 * obtém a agenda completa da internet
                 */
                doAsync {
                        AgendaRN(app).retrieve()
                        AgendaRN(app).loadKeywords()
        

                        uiThread {
                                startActivity<MainActivity>()
                                finish()
                        }
                }
        }
}
