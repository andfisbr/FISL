package br.com.afischer.fisl

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import br.com.afischer.fisl.adapters.NotificationsAdapter
import br.com.afsystems.japassou.models.AlarmBase
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_alarm.*





class AlarmActivity: ParentActivity() {
        private var adapter: NotificationsAdapter? = null
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_alarm)
        
        
                initViews()
                setupNotifications()
        }
        


        private fun initViews() {
                notif_list.layoutManager = LinearLayoutManager(this)
                notif_list.setHasFixedSize(true)
                
                notif_refresh.setOnRefreshListener {
                        setupNotifications()
                }
        
        
                notif_back_button.setOnClickListener {
                        finish()
                }
                notif_toolbar_title.setOnClickListener {
                        finish()
                }
        }
        
        
        
        
        private fun setupNotifications() {
                updateAlarms()
        }
        
        
        
        
        
        private fun updateAlarms() {
                val aux = mutableListOf<AlarmBase>()
                if (app.alarms.isNotEmpty())
                        app.alarms.values.map { aux.add(it) }
                
                adapter = NotificationsAdapter(
                        activity = this,
                        list = aux.sortedWith(compareBy<AlarmBase> { it.hour }.thenBy { it.title }).toMutableList(),
                        listener = { a -> alarmDelete(a) }
                )
                
                
                notif_list?.let {
                        it.adapter = adapter
                }
                
                notif_refresh?.isRefreshing = false
        }
        
        
        
        
        
        private fun alarmDelete(alarm: AlarmBase) {
                try {
                        alarm.notificationDelete(this)
                        app.alarms.remove(alarm.id)
                        app.settings.alarms = app.alarms
                        
                        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        
                        toastyShow("e", "Ops, houve um problema ao excluir a notificação.")
                }
                
                updateAlarms()
        }
        
}