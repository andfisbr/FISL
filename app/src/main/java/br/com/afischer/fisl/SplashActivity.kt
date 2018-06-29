package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.models.AgendaRN
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.startActivity
import java.util.*

class SplashActivity: AppCompatActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_splash)
        
                
                
        
        
                val app = application as FISLApplication
        
        
        
                val cal = Calendar.getInstance()
                val dd = cal[Calendar.DAY_OF_MONTH]

                
                app.day = if (dd.pad("<00") !in arrayOf("11", "12", "13", "14"))
                        "11"
                else
                        dd.pad("<00")
        
                
                
                
        
                launch(UI) {
                        async(CommonPool) { AgendaRN(app).retrieve() }.await()
                        
                        
                        startActivity<MainActivity>()
                        finish()
                }
        
        }
}
