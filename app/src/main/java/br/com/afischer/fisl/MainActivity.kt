package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity





class MainActivity: AppCompatActivity() {
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
                
                
                main_calendar.bringToFront()
                main_calendar.setOnClickListener {
                        startActivity<AgendaActivity>()
                }
        }
}
