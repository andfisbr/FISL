package br.com.afischer.fisl.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.extensions.inflate
import br.com.afischer.fisl.extensions.pxToDp
import br.com.afischer.fisl.models.Item
import kotlinx.android.synthetic.main.hour2_row.view.*


class Hours2Adapter(private var list: MutableList<Item>, var listener: (i: Item) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = parent.inflate(R.layout.hour2_row)
                return ItemViewHolder(itemView)
        }
        
        
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder as? ItemViewHolder)?.bind(list[position], position)
        }
        
        override fun getItemCount(): Int = list.size

        
        
        
        
        inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind(item: Item, position: Int) = with(itemView) {
                        
                        hourrow_hour.text = item.begins.slice(11..15)
                        
                        item.talk?.let {
                                hourrow_title.text = it.title
                                hourrow_panelist.text = it.owner
                                hourrow_track.text = it.track.split(" - ")[1]
        

                                val layoutParams = hourrow_container.layoutParams as ViewGroup.MarginLayoutParams
                                layoutParams.width = (item.duration * 3).pxToDp()
                                hourrow_container.requestLayout()
        
                                hourrow_container.setOnClickListener {
                                        listener(item)
                                }
                        }
                }
        }
}