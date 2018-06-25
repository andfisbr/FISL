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
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.tuc
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.TalkDetail
import br.com.afischer.fisl.mvp.TalkPresenter
import kotlinx.android.synthetic.main.dialog_schedule_detail.view.*
import kotlinx.android.synthetic.main.fragment_hour.*
import org.jetbrains.anko.alert






class HourFragment: ParentFragment(), BaseView {
        private var hour: String = "09:00"
        private var presenter: TalkPresenter = TalkPresenter()
        
        

        companion object {
                private val ARG_PARAM1 = "hour"
                
                
                fun newInstance(param1: String): HourFragment {
                        val fragment = HourFragment()
                        val args = Bundle()
                        args.putString(ARG_PARAM1, param1)
                        fragment.arguments = args
                        return fragment
                }
        }



        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                arguments?.let {
                        hour = it.getString(ARG_PARAM1)
                }
        }
        
        

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                rootView = inflater.inflate(R.layout.fragment_hour, container, false)
                return rootView
        }
        
        
        
        
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
        
        
                rooms_list.layoutManager = LinearLayoutManager(context)
                rooms_list.setHasFixedSize(true)
                rooms_list.tag = hour
                



                val rooms = app.agenda.filter {
                        it.begins.trim() == "2018-07-${app.day}T$hour:00"
                }.sortedBy {
                        it.room
                }.toMutableList()
                
                

                
                val adapter = RoomsAdapter(rooms.distinctBy { it.room }.toMutableList()) { i -> showScheduleDetail(i) }
                rooms_list.adapter = adapter
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
        
}
