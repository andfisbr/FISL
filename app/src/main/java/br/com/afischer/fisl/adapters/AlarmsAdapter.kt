package br.com.afischer.fisl.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.extensions.asColor
import br.com.afischer.fisl.extensions.inflate
import br.com.afischer.fisl.extensions.pad
import br.com.afischer.fisl.models.AlarmBase
import kotlinx.android.synthetic.main.alarm_row.view.*
import java.util.*


class AlarmsAdapter(var activity: Activity, private var list: MutableList<AlarmBase>, var listener: (a: AlarmBase) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
        companion object {
                private const val TYPE_HEADER = 0
                private const val TYPE_ITEM = 1
                private const val TYPE_FOOTER = 2
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
                TYPE_HEADER -> {
                        val itemView = parent.inflate(R.layout.alarm_header)
                        HeaderViewHolder(itemView)
                }
                
                TYPE_ITEM -> {
                        val itemView = parent.inflate(R.layout.alarm_row)
                        ItemViewHolder(itemView)
                }
                
                
                TYPE_FOOTER -> {
                        val itemView = parent.inflate(R.layout.alarm_footer)
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
                fun bind(item: AlarmBase, position: Int) = with(itemView) {
        
                        alarmrow_container.setBackgroundColor(R.color.white_smoke.asColor(activity))
                        if (position % 2 == 0)
                                alarmrow_container.setBackgroundColor(R.color.grey_300.asColor(activity))
        
        
        
                        val cal = Calendar.getInstance()
                        cal.timeInMillis = item.hour
        
                        val yyyy = cal[Calendar.YEAR]
                        val MM = cal[Calendar.MONTH] + 1
                        val dd = cal[Calendar.DAY_OF_MONTH]
                        val hh = cal[Calendar.HOUR_OF_DAY]
                        val mm = cal[Calendar.MINUTE]



                        alarmrow_hour.text = "${hh.pad("<00")}:${mm.pad("<00")}"
                        alarmrow_date.text = "${dd.pad("<00")}/${MM.pad("<00")}/$yyyy"
        
                        alarmrow_title.text = item.title
                        alarmrow_room.text = item.room
                        alarmrow_owner.text = item.owner
                        alarmrow_track.text = item.track.split(" - ")[1]
                        

                        alarmrow_del.setOnClickListener {
                                listener(item)
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