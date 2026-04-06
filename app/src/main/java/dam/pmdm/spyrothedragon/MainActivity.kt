package dam.pmdm.spyrothedragon

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding
import dam.pmdm.spyrothedragon.databinding.GuideBinding
import dam.pmdm.spyrothedragon.databinding.GuideStepBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingGuide: GuideBinding
    private lateinit var bindingGuideStep: GuideStepBinding
    private var navController: NavController? = null
    private var pasoActual = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindingGuide = GuideBinding.bind(binding.includeLayoutGuide.root)
        bindingGuideStep = GuideStepBinding.bind(binding.includeLayoutGuideStep.root)
        binding.includeLayoutGuide.root.visibility = View.VISIBLE
        binding.includeLayoutGuideStep.root.visibility = View.GONE

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
                    // En las pantallas de los tabs no mostramos la flecha atrás
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }

                else -> {
                    // En el resto de pantallas sí
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
            }
        }
        bindingGuide.buttonComenzar.setOnClickListener {
            startGuide()
        }
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

        step1()
        pasoActual = 1

    }

    private fun step1() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep1
        val nav = binding.navView
        val primerItem = nav.width / 3
        circulo.animate()
            .translationX(-primerItem.toFloat())
            .translationY(200.toFloat())
            .withEndAction {
                circulo.animate()
                    .alpha(1f)
                texto.animate()
                    .alpha(1f)
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
        val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        scaleXCir.start()
        scaleYCir.start()
        scaleXText.start()
        scaleYText.start()
        bindingGuideStep.root.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            fadeOutTexto.start()
            fadeOutCirculo.start()

            step2()
        }
    }

    private fun step2() {
        val circulo = bindingGuideStep.circuloSelector
        val texto = bindingGuideStep.textStep1
        val nav = binding.navView
        val primerItem = nav.width / 3
        circulo.animate()
            .translationXBy(primerItem.toFloat())
            .translationY(200.toFloat())
            .withEndAction {
                circulo.animate()
                    .alpha(1f)
                texto.animate()
                    .alpha(1f)
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
        val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
            duration = 500
            repeatCount = 5
        }
        scaleXCir.start()
        scaleYCir.start()
        scaleXText.start()
        scaleYText.start()
        bindingGuideStep.root.setOnClickListener {
            val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                duration = 400
            }
            fadeOutTexto.start()
            fadeOutCirculo.start()
            step3()
        }
    }

    private fun step3() {

            val circulo = bindingGuideStep.circuloSelector
            val texto = bindingGuideStep.textStep1
            val nav = binding.navView
            val primerItem = nav.width / 3
            circulo.animate()
                .translationXBy(primerItem.toFloat())
                .translationY(200.toFloat())
                .withEndAction {
                    circulo.animate()
                        .alpha(1f)
                    texto.animate()
                        .alpha(1f)
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
            val scaleYText = ObjectAnimator.ofFloat(texto, View.SCALE_X, 1f, 1.1f, 1f).apply {
                duration = 500
                repeatCount = 5
            }
            scaleXCir.start()
            scaleYCir.start()
            scaleXText.start()
            scaleYText.start()
            bindingGuideStep.root.setOnClickListener {
                val fadeOutCirculo = ObjectAnimator.ofFloat(circulo, View.ALPHA, 1f, 0f).apply {
                    duration = 400
                }
                val fadeOutTexto = ObjectAnimator.ofFloat(texto, View.ALPHA, 1f, 0f).apply {
                    duration = 400
                }
                fadeOutTexto.start()
                fadeOutCirculo.start()
                step3()
            }
        }
    }

