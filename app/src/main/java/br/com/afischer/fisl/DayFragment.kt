package br.com.afischer.fisl


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import br.com.afischer.fisl.adapters.Rooms2Adapter
import br.com.afischer.fisl.bases.BaseView
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.tuc
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.RoomBase
import br.com.afischer.fisl.models.TalkDetail
import br.com.afischer.fisl.mvp.TalkPresenter
import kotlinx.android.synthetic.main.dialog_schedule_detail.view.*
import kotlinx.android.synthetic.main.fragment_day.*
import org.jetbrains.anko.alert


class DayFragment: ParentFragment(), BaseView {
        private var day: Int = 11
        private var presenter: TalkPresenter = TalkPresenter()
        
        

        companion object {
                private val ARG_PARAM1 = "day"
                
                
                fun newInstance(param1: Int): DayFragment {
                        val fragment = DayFragment()
                        val args = Bundle()
                        args.putInt(ARG_PARAM1, param1)
                        fragment.arguments = args
                        return fragment
                }
        }



        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                arguments?.let {
                        day = it.getInt(ARG_PARAM1)
                }
        }
        
        

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                rootView = inflater.inflate(R.layout.fragment_day, container, false)
                return rootView
        }
        
        
        
        
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
        
                
                presenter.attachView(this)
                
                
                
        
                day_list.layoutManager = LinearLayoutManager(context)
                day_list.setHasFixedSize(true)
        
        
                val schedules = app.agenda.filter { it.begins.startsWith("2018-07-$day") }.toMutableList()
                
                val rooms = schedules.map {
                        RoomBase().apply {
                                id = it.room
                                name = it.roomName
                        }
                }.distinctBy { it.id }.sortedBy { it.id }.toMutableList()
                

                
                val adapter = Rooms2Adapter(rooms, schedules) { i -> showScheduleDetail(i) }
                day_list.adapter = adapter
        }
        
        
        
        private fun showScheduleDetail(i: Item) {
                presenter.retrieveTalk(i.talk!!.id)
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
        
}