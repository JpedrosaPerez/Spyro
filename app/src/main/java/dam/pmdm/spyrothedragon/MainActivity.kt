package dam.pmdm.spyrothedragon

import android.animation.ObjectAnimator
import android.content.SharedPreferences
import android.media.SoundPool
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding
import dam.pmdm.spyrothedragon.databinding.GuideBinding
import dam.pmdm.spyrothedragon.databinding.GuideStepBinding
import dam.pmdm.spyrothedragon.databinding.ResumenGuiaBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingGuide: GuideBinding
    private lateinit var bindingGuideStep: GuideStepBinding
    private lateinit var bindingResumen: ResumenGuiaBinding
    private lateinit var soundPool: SoundPool
    private var sonidoClick: Int = 0
    private lateinit var preference: SharedPreferences
    private var navController: NavController? = null

    private var personajesVisto: Boolean = false
    private var mundosVisto: Boolean = false
    private var coleccionesVisto: Boolean = false
    private var infoVisto: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference = getSharedPreferences("preferences", MODE_PRIVATE)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingGuide = GuideBinding.bind(binding.includeLayoutGuide.root)
        bindingGuideStep = GuideStepBinding.bind(binding.includeLayoutGuideStep.root)
        bindingResumen = ResumenGuiaBinding.bind(binding.includeLayoutResumen.root)

        binding.includeLayoutResumen.root.visibility = View.GONE
        binding.includeLayoutGuideStep.root.visibility = View.GONE

        if (!isGuiaVista()) {
            binding.includeLayoutGuide.root.visibility = View.VISIBLE
        } else {
            binding.includeLayoutGuide.root.visibility = View.GONE
        }

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        sonidoClick = soundPool.load(this, R.raw.pop, 1)

        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.navHostFragment)

        navHostFragment?.let {
            navController = NavHostFragment.findNavController(it)
            NavigationUI.setupWithNavController(binding.navView, navController!!)
            NavigationUI.setupActionBarWithNavController(this, navController!!)
        }

        binding.navView.setOnItemSelectedListener { menuItem ->
            selectedBottomMenu(menuItem)
        }

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_characters,
                R.id.navigation_worlds,
                R.id.navigation_collectibles -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        }

        bindingGuide.saltarGuiaStart.setOnClickListener {
            marcarGuiaVista()
            resumenGuia()
        }

        bindingGuide.buttonComenzar.setOnClickListener {
            startGuide()
        }
    }

    private fun marcarGuiaVista() {
        preference.edit {
            putBoolean("guiaVista", true)
        }
    }

    private fun isGuiaVista(): Boolean {
        return preference.getBoolean("guiaVista", false)
    }

    private fun selectedBottomMenu(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_characters ->
                navController?.navigate(R.id.navigation_characters)

            R.id.nav_worlds ->
                navController?.navigate(R.id.navigation_worlds)

            else ->
                navController?.navigate(R.id.navigation_collectibles)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_info) {
            showInfoDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_about)
            .setMessage(R.string.text_about)
            .setPositiveButton(R.string.accept, null)
            .show()
    }

    private fun startGuide() {
        binding.includeLayoutGuide.root.visibility = View.GONE
        binding.includeLayoutGuideStep.root.visibility = View.VISIBLE
        soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)

        bindingGuide.saltarGuiaStart.setOnClickListener {
            marcarGuiaVista()
            resumenGuia()
        }

        step1()
    }

    private fun step1() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep1
        val nav = binding.navView
        val primerItem = nav.width / 3

        navController?.navigate(R.id.navigation_characters)

        circulo.animate()
            .translationX(-primerItem.toFloat())
            .translationY(200f)
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleXText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        scaleXCir.start()
        scaleYCir.start()
        scaleXText.start()
        scaleYText.start()

        bindingGuideStep.saltarGuia.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            personajesVisto = true
            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            marcarGuiaVista()
            resumenGuia()
        }

        bindingGuideStep.root.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            personajesVisto = true
            step2()
        }
    }

    private fun step2() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep2
        val nav = binding.navView
        val segundoItem = nav.width / 3

        navController?.navigate(R.id.navigation_worlds)

        circulo.animate()
            .translationXBy(segundoItem.toFloat())
            .translationY(200f)
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleXText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        scaleXCir.start()
        scaleYCir.start()
        scaleXText.start()
        scaleYText.start()

        bindingGuideStep.saltarGuia.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            mundosVisto = true
            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            marcarGuiaVista()
            resumenGuia()
        }

        bindingGuideStep.root.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            mundosVisto = true
            step3()
        }
    }

    private fun step3() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep3
        val nav = binding.navView
        val tercerItem = nav.width / 3

        navController?.navigate(R.id.navigation_collectibles)

        circulo.animate()
            .translationXBy(tercerItem.toFloat())
            .translationY(200f)
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleXText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        scaleXCir.start()
        scaleYCir.start()
        scaleXText.start()
        scaleYText.start()

        bindingGuideStep.saltarGuia.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            coleccionesVisto = true
            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            marcarGuiaVista()
            resumenGuia()
        }

        bindingGuideStep.root.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            coleccionesVisto = true
            step4()
        }
    }

    private fun step4() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep3
        val cuartoItem = binding.root.height - (circulo.height / 2)

        navController?.navigate(R.id.navigation_collectibles)

        circulo.animate()
            .translationXBy(150f)
            .translationY(-cuartoItem.toFloat())
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleXText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        scaleXCir.start()
        scaleYCir.start()
        scaleXText.start()
        scaleYText.start()

        bindingGuideStep.saltarGuia.alpha = 0f

        bindingGuideStep.root.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }

            fadeOutTexto.start()
            fadeOutCirculo.start()

            infoVisto = true
            soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)
            marcarGuiaVista()
            resumenGuia()
        }
    }

    private fun resumenGuia() {
        binding.includeLayoutGuide.root.visibility = View.GONE
        binding.includeLayoutGuideStep.root.visibility = View.GONE
        binding.includeLayoutResumen.root.visibility = View.VISIBLE

        bindingResumen.tituloResumen.alpha = 0f
        bindingResumen.filaPersonajes.alpha = 0f
        bindingResumen.filaMundos.alpha = 0f
        bindingResumen.filaColecciones.alpha = 0f
        bindingResumen.filaInfo.alpha = 0f
        bindingResumen.button.alpha = 0f

        ponerCheck(bindingResumen.checkPersonajes, personajesVisto)
        ponerCheck(bindingResumen.checkMundos, mundosVisto)
        ponerCheck(bindingResumen.checkColecciones, coleccionesVisto)
        ponerCheck(bindingResumen.checkInfo, infoVisto)

        var delay = 0L

        animarFila(bindingResumen.tituloResumen, delay)
        delay += 250

        animarFila(bindingResumen.filaPersonajes, delay)
        delay += 250

        animarFila(bindingResumen.filaMundos, delay)
        delay += 250

        animarFila(bindingResumen.filaColecciones, delay)
        delay += 250

        animarFila(bindingResumen.filaInfo, delay)
        delay += 250

        animarFila(bindingResumen.button, delay)

        bindingResumen.button.setOnClickListener {
            binding.includeLayoutResumen.root.visibility = View.GONE
            marcarGuiaVista()
        }
    }

    private fun ponerCheck(imagen: ImageView, visto: Boolean) {
        if (visto) {
            imagen.setImageResource(R.drawable.check)
        } else {
            imagen.setImageResource(R.drawable.not_check)
        }
    }

    private fun animarFila(view: View, delay: Long) {
        view.alpha = 0f
        view.visibility = View.VISIBLE

        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 400
            startDelay = delay
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}