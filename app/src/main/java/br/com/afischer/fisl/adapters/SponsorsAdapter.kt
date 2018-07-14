package br.com.afischer.fisl.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.R
import br.com.afischer.fisl.app.FISLApplication
import br.com.afischer.fisl.extensions.inflate
import br.com.afischer.fisl.models.Sponsor
import kotlinx.android.synthetic.main.sponsor_row.view.*


class SponsorsAdapter(var activity: Activity, private var list: MutableList<Sponsor>, var listener: (i: Sponsor) -> Unit ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var app = activity.application as FISLApplication
        
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = parent.inflate(R.layout.sponsor_row)
                return ItemViewHolder(itemView)
        }
        
        
        
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder as? ItemViewHolder)?.bind(list[position], position)
        }
        

        override fun getItemCount(): Int = list.size
        
        
        
        
        
        inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
                fun bind(item: Sponsor, position: Int) = with(itemView) {
                        sponsorrow_logo.setImageResource(item.logo)
                        sponsorrow_logo.contentDescription = item.description
                        sponsorrow_logo.setOnClickListener {
                                listener(item)
                        }
                }
        }
}