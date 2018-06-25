package br.com.afischer.fisl.adapters

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.extensions.inflate
import br.com.afischer.fisl.models.Item
import br.com.afischer.fisl.models.RoomBase
import kotlinx.android.synthetic.main.room2_row.view.*


class Rooms2Adapter(private var list: MutableList<RoomBase>, private var schedules: MutableList<Item>, var listener: (i: Item) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = parent.inflate(R.layout.room2_row)
                return ItemViewHolder(itemView)
        }
        
        
        
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder as? ItemViewHolder)?.bind(list[position], position)
        }
        


        override fun getItemCount(): Int = list.size
        
        


        inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind(item: RoomBase, position: Int) = with(itemView) {
        
                        roomrow_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        roomrow_list.setHasFixedSize(true)
        
        
                        val adapter = Hours2Adapter(schedules.filter { it.room == item.id }.sortedBy { it.hour }.toMutableList()) { s -> listener(s) }
                        roomrow_list.adapter = adapter
                        
                        
                        roomrow_id.text = item.name
                }
        }
}