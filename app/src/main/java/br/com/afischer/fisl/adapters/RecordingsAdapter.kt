package br.com.afischer.fisl.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.inflate
import kotlinx.android.synthetic.main.recording_row.view.*


class RecordingsAdapter(var activity: Activity, private var list: MutableList<String>, var listener: (i: String) -> Unit ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var app = activity.application as FISLApplication
        
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = parent.inflate(R.layout.recording_row)
                return ItemViewHolder(itemView)
        }
        
        
        
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder as? ItemViewHolder)?.bind(list[position], position)
        }
        

        override fun getItemCount(): Int = list.size
        
        
        
        
        
        inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind(item: String, position: Int) = with(itemView) {
                        recordingrow_play.tag = item
                        recordingrow_play.contentDescription = "Gravação ${position + 1}"
                        recordingrow_play.setOnClickListener {
                                listener(item)
                        }
                }
        }
}