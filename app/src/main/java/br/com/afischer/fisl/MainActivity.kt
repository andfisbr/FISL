package br.com.afischer.fisl

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.webkit.WebView
import br.com.afischer.fisl.adapters.SponsorsAdapter
import br.com.afischer.fisl.enums.ResultType
import br.com.afischer.fisl.extensions.asHtml
import br.com.afischer.fisl.extensions.asString
import br.com.afischer.fisl.models.FISLResult
import br.com.afischer.fisl.models.Sponsor
import com.blankj.utilcode.util.NetworkUtils
import com.crashlytics.android.Crashlytics
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.util.concurrent.TimeUnit


class MainActivity: ParentActivity() {
        private var mainDrawer: Drawer? = null
        private var mainAccountHeader: AccountHeader? = null
        
        
        
        
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_main)
        
        
                initAbout()


                initDrawer()


                initTitle()
                initListeners()
                initSponsors()
        }
        
        
        override fun onResume() {
                super.onResume()
                
                if (!NetworkUtils.isConnected()) {
                        alerterShow("w", "Você está sem conexão com a Internet. A agenda não pode ser carregada. Verifique.")
                }
        }
        
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)

                if (resultCode == 1000) {
                        app.agenda.doLoad()
                        app.agenda.doKeywords()
                }
        }

        
        
        

        private fun initAbout() {
                main_about.show()
                main_about2.hide()


                if (app.about.isNotEmpty()) {
                        val content = """
                        <?xml version="1.0" encoding="UTF-8" ?>
                        <html>
                        <head>
                                <meta http-equiv="content-type" content="text/html; charset=utf-8" />
                                <link href="https://fonts.googleapis.com/css?family=Roboto+Condensed" rel="stylesheet">
                                <style type="text/css">
                                        body {
                                                font-family: 'Roboto Condensed', sans-serif;
                                                font-size: 20px;
                                                color: #708792;
                                        }
                                        .margem {
                                                padding-top: 1em;
                                        }
                                </style>
                        </head>
                        <body class="margem">
                                ${app.about}
                        </body>
                        </html>
                        """
                        //main_about.loadData(content, "text/html; charset=utf-8", "UTF-8")
                        main_about.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
                        main_about.setBackgroundColor(Color.TRANSPARENT)
                        main_about.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
                        
                        return
                }


                main_about2.text = R.string.about.asString(this).asHtml()
                main_about2.show()
                main_about.hide()
        }

        
        
        private fun initListeners() {
                main_calendar_button.setOnClickListener {
                        if (app.agenda.items.isEmpty()) {
                                alerterShow("w", "A agenda não foi carregada corretamente. Tente recarregá-la através do menu.")
                                return@setOnClickListener
                        }
                        
                        if (!NetworkUtils.isConnected()) {
                                alerterShow("w", "Você está sem conexão com a Internet. Verifique.")
                                return@setOnClickListener
                        }
        
        
                        
                        
        
                        val i = Intent(this, AgendaActivity::class.java)

                        
                        
                        //
                        // verifica se a agenda não mudou
                        //
                        progressShow("Aguarde\nprocurando por atualizações")

                        doAsync {
                                var result = FISLResult(ResultType.SUCCESS)

                                
                                app.agenda.retrieveSummary()
                                if (app.agenda.summary!!.hashCode() != app.summaryHashCode) {
                                        app.summaryHashCode =  app.agenda.summary!!.hashCode()
                                        app.settings.agendaSummaryHashCode = app.summaryHashCode
                                        
                                        result = app.agenda.retrieve()
                                }

                                
                                uiThread {
                                        if (result.type == ResultType.SUCCESS) {
                                                app.agenda.doKeywords()
                                                app.agenda.doSave()
                                        }
        
        
                                        progressHide()
                                        startActivityForResult(i, 1000)
                                }
                        }
                }
                
                
                main_menu.setOnClickListener {
                        if (!mainDrawer?.isDrawerOpen!!) {
                                mainDrawer?.openDrawer()
                        }
                }
        }
        
        
        
        private fun initSponsors() {
                val sponsors = mutableListOf(
                        Sponsor("nic.br", R.drawable.ic_sponsor_nicbr, "http://nic.br/"),
                        Sponsor("caixa", R.drawable.ic_sponsor_caixa, "http://www.caixa.gov.br/Paginas/home-caixa.aspx"),
                        Sponsor("sidia", R.drawable.ic_sponsor_sidia, "https://www.sidia.org.br/"),
                        Sponsor("redhat", R.drawable.ic_sponsor_redhat, "https://www.redhat.com/pt-br/"),
                        Sponsor("zimbra zextras", R.drawable.ic_sponsor_zextras, "https://www.zextras.com/pt-br/"),
                        Sponsor("thoughtworks", R.drawable.ic_sponsor_thoughtworks, "https://www.thoughtworks.com/pt/"),
                        Sponsor("rocket chat", R.drawable.ic_sponsor_rocketchat, "https://rocket.chat/"),
                        Sponsor("open suse", R.drawable.ic_sponsor_opensuse, "https://www.opensuse.org/"),
                        Sponsor("locaweb", R.drawable.ic_sponsor_locaweb, "https://www.locaweb.com.br/"),
                        Sponsor("seprorgs", R.drawable.ic_sponsor_seprorgs, "http://www.seprorgs.org.br/"),
                        Sponsor("adapta moodle", R.drawable.ic_sponsor_adapta, "http://adapta.online"),
                        Sponsor("assespro", R.drawable.ic_sponsor_assespro, "http://assespro.org.br/"),
                        Sponsor("softsul", R.drawable.ic_sponsor_softsul, "http://softsul.org.br/"),
                        Sponsor("propus", R.drawable.ic_sponsor_propus, "http://www.propus.com.br/"),
                        Sponsor("museu de ciência e tecnologia da puc rs", R.drawable.ic_sponsor_mctpucrs, "http://www.pucrs.br/mct/"),
                        Sponsor("senac rs", R.drawable.ic_sponsor_senacrs, "http://www.senacrs.com.br/"),
                        Sponsor("sucesu rs", R.drawable.ic_sponsor_sucesurs, "http://www.sucesurs.org.br/"),
                        Sponsor("linux magazine", R.drawable.ic_sponsor_linuxmagazine, "http://www.linux-magazine.com/"),
                        Sponsor("i-educar", R.drawable.ic_sponsor_ieducar, "https://portal.softwarepublico.gov.br/social/i-educar/"),
                        Sponsor("linux professional institute", R.drawable.ic_sponsor_lpi, "http://pucrs.br/"),
                        Sponsor("optdyn", R.drawable.ic_sponsor_optdyn, "https://optdyn.com/"),
                        Sponsor("tecnopuc rs", R.drawable.ic_sponsor_tecnopucrs, "http://pucrs.br/tecnopuc/"),
                        Sponsor("madson books", R.drawable.ic_sponsor_madsonbooks, "http://madsonbooks.com.br/"),
                        Sponsor("puc rs", R.drawable.ic_sponsor_pucrs, "http://pucrs.br/"),
                        Sponsor("rede nacional de ensino e pesquisa", R.drawable.ic_sponsor_rnp, "https://www.rnp.br/"),
                        Sponsor("ministério da ciência, tecnologia, inovação e comunicação", R.drawable.ic_sponsor_mctic, "http://www.mctic.gov.br/portal/"),
                        Sponsor("software livre brasil", R.drawable.ic_sponsor_slb, "http://softwarelivre.org/"),
                        Sponsor("associação software livre", R.drawable.ic_sponsor_asl, "http://asl.org.br/")
                )
        
        
        
                val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                main_sponsors_list.layoutManager = linearLayoutManager
                main_sponsors_list.setHasFixedSize(true)
                val adapter = SponsorsAdapter(
                        activity = this,
                        list = sponsors,
                        listener = { i -> browse(i.url) }
                )
                main_sponsors_list.adapter = adapter
        
                val helper = LinearSnapHelper()
                helper.attachToRecyclerView(main_sponsors_list)
                
                
                
                //
                //  Auto Scroll da esquerda para a direita
                //
                val scrollSpeed = 8000L
                val handler = Handler()
                val runnable = object: Runnable {
                        override fun run() {
                                //
                                // Know The Last Visible Item
                                //
                                val scrollPosition = linearLayoutManager.findLastVisibleItemPosition()

                                
                                if (scrollPosition < sponsors.size) {
                                        if (scrollPosition == sponsors.size - 1) {
                                                try {
                                                        // Delay in Seconds So User Can Completely Read Till Last String
                                                        TimeUnit.MILLISECONDS.sleep(500)
                                                        main_sponsors_list.smoothScrollToPosition(0)  // Jumps Back Scroll To Start Point
                                                } catch(ex: InterruptedException) {
                                                        Crashlytics.logException(ex)
                                                }


                                        } else {
                                                main_sponsors_list.smoothScrollToPosition(scrollPosition + 1)
                                        }
                                        
                                        handler.postDelayed(this, scrollSpeed)
                                }
                        }
                }
                handler.postDelayed(runnable, scrollSpeed)
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
        }
        
        
        
        
        
        
        
        
        
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
                                                206 -> startActivity<DevActivity>()
                                                else -> false
                                        }
                                }
                                false
                        }
                
                
                mainDrawer = drawerBuilder.build()
                
                initDrawerItems()
        }
        
        
        
        private lateinit var itemAbout: PrimaryDrawerItem
        private lateinit var itemAvalie: PrimaryDrawerItem
        private lateinit var itemReload: PrimaryDrawerItem
        private lateinit var itemDev: PrimaryDrawerItem
        
        private fun initDrawerItems() {
                itemAbout = PrimaryDrawerItem().withIdentifier(201).withName("Sobre o app").withSelectable(false).withIcon(R.drawable.ic_about_black_24dp).withIconTintingEnabled(true)
                itemAvalie = PrimaryDrawerItem().withIdentifier(204).withName("Avalie-me no Google Play").withSelectable(false).withIcon(R.drawable.ic_star_black_24dp).withIconTintingEnabled(true)
                itemReload = PrimaryDrawerItem().withIdentifier(205).withName("Recarregar agenda").withSelectable(false).withIcon(R.drawable.ic_calendar_reload_black_24dp).withIconTintingEnabled(true)
                itemDev = PrimaryDrawerItem().withIdentifier(206).withName("Sobre o desenvolvedor").withSelectable(false).withIcon(R.drawable.ic_dev_black_24dp).withIconTintingEnabled(true)
                
                
                mainDrawer!!.removeAllItems()
                mainDrawer!!.addItems(
                        itemReload
                        , DividerDrawerItem()
                        , itemAvalie
                        , itemAbout
                        , DividerDrawerItem()
                        , itemDev
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
                        app.agenda.retrieveSummary()
                        app.summaryHashCode = app.agenda.summary!!.hashCode()
                        app.settings.agendaSummaryHashCode = app.summaryHashCode
                        
                        
                        val result = app.agenda.retrieve()
                
                
                        uiThread {
                                if (result.type != ResultType.SUCCESS) {
                                        progressHide()
                                        alerterShow("w", "Problemas ao obter a agenda. Verifique sua conexão com a Internet e tente outra vez.")
                                        return@uiThread
                                }

                                app.agenda.doKeywords()
                                app.agenda.doSave()
                                
                                
                                progressHide()
                                alerterShow("s", "Agenda recarregada.")
                        }
                }
        }
}
