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

    // Binding principal de la actividad
    private lateinit var binding: ActivityMainBinding

    // Binding de la pantalla inicial de la guía
    private lateinit var bindingGuide: GuideBinding

    // Binding de los pasos de la guía
    private lateinit var bindingGuideStep: GuideStepBinding

    // Binding del resumen final de la guía
    private lateinit var bindingResumen: ResumenGuiaBinding

    // Reproductor de sonidos
    private lateinit var soundPool: SoundPool

    // Id del sonido de click
    private var sonidoClick: Int = 0

    // Preferencias para guardar si la guía ya fue vista
    private lateinit var preference: SharedPreferences

    // Controlador de navegación
    private var navController: NavController? = null

    // Variables para saber qué apartados ha visto el usuario
    private var personajesVisto: Boolean = false
    private var mundosVisto: Boolean = false
    private var coleccionesVisto: Boolean = false
    private var infoVisto: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa las preferencias
        preference = getSharedPreferences("preferences", MODE_PRIVATE)

        // Reinicia las preferencias al abrir la app
        reiniciarPreferencias()

        // Infla el layout principal
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enlaza los layouts incluidos con sus bindings
        bindingGuide = GuideBinding.bind(binding.includeLayoutGuide.root)
        bindingGuideStep = GuideStepBinding.bind(binding.includeLayoutGuideStep.root)
        bindingResumen = ResumenGuiaBinding.bind(binding.includeLayoutResumen.root)

        // Oculta al inicio el resumen y los pasos de la guía
        binding.includeLayoutResumen.root.visibility = View.GONE
        binding.includeLayoutGuideStep.root.visibility = View.GONE

        // Muestra la guía solo si aún no ha sido vista
        if (!isGuiaVista()) {
            binding.includeLayoutGuide.root.visibility = View.VISIBLE
        } else {
            binding.includeLayoutGuide.root.visibility = View.GONE
        }

        // Configura el reproductor de sonido
        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .build()

        // Carga el sonido de click
        sonidoClick = soundPool.load(this, R.raw.pop, 1)

        // Busca el fragmento contenedor de navegación
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.navHostFragment)

        // Configura el navController con el menú inferior
        navHostFragment?.let {
            navController = NavHostFragment.findNavController(it)
            NavigationUI.setupWithNavController(binding.navView, navController!!)
            NavigationUI.setupActionBarWithNavController(this, navController!!)
        }

        // Controla qué ocurre al pulsar cada opción del menú inferior
        binding.navView.setOnItemSelectedListener { menuItem ->
            selectedBottomMenu(menuItem)
        }

        // Muestra u oculta la flecha de volver según la pantalla
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

        // Si pulsa en saltar guía, la marca como vista y muestra el resumen
        bindingGuide.saltarGuiaStart.setOnClickListener {
            marcarGuiaVista()
            resumenGuia()
        }

        // Si pulsa comenzar, inicia la guía paso a paso
        bindingGuide.buttonComenzar.setOnClickListener {
            startGuide()
        }
    }

    // Guarda que la guía ya ha sido vista
    private fun marcarGuiaVista() {
        preference.edit {
            putBoolean("guiaVista", true)
        }
    }

    // Comprueba si la guía ya fue vista antes
    private fun isGuiaVista(): Boolean {
        return preference.getBoolean("guiaVista", false)
    }

    // Controla la navegación del menú inferior
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

    // Crea el menú superior
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return true
    }

    // Controla las acciones del menú superior
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_info) {
            showInfoDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    // Muestra el diálogo de información
    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_about)
            .setMessage(R.string.text_about)
            .setPositiveButton(R.string.accept, null)
            .show()
    }

    // Inicia la guía y muestra el primer paso
    private fun startGuide() {
        binding.includeLayoutGuide.root.visibility = View.GONE
        binding.includeLayoutGuideStep.root.visibility = View.VISIBLE
        soundPool.play(sonidoClick, 1f, 1f, 1, 0, 1f)

        // Permite saltar la guía desde esta pantalla
        bindingGuide.saltarGuiaStart.setOnClickListener {
            marcarGuiaVista()
            resumenGuia()
        }

        step1()
    }

    // Primer paso de la guía
    private fun step1() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep1
        val nav = binding.navView
        val primerItem = nav.width / 3

        // Navega a la pantalla de personajes
        navController?.navigate(R.id.navigation_characters)

        // Mueve el círculo y muestra el texto
        circulo.animate()
            .translationX(-primerItem.toFloat())
            .translationY(200f)
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        // Animación de escala del círculo
        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        // Animación de escala del texto
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

        // Si pulsa saltar guía, termina aquí
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

        // Si pulsa la pantalla, pasa al siguiente paso
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

    // Segundo paso de la guía
    private fun step2() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep2
        val nav = binding.navView
        val segundoItem = nav.width / 3

        // Navega a la pantalla de mundos
        navController?.navigate(R.id.navigation_worlds)

        // Mueve el círculo y muestra el texto
        circulo.animate()
            .translationXBy(segundoItem.toFloat())
            .translationY(200f)
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        // Animación de escala del círculo
        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        // Animación de escala del texto
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

        // Si pulsa saltar guía, termina aquí
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

        // Si pulsa la pantalla, pasa al siguiente paso
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

    // Tercer paso de la guía
    private fun step3() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep3
        val nav = binding.navView
        val tercerItem = nav.width / 3

        // Navega a la pantalla de coleccionables
        navController?.navigate(R.id.navigation_collectibles)

        // Mueve el círculo y muestra el texto
        circulo.animate()
            .translationXBy(tercerItem.toFloat())
            .translationY(200f)
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        // Animación de escala del círculo
        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        // Animación de escala del texto
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

        // Si pulsa saltar guía, termina aquí
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

        // Si pulsa la pantalla, pasa al siguiente paso
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

    // Cuarto paso de la guía
    private fun step4() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep4
        val cuartoItem = binding.root.height - (circulo.height / 2)

        // Mantiene la pantalla de coleccionables
        navController?.navigate(R.id.navigation_collectibles)

        // Mueve el círculo hacia la parte superior
        circulo.animate()
            .translationXBy(150f)
            .translationY(-cuartoItem.toFloat())
            .withEndAction {
                circulo.animate().alpha(1f)
                texto.animate().alpha(1f)
            }
            .start()

        // Animación de escala del círculo
        val scaleXCir = ObjectAnimator.ofFloat(circulo, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        val scaleYCir = ObjectAnimator.ofFloat(circulo, View.SCALE_Y, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }

        // Animación de escala del texto
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

        // Oculta el botón de saltar en el último paso
        bindingGuideStep.saltarGuia.alpha = 0f

        // Al pulsar la pantalla termina la guía y muestra el resumen
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

    // Muestra el resumen final de la guía
    private fun resumenGuia() {
        binding.includeLayoutGuide.root.visibility = View.GONE
        binding.includeLayoutGuideStep.root.visibility = View.GONE
        binding.includeLayoutResumen.root.visibility = View.VISIBLE

        // Oculta los elementos antes de animarlos
        bindingResumen.tituloResumen.alpha = 0f
        bindingResumen.filaPersonajes.alpha = 0f
        bindingResumen.filaMundos.alpha = 0f
        bindingResumen.filaColecciones.alpha = 0f
        bindingResumen.filaInfo.alpha = 0f
        bindingResumen.button.alpha = 0f

        // Coloca el icono correcto según lo visto
        ponerCheck(bindingResumen.checkPersonajes, personajesVisto)
        ponerCheck(bindingResumen.checkMundos, mundosVisto)
        ponerCheck(bindingResumen.checkColecciones, coleccionesVisto)
        ponerCheck(bindingResumen.checkInfo, infoVisto)

        var delay = 0L

        // Anima cada fila con un pequeño retraso
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

        // Al pulsar el botón oculta el resumen
        bindingResumen.button.setOnClickListener {
            binding.includeLayoutResumen.root.visibility = View.GONE
            marcarGuiaVista()
        }
    }

    // Cambia la imagen del check según si el apartado fue visto o no
    private fun ponerCheck(imagen: ImageView, visto: Boolean) {
        if (visto) {
            imagen.setImageResource(R.drawable.check)
        } else {
            imagen.setImageResource(R.drawable.not_check)
        }
    }

    // Hace una animación de aparición con alpha
    private fun animarFila(view: View, delay: Long) {
        view.alpha = 0f
        view.visibility = View.VISIBLE

        ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
            duration = 400
            startDelay = delay
            start()
        }
    }

    // Libera el SoundPool al destruir la actividad
    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    // Borra todas las preferencias guardadas
    private fun reiniciarPreferencias() {
        preference.edit {
            clear()
        }
    }
}