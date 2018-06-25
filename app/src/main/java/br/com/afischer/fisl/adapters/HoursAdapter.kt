package br.com.afischer.fisl.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.extensions.inflate
import kotlinx.android.synthetic.main.hour2_row.view.*


class HoursAdapter(private var list: MutableList<String>, var listener: (h: String) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        
        companion object {
                private const val TYPE_HEADER = 0
                private const val TYPE_ITEM = 1
                private const val TYPE_FOOTER = 2
        }
        
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
                TYPE_HEADER -> {
                        val itemView = parent.inflate(R.layout.hour_header)
                        HeaderViewHolder(itemView)
                }
                
                TYPE_ITEM -> {
                        val itemView = parent.inflate(R.layout.hour_row)
                        ItemViewHolder(itemView)
                }
                
                
                TYPE_FOOTER -> {
                        val itemView = parent.inflate(R.layout.hour_footer)
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
                fun bind(item: String, position: Int) = with(itemView) {
                        hourrow_hour.text = item
                        
                        hourrow_hour.setOnClickListener {
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