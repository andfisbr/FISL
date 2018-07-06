package br.com.afischer.fisl


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import br.com.afischer.fisl.adapters.RoomsAdapter
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.events.AgendaActivity_OnAgendaFilter
import br.com.afischer.fisl.events.AgendaActivity_ProgressHide
import br.com.afischer.fisl.events.AgendaActivity_ProgressShow
import br.com.afischer.fisl.events.AgendaActivity_ShowToast
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.models.AlarmBase
import br.com.afischer.fisl.models.Item
import com.crashlytics.android.Crashlytics
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.dialog_talk_detail.view.*
import kotlinx.android.synthetic.main.fragment_tab.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class TabFragment: ParentFragment() {
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
        
                //
                //
                //
                rooms_list.layoutManager = LinearLayoutManager(context)
                rooms_list.setHasFixedSize(true)
        
                
                
        
                //
                // filtra a agenda pelas horas
                //
                val rows = app.agenda.aux.filter {
                        it.begins.trim() == "${app.agenda.date}T$param:00"
                }.sortedBy {
                        it.room
                }.toMutableList()

                
                

                adapter = RoomsAdapter(
                        activity = this.activity!!,
                        list = rows,
                        listener = { i -> detailListener(i) },
                        filterListener = { s, t -> filterListener(s, t) },
                        alarmListener = { i, s -> alarmListener(i, s) }
                )
                rooms_list.adapter = adapter
        }
        
        
        
        private fun filterListener(param: String, type: String) {
                app.agenda.filter = when (type) {
                        "track" -> param.toLowerCase().split(" - ")[1]
                        else -> param.toLowerCase()
                }
        
        
                EventBus.getDefault().post(AgendaActivity_OnAgendaFilter(app.agenda.filter))
        }
        
        
        private fun detailListener(i: Item) {
                EventBus.getDefault().post(AgendaActivity_ProgressShow("Aguarde"))
                doAsync {
                        val result = app.agenda.retrieveTalk(i.talk?.id)
                        
                        
                        uiThread {
                                EventBus.getDefault().post(AgendaActivity_ProgressHide())
                                
                                if (result.type != ResultType.SUCCESS) {
                                        EventBus.getDefault().post(AgendaActivity_ShowToast("w", "Houve um problema ao obter os detalhes, tente mais tarde."))
                                        return@uiThread
                                }
                                
                                updateDetails()
                        }
                }
        }
        
        

        private fun updateDetails() {
                try {
                        val v = inflate(context, R.layout.dialog_talk_detail, null)
                        v.dsd_title.text = app.agenda.talk.resource.title
                        v.dsd_track.text = app.agenda.talk.resource.track.asHtml()
                        v.dsd_owner.text = "${app.agenda.talk.resource.owner.name.toUpperCase()}<br>${app.agenda.talk.resource.owner.resume}".asHtml()
        
        
                        val co = mutableListOf<String>()
                        app.agenda.talk.resource.coauthors.forEach {
                                co.add(it.name.toUpperCase())
                                co.add(it.resume)
                        }
                        v.dsd_coauthor.text = co.joinToString("<br>").asHtml()
                        if (app.agenda.talk.resource.coauthors.isNotEmpty()) v.dsd_coauthor.show()


                        v.dsd_local.text = "Dia <strong>${app.agenda.talk.resource.slots[0].begins.split("T")[0].split("-")[2]}</strong> às <strong>${app.agenda.talk.resource.slots[0].hour}h</strong> na sala <strong>${app.agenda.talk.resource.slots[0].roomName}</strong> (${app.agenda.talk.resource.slots[0].duration}min)".asHtml()
                        v.dsd_description?.text = app.agenda.talk.resource.full.asHtml()
        
                        
                        activity!!.alert {
                                customView = v
                                isCancelable = false
                
                                positiveButton("Fechar") {
                                        it.dismiss()
                                }
                        }.show()
        
        
        
                } catch (ex: Exception) {
                        Crashlytics.logException(ex)
                        
                        //EventBus.getDefault().post(AgendaActivity_ShowToast("e", "Houve um problema ao obter os detalhes, tente mais tarde."))
                }
        }
        
        
        
        
        
        
        
        
        private fun alarmListener(i: Item, type: String) {
                //
                // configura o horário para o alarm
                //
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
                
                
                

                val alarm = AlarmBase().apply {
                        id = i.id
                        alarmID = i.alarmID
                        hour = cal.timeInMillis
                        title = i.talk?.title!!
                        owner = i.talk?.owner!!
                        track = i.talk?.track!!
                        room = i.roomName
                }
                
                
                
                
                
                
                if (type == "*") {
                        app.alarm.add(alarm)
                        app.alarm.notificationCreate(alarm)
        
        
                        EventBus.getDefault().post(AgendaActivity_ShowToast("s", "Notificação adicionada.\n\nVocê será avisado 15 minutos antes do início da palestra."))
                        

                        
                } else if (type == "o") {
                        app.alarm.delete(alarm)
                        app.alarm.notificationDelete(alarm)
        
        
                        EventBus.getDefault().post(AgendaActivity_ShowToast("i", "Notificação cancelada."))
                }
        }
}
