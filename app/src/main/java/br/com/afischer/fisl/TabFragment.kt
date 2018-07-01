package br.com.afischer.fisl


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import br.com.afischer.fisl.adapters.RoomsAdapter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.events.AgendaActivity_OnAgendaFilter
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.tlc
import br.com.afischer.fisl.extensions.tuc
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.TalkDetail
import br.com.afsystems.japassou.models.AlarmBase
import kotlinx.android.synthetic.main.dialog_schedule_detail.view.*
import kotlinx.android.synthetic.main.fragment_tab.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import java.util.*


class TabFragment: ParentFragment(), BaseView {
        private var param: String = "09:00"
        private var adapter: RoomsAdapter? = null
        
        
        

        companion object {
                private val ARG_PARAM1 = "hour"
                
                
                fun newInstance(param1: String): TabFragment {
                        val fragment = TabFragment()
                        val args = Bundle()
                        args.putString(ARG_PARAM1, param1)
                        fragment.arguments = args
                        return fragment
                }
        }



        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                arguments?.let {
                        param = it.getString(ARG_PARAM1)
                }
        }
        
        

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                rootView = inflater.inflate(R.layout.fragment_tab, container, false)
                return rootView
        }
        
        
        
        
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
        
                rooms_list.layoutManager = LinearLayoutManager(context)
                rooms_list.setHasFixedSize(true)
        
                
        
                /**
                 * filtra a agenda pelas horas
                 */
                val rows = app.aux.filter {
                        it.begins.trim() == "${app.date}T$param:00"
                }.sortedBy {
                        it.room
                }.toMutableList()

                
                

                adapter = RoomsAdapter(
                        activity = this.activity!!,
                        list = rows,
                        listener = { i -> showScheduleDetail(i) },
                        filterListener = { s, t -> filterListener(s, t) },
                        alarmListener = { i, s -> alarmListener(i, s) }
                )
                rooms_list.adapter = adapter
        }
        
        
        
        private fun filterListener(param: String, type: String) {
                app.filter = param.tlc()
        
        
                EventBus.getDefault().post(AgendaActivity_OnAgendaFilter(
                        when (type) {
                                "track" -> param.split(" - ")[1]
                                else -> param
                        }
                ))
        }
        
        
        private fun showScheduleDetail(i: Item) {
                //presenter.retrieveTalk(i.talk!!.id)
        }
        
        
        @SuppressLint("SetTextI18n")
        fun updateTalkDetail(talk: TalkDetail) {
                val view = inflate(context, R.layout.dialog_schedule_detail, null)
                
                view.dsd_title.text = talk.resource.title
                view.dsd_track.text = "<strong>Trilha</strong>:<br>${talk.resource.track}".asHtml()
                view.dsd_panelist.text = "<strong>Palestrante</strong>:<br>${talk.resource.owner!!.name.tuc()}<br>${talk.resource.owner!!.resume}".asHtml()
                view.dsd_local.text = "<strong>Apresentação</strong>:<br>Dia <strong>${talk.resource.slots!![0].begins.split("T")[0].split("-")[2]}</strong> às <strong>${talk.resource.slots!![0].hour}h</strong> na sala <strong>${talk.resource.slots!![0].roomName}</strong> (${talk.resource.slots!![0].duration})".asHtml()
                view.dsd_description.text = "<strong>Descrição</strong>:<br>${talk.resource.full}".asHtml()
                
        
                activity!!.alert {
                        customView = view
                        isCancelable = false
                        
                        positiveButton("Fechar") {
                                it.dismiss()
                        }
                }.show()
        }
        
        
        
        
        
        
        
        
        private fun alarmListener(i: Item, type: String) {
                /**
                 * configura o horário para o alarm
                 */
                val cal = Calendar.getInstance()
                val yy = i.begins.split("T")[0].split("-")[0]
                val mm = i.begins.split("T")[0].split("-")[1]
                val dd = i.begins.split("T")[0].split("-")[2]
                val hh = i.begins.split("T")[1].split(":")[0]
                val nn = i.begins.split("T")[1].split(":")[1]
                
                cal[Calendar.YEAR] = yy.toInt()
                cal[Calendar.MONTH] = mm.toInt() - 1
                cal[Calendar.DAY_OF_MONTH] = dd.toInt()
                cal[Calendar.HOUR_OF_DAY] = hh.toInt()
                cal[Calendar.MINUTE] = nn.toInt()
                cal[Calendar.SECOND] = 0
                
                
                
                /**
                 * acerta o dia do alarm
                 */
                var alertTime = cal.timeInMillis
                
                
                
                val alarm = AlarmBase().apply {
                        id = i.id
                        alarmID = i.alarmID
                        hour = cal.timeInMillis
                        title = i.talk?.title!!
                }
                
                
                
                
                
                
                if (type == "*") {
                        alarm.notificationCreate(this.activity)
        
        
                        app.alarms[alarm.id] = alarm
                        app.settings.alarms = app.alarms


                        toastyShow("s", "Notificação adicionada.\nVocê será avisado 15 minutos antes do início da viagem.")
                        

                        
                } else if (type == "o") {
                        alarm.notificationDelete(this.activity)
        
        
                        app.alarms.remove(alarm.id)
                        app.settings.alarms = app.alarms
                        toastyShow("i", "Notificação cancelada.")
                }
        }
        
}
