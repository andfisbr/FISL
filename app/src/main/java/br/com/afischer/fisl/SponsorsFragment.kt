package br.com.afischer.fisl


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.afischer.fisl.bases.BaseView
import kotlinx.android.synthetic.main.fragment_sponsors.*


class SponsorsFragment: ParentFragment(), BaseView {
        private var param: Int = R.drawable.ic_launcher_144
        
        
        

        companion object {
                private val ARG_PARAM1 = "image"
                
                
                fun newInstance(param1: Int): SponsorsFragment {
                        val fragment = SponsorsFragment()
                        val args = Bundle()
                        args.putInt(ARG_PARAM1, param1)
                        fragment.arguments = args
                        return fragment
                }
        }



        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                arguments?.let {
                        param = it.getInt(ARG_PARAM1)
                }
        }
        
        

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                rootView = inflater.inflate(R.layout.fragment_sponsors, container, false)
                return rootView
        }
        
        
        
        
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                
                
                //sponsors_logo.setImageResource(param)
                sponsors_container.setBackgroundResource(param)
        }
}
