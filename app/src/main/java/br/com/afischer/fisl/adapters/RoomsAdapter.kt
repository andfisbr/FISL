package br.com.afischer.fisl.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.inflate
import br.com.afischer.fisl.extensions.random
import br.com.afischer.fisl.models.Item
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.room_row.view.*


class RoomsAdapter(
        var activity: Activity,
        private var list: MutableList<Item>,
        var listener: (i: Item) -> Unit,
        var filterListener: (i: Item) -> Unit,
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
                        roomrow_container.setBackgroundResource(R.color.white)
        
        
                        roomrow_room.text = item.roomName.replace(" ", "\n")
                        roomrow_duration.text = "${item.duration}min"
                        


                        if (item.status == "empty") {
                                roomrow_title.text = "Sem palestra"
                                roomrow_container.setBackgroundResource(R.color.blue_50)
                                return@with
                        
                        } else if (item.status == "dirty") {
                                roomrow_container.setBackgroundResource(R.color.red_50)
                        }
        
        
        
        
        
                        item.talk?.let {
                                roomrow_owner.show()
                                roomrow_track.show()
                                

                                roomrow_title.text = it.title
                                roomrow_owner.text = it.owner
                                roomrow_track.text = it.track.split(" - ")[1]
                
                
                                roomrow_container.setOnClickListener {
                                        listener(item)
                                }
                                roomrow_track.setOnClickListener { filterListener(item) }
        
        
        
        
                                /**
                                 * altera o ícone conforme o item tenha ou não alarm
                                 */
                                roomrow_notifyme.setImageResource(R.drawable.ic_notification_outline_black_24dp)
                                roomrow_notifyme.contentDescription = "o"
                                if (app.alarms.containsKey(item.id)) {
                                        roomrow_notifyme.setImageResource(R.drawable.ic_notifications_black_24dp)
                                        roomrow_notifyme.contentDescription = "*"
                                        item.alarmID = app.alarms[item.id]!!.alarmID
                                }
                                roomrow_notifyme.setOnClickListener {
                                        if (it.contentDescription == "o" ) {
                                                roomrow_notifyme.setImageResource(R.drawable.ic_notifications_black_24dp)
                                                it.contentDescription = "*"
                                                item.alarmID = (7483640..8483640).random()
                        
                                        } else if (it.contentDescription == "*") {
                                                roomrow_notifyme.setImageResource(R.drawable.ic_notification_outline_black_24dp)
                                                it.contentDescription = "o"
                                        }
                
                                        alarmListener(item, it.contentDescription.toString())
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