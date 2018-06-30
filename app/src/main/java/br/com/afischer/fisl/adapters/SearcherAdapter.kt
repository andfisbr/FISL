package br.com.afischer.fisl.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import br.com.afischer.fisl.R
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.normalise
import br.com.afischer.fisl.models.Keyword
import kotlinx.android.synthetic.main.searcher_view.view.*


class SearcherAdapter(
        context: Context,
        private val itemLayout: Int,
        private var dataList: MutableList<Keyword>?
): ArrayAdapter<Keyword>(context, itemLayout, dataList) {
        
        
        private val listFilter = ListFilter()
        private var dataListAllItems: MutableList<Keyword>? = null
        
        
        
        override fun getCount(): Int = dataList?.size ?: 0
        override fun getItem(position: Int): Keyword? = dataList?.get(position)
        
        override fun getView(position: Int, view: View?, parent: ViewGroup): View {
                var v = view
                
                if (v == null) {
                        v = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
                }
                
                val item = getItem(position)
                v!!.sv_text.text = item?.text?.asHtml()
                v.sv_type.text = item?.type
                
                
                when (item?.type) {
                        "palestrante" -> v.sv_container.setBackgroundResource(R.color.green_50)
                        "título" -> v.sv_container.setBackgroundResource(R.color.blue_50)
                        "trilha" -> v.sv_container.setBackgroundResource(R.color.red_50)
                }
                
                
                return v
        }
        
        
        override fun getFilter(): Filter {
                return listFilter
        }
        
        
        
        
        
        
        inner class ListFilter: Filter() {
                private val lock = Any()
                
                override fun performFiltering(prefix: CharSequence?): Filter.FilterResults {
                        val results = Filter.FilterResults()
                        if (dataListAllItems == null) {
                                synchronized(lock) {
                                        dataListAllItems = mutableListOf()
                                        dataListAllItems?.addAll(dataList!!)
                                }
                        }
                        
                        if (prefix == null || prefix.isEmpty()) {
                                synchronized(lock) {
                                        results.values = dataListAllItems
                                        results.count = dataListAllItems!!.size
                                }
                                
                        } else {
                                val searchStrLowerCase = prefix.toString().normalise().toLowerCase()
                                
                                val matchValues = mutableListOf<Keyword>()
                                
                                for (dataItem in dataListAllItems!!) {
                                        dataItem.text = dataItem.text.replace("<strong>", "").replace("</strong>", "")
                                        if (dataItem.text.normalise().toLowerCase().contains(searchStrLowerCase)) {
                                                dataItem.text = dataItem.text.replace(prefix.toString(), "<strong>$prefix</strong>")
                                                matchValues.add(dataItem)
                                        }
                                }
                                
                                results.values = matchValues
                                results.count = matchValues.size
                        }
                        
                        return results
                }
                
                override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
                        dataList = if (results.values != null) {
                                results.values as MutableList<Keyword>
                        } else {
                                null
                        }
                        if (results.count > 0) {
                                notifyDataSetChanged()
                        } else {
                                notifyDataSetInvalidated()
                        }
                }
                
        }
}