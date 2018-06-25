package br.com.afischer.fisl.app

import android.support.multidex.MultiDexApplication
import android.support.v7.widget.RecyclerView
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.TalkDetail
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric




class FISLApplication: MultiDexApplication() {
        var agenda: MutableList<Item> = mutableListOf()
        var talk: TalkDetail = TalkDetail()
        var day: String = "11"
        var touchedTag = ""
        var rvs = mutableListOf<RecyclerView>()
        var dx = 0
        var dy = 0
        var currentPage = -1
        var previousPage = -1
        
        
        
        override fun onCreate() {
                super.onCreate()
        
                Fabric.with(this, Crashlytics())
        }
}