package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import br.com.afischer.fisl.adapters.AlarmsAdapter
import br.com.afischer.fisl.models.AlarmBase
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_alarm.*





class AlarmActivity: ParentActivity() {
        private var adapter: AlarmsAdapter? = null
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_alarm)
        
        
                initViews()
                setupNotifications()
        }
        


        private fun initViews() {
                alarm_list.layoutManager = LinearLayoutManager(this)
                alarm_list.setHasFixedSize(true)
                
                alarm_refresh.setOnRefreshListener {
                        setupNotifications()
                }
        
        
                alarm_back_button.setOnClickListener {
                        finish()
                }
                alarm_toolbar_title.setOnClickListener {
                        finish()
                }
        }
        
        
        
        
        private fun setupNotifications() {
                updateAlarms()
        }
        
        
        
        
        
        private fun updateAlarms() {
                val aux = mutableListOf<AlarmBase>()
                if (app.alarm.items.isNotEmpty()) {
                        app.alarm.items.values.map { aux.add(it) }
                }
                
                adapter = AlarmsAdapter(
                        activity = this,
                        list = aux.sortedWith(compareBy({ it.hour }, { it.title })).toMutableList(),
                        listener = { a -> alarmDelete(a) }
                )
                
                
                alarm_list?.let {
                        it.adapter = adapter
                }
                
                alarm_refresh?.isRefreshing = false
        }
        
        
        
        
        
        private fun alarmDelete(alarm: AlarmBase) {
                try {
                        app.alarm.notificationDelete(alarm)
                        app.alarm.delete(alarm)
        
                        
        
                        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        
                        alerterShow("e", "Ops, houve um problema ao excluir a notificação.")
                }
                
                updateAlarms()
        }
        
}