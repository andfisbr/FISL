package br.com.afischer.fisl

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import br.com.afischer.fisl.app.FISLApplication


open class ParentFragment: Fragment() {
        lateinit var rootView: View
        lateinit var app: FISLApplication
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                
                app = activity!!.application as FISLApplication
        }
}
