package dam.pmdm.spyrothedragon.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import dam.pmdm.spyrothedragon.R
import dam.pmdm.spyrothedragon.databinding.VideoActivityBinding
import androidx.core.net.toUri

class VideoActivity : ComponentActivity() {

    // Binding del layout de la actividad de vídeo
    private lateinit var binding : VideoActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla el layout con ViewBinding
        binding = VideoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crea la ruta del vídeo guardado en res/raw
        val uri = "android.resource://$packageName/${R.raw.spyro_video}".toUri()

        // Asigna el vídeo al VideoView
        binding.videoView.setVideoURI(uri)

        // Inicia la reproducción del vídeo
        binding.videoView.start()

        // Cuando termina el vídeo, cierra la actividad
        binding.videoView.setOnCompletionListener {
            finish()
        }
    }

}

