package br.com.afischer.fisl.adapters

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.asDate
import br.com.afischer.fisl.extensions.inflate
import br.com.afischer.fisl.extensions.random
import br.com.afischer.fisl.models.Item
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import khronos.Dates
import kotlinx.android.synthetic.main.room_row.view.*
import org.jetbrains.anko.browse


class RoomsAdapter(
        var activity: Activity,
        private var list: MutableList<Item>,
        var listener: (i: Item) -> Unit,
        var filterListener: (s: String) -> Unit,
        var alarmListener: (i: Item, s: String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
        
        var app = activity.application as FISLApplication
        
        
        companion object {
                private const val TYPE_HEADER = 0
                private const val TYPE_ITEM = 1
                private const val TYPE_FOOTER = 2
        }
        
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
                TYPE_HEADER -> {
                        val itemView = parent.inflate(R.layout.room_header)
                        HeaderViewHolder(itemView)
                }
                
                TYPE_ITEM -> {
                        val itemView = parent.inflate(R.layout.room_row)
                        ItemViewHolder(itemView)
                }
                
                
                TYPE_FOOTER -> {
                        val itemView = parent.inflate(R.layout.room_footer)
                        FooterViewHolder(itemView)
                }
                
                else -> throw RuntimeException("There is no type that matches the type $viewType make sure your using types correctly")
        }
        
        
        
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                when(holder) {
                        is HeaderViewHolder -> holder.bind()
                        is ItemViewHolder -> holder.bind(list[position - 1], position - 1)
                        is FooterViewHolder -> holder.bind()
                }
        }
        
        //added a method that returns viewType for a given position
        override fun getItemViewType(position: Int): Int = when {
                isPositionHeader(position) -> TYPE_HEADER
                isPositionFooter(position) -> TYPE_FOOTER
                else -> TYPE_ITEM
        }
        
        //our old getItemCount()
        private val basicItemCount: Int
                get() = list.size
        
        
        
        //our new getItemCount() that includes header+footer View
        override fun getItemCount(): Int = basicItemCount + 1 + 1 // with header + footer
        
        
        //added a method to check if given position is a header
        private fun isPositionHeader(position: Int): Boolean = position == 0
        private fun isPositionFooter(position: Int): Boolean = position == list.size + 1
        
        


        inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind(item: Item, position: Int) = with(itemView) {
                        roomrow_owner.hide()
                        roomrow_track.hide()
                        roomrow_notifyme.hide()
                        roomrow_recordings_list.hide()
                        roomrow_recordings_warning.hide(false)
                        roomrow_container.setOnClickListener {}
        
                        
                        roomrow_room.text = item.roomName //.replace(" ", "\n")
                        roomrow_room.contentDescription = "Clique-me para filtrar pela sala ${item.roomName.toLowerCase().replace("sala ", "")}"
                        roomrow_room.setOnClickListener { filterListener(item.roomName) }


                        roomrow_duration.text = "${item.duration}min"
        
        
        
                        when {
                                item.status == "empty" -> {
                                        roomrow_title.text = "Sem palestra"
                                        roomrow_title.contentDescription = "Sem palestra"
                                        roomrow_container.setBackgroundResource(R.color.blue_50)
                                        return@with
        
                                }
                                item.status == "dirty" -> roomrow_container.setBackgroundResource(R.color.red_50)
                                else -> roomrow_container.setBackgroundResource(R.color.white)
                        }
        
        
                        


                        /**
                         * insere o link das gravações para o download
                         */
                        roomrow_recordings_list.show()
                        if (item.recordings.isNotEmpty()) {
                                roomrow_recordings_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                roomrow_recordings_list.setHasFixedSize(true)
                                val adapter = RecordingsAdapter(activity, item.recordings) { s ->
                                        //if (!NetworkUtils.isWifiConnected()) {
                                        //        activity.alert {
                                        //                isCancelable = false
                                        //                message = "Você não está conectado através de um WiFi. Dependendo do vídeo, o download pode consumir muito de sua banda 3/4G.\n\nContinuar download através da rede atual?"
                                        //
                                        //                positiveButton("Sim") { activity.browse(s) }
                                        //                negativeButton("Não") { it.dismiss() }
                                        //        }.show()
                                        //
                                        //} else {
                                                activity.browse(s)
                                        //}
                                }
                                roomrow_recordings_list.adapter = adapter
                
                        } else {
                                roomrow_recordings_warning.show()
                                roomrow_recordings_list.hide()
                        }
        
        


                        
        
        
                        item.talk?.let { t ->
                                roomrow_owner.show()
                                roomrow_track.show()
                                roomrow_notifyme.show()

                                roomrow_title.alpha = 1.0f
                                roomrow_owner.alpha = 1.0f
                                roomrow_track.alpha = 1.0f
        
        
                                val track = if (t.track.contains("-")) t.track.split(" - ")[1] else t.track
        
                                roomrow_title.text = t.title
                                roomrow_owner.text = t.owner
                                roomrow_track.text = track

                                roomrow_owner.contentDescription = "Clique-me para filtrar pelo palestrante ${t.owner}"
                                roomrow_track.contentDescription = "Clique-me para filtrar pela trilha $track"
        
                                roomrow_container.setOnClickListener { listener(item) }
                                roomrow_owner.setOnClickListener { filterListener(t.owner) }
                                roomrow_track.setOnClickListener { filterListener(track) }
        
        

                                
        
                                //
                                // informações para acessibilidade
                                //
                                val recordings = when {
                                        item.recordings.isEmpty() -> "sem gravações"
                                        item.recordings.size == 1 -> "uma gravação"
                                        else -> "${item.recordings.size} gravações"
                                }
                                roomrow_container.contentDescription = """
                                        sala: ${item.roomName.toLowerCase().replace("sala ", "")}
                                        duração: ${item.duration}min
                                        título: ${t.title.toLowerCase()}
                                        palestrante: ${t.owner.toLowerCase()}
                                        trilha: $track
                                        $recordings
                                """

                                
                                
                                
                                
                                //
                                // muda a cor do fundo baseado no status da palestra
                                //
                                if (t.status != "confirmed") {
                                        roomrow_container.setBackgroundResource(R.color.red_50)
                                }
        
        
        
                                //
                                // muda a cor do fundo quando for continuação
                                //
                                if (item.continuation) {
                                        roomrow_notifyme.hide()
                                        roomrow_duration.hide()
                                        roomrow_recordings_list.hide()
                                        roomrow_recordings_warning.hide()
        
                                        roomrow_title.alpha = 0.5f
                                        roomrow_owner.alpha = 0.5f
                                        roomrow_track.alpha = 0.5f
                                        
                                        roomrow_title.text = "CONTINUAÇÃO .:\n${t.title}"
                                        roomrow_container.setBackgroundResource(R.color.green_50)
        
                                        roomrow_container.contentDescription = "Continuação da palestra ${t.title.toLowerCase()}"
                                }
        
        
        
        
        
                                //
                                // não mostra mais o botão para adicionar notificação se já passou do horário da palestra
                                //
                                val date = item.begins.replace("T", " ").asDate("yyyy-MM-dd HH:mm:ss").time
                                if (date < Dates.today.time) {
                                        roomrow_notifyme.hide()
                                }
        
        
        
                                

                                
                
                                
        
                                
                                
        
        
        
        
                                //
                                // altera o ícone conforme o item tenha ou não alarm
                                //
                                roomrow_notifyme.setImageResource(R.drawable.ic_notification_outline_black_24dp)
                                roomrow_notifyme.tag = "o"
                                if (app.alarm.contains(item.id)) {
                                        roomrow_notifyme.setImageResource(R.drawable.ic_notifications_black_24dp)
                                        roomrow_notifyme.tag = "*"
                                        item.alarmID = app.alarm.get(item.id).alarmID
                                }
                                roomrow_notifyme.setOnClickListener {
                                        if ((it.tag as String) == "o" ) {
                                                roomrow_notifyme.setImageResource(R.drawable.ic_notifications_black_24dp)
                                                it.tag = "*"
                                                item.alarmID = (7483640..8483640).random()
                        
                                        } else if ((it.tag as String) == "*") {
                                                roomrow_notifyme.setImageResource(R.drawable.ic_notification_outline_black_24dp)
                                                it.tag = "o"
                                        }
                
                                        alarmListener(item, (it.tag as String))
                                }
                        }
                }
        }
        
        
        inner class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind() = with (itemView) {}
        }
        
        inner class FooterViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind() = with (itemView) {}
        }
        
}