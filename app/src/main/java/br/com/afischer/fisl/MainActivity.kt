package br.com.afischer.fisl

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.asString
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread


class MainActivity: ParentActivity() {
        private var mainDrawer: Drawer? = null
        private var mainAccountHeader: AccountHeader? = null
        
        
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
        
        
                main_about.text = R.string.about.asString(this).asHtml()
        

                initDrawer()


                initTitle()
                initListeners()
                initSponsors()
        }
        
        
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)

                if (resultCode == 1000) {
                        app.agenda.doLoad()
                        app.agenda.doKeywords()
                }
        }

        
        
        
        
        
        private fun initListeners() {
                main_calendar_button.setOnClickListener {
                        if (app.agenda.items.isEmpty()) {
                                toastyShow("w", "A agenda não foi carregada corretamente. Tente recarregá-la através do menu.")
                                return@setOnClickListener
                        }
                        
                        val i = Intent(this, AgendaActivity::class.java)
                        startActivityForResult(i, 1000)
                }
                
                
                main_menu.setOnClickListener {
                        if (!mainDrawer?.isDrawerOpen!!) {
                                mainDrawer?.openDrawer()
                        }
                }
        }
        
        
        
        private fun initSponsors() {
                val list = mutableListOf(
                        R.drawable.ic_sponsor01,
                        R.drawable.ic_sponsor02,
                        R.drawable.ic_sponsor03,
                        R.drawable.ic_sponsor04,
                        R.drawable.ic_sponsor05,
                        R.drawable.ic_sponsor06,
                        R.drawable.ic_sponsor07,
                        R.drawable.ic_sponsor08,
                        R.drawable.ic_sponsor09,
                        R.drawable.ic_sponsor10,
                        R.drawable.ic_sponsor11,
                        R.drawable.ic_sponsor12,
                        R.drawable.ic_sponsor13,
                        R.drawable.ic_sponsor14,
                        R.drawable.ic_sponsor15,
                        R.drawable.ic_sponsor16,
                        R.drawable.ic_sponsor17,
                        R.drawable.ic_sponsor18,
                        R.drawable.ic_sponsor19,
                        R.drawable.ic_sponsor20,
                        R.drawable.ic_sponsor21,
                        R.drawable.ic_sponsor22
                        )
                
                val urls = mutableListOf(
                        "http://nic.br/",
                        "http://www.caixa.gov.br/Paginas/home-caixa.aspx",
                        "https://www.sidia.org.br/",
                        "https://www.redhat.com/pt-br",
                        "https://www.zextras.com/pt-br/",
                        "https://www.thoughtworks.com/pt",
                        "https://rocket.chat/",
                        "https://www.opensuse.org/",
                        "https://www.locaweb.com.br/",
                        "http://www.seprorgs.org.br/",
                        "http://adapta.online",
                        "http://assespro.org.br/",
                        "http://softsul.org.br/",
                        "http://www.propus.com.br/",
                        "http://www.pucrs.br/mct/",
                        "http://www.senacrs.com.br/",
                        "http://www.sucesurs.org.br",
                        "http://www.linux-magazine.com/",
                        "http://pucrs.br/",
                        "https://www.rnp.br/",
                        "http://softwarelivre.org",
                        "http://asl.org.br"
                )
        
        
                main_sponsors.pageCount = list.size
                main_sponsors.setImageListener { position, imageView ->
                        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                        imageView.setImageResource(list[position])
                }
        
                main_sponsors.setImageClickListener { position ->
                        browse(urls[position])
                }
        }



        
        
        
        private fun initTitle() {
                //
                // carregando fonts
                //
                val orbitronBlack = Typeface.createFromAsset(assets, "fonts/orbitron-black.otf")
                val orbitronBold = Typeface.createFromAsset(assets, "fonts/orbitron-bold.otf")
                val orbitronLight = Typeface.createFromAsset(assets, "fonts/orbitron-light.otf")
                val orbitronMedium = Typeface.createFromAsset(assets, "fonts/orbitron-medium.otf")
        
        
        
        
                //
                // alterando fonts
                //
                main_fisl.typeface = orbitronBold
                main_fisl_18.typeface = orbitronBold
                main_fisl_forum.typeface = orbitronMedium
                main_fisl_software.typeface = orbitronMedium
                main_fisl_tecnologia.typeface = orbitronMedium
                
                
                
                
                
                main_dev.setOnClickListener {
                        browse("https://play.google.com/store/apps/developer?id=Andr%C3%A9+Fischer")
                }
        }
        
        
        
        
        
        
        
        
        
        private lateinit var itemAbout: PrimaryDrawerItem
        private lateinit var itemAvalie: PrimaryDrawerItem
        private lateinit var itemReload: PrimaryDrawerItem
        
        
        private fun initDrawer() {
                initDrawerHeader()
                
                
                val drawerBuilder = DrawerBuilder()
                        .withDisplayBelowStatusBar(false)
                        .withActivity(this@MainActivity)
                        .withHeader(R.layout.drawer_header)
                        .withOnDrawerItemClickListener { _, _, drawerItem ->
                                drawerItem?.let {
                                        val id = it.identifier.toInt()
                                        
                                        
                                        when (id) {
                                                1 -> {}
                                                201 -> startActivity<AboutActivity>()
                                                204 -> try {
                                                        browse("market://details?id=$packageName")
                                                } catch (anfe: android.content.ActivityNotFoundException) {
                                                        browse("https://play.google.com/store/apps/details?id=$packageName")
                                                }
                                                205 -> reloadAgenda()
                                                else -> false
                                        }
                                }
                                false
                        }
                
                
                mainDrawer = drawerBuilder.build()
                
                initDrawerItems()
        }
        
        
        
        private fun initDrawerItems() {
                itemAbout = PrimaryDrawerItem().withIdentifier(201).withName("Sobre").withSelectable(false).withIcon(R.drawable.ic_about_black_24dp).withIconTintingEnabled(true)
                itemAvalie = PrimaryDrawerItem().withIdentifier(204).withName("Avalie-me no Google Play").withSelectable(false).withIcon(R.drawable.ic_star_black_24dp).withIconTintingEnabled(true)
                itemReload = PrimaryDrawerItem().withIdentifier(205).withName("Recarregar agenda").withSelectable(false).withIcon(R.drawable.ic_calendar_reload_black_24dp).withIconTintingEnabled(true)
                
                
                mainDrawer!!.removeAllItems()
                mainDrawer!!.addItems(
                        itemReload
                        , DividerDrawerItem()
                        , itemAvalie
                        , itemAbout
                )
        }
        
        
        
        private fun initDrawerHeader() {
                //
                // Create the AccountHeader
                //
                val accountHeaderBuilder = AccountHeaderBuilder()
                        .withActivity(this@MainActivity)
                        .withCompactStyle( true )
                        .withOnlyMainProfileImageVisible(true)
                        .withSelectionListEnabled(false)
                        .withCurrentProfileHiddenInList(true)
                mainAccountHeader = accountHeaderBuilder.build()
        }
        
        
        
        
        private fun reloadAgenda() {
                progressShow("Aguarde")
                doAsync {
                        val result = app.agenda.retrieve()
                
                
                        uiThread {
                                if (result.type != ResultType.SUCCESS) {
                                        toastyShow("w", "Problemas ao obter a agenda. Tente mais tarde.")
                                        return@uiThread
                                }

                                app.agenda.doKeywords()
                                app.agenda.doSave()
                                
                                
                                progressHide()
                                toastyShow("s", "Agenda recarregada.")
                        }
                }
        }
}
