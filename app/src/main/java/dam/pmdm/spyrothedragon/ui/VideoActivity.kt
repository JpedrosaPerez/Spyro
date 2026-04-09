package dam.pmdm.spyrothedragon.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dam.pmdm.spyrothedragon.R
import dam.pmdm.spyrothedragon.databinding.VideoActivityBinding
import dam.pmdm.spyrothedragon.ui.ui.theme.SpyroTheDragonTheme
import androidx.core.net.toUri

class VideoActivity : ComponentActivity() {
    private lateinit var binding : VideoActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= VideoActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val uri= "android.resource://$packageName/${R.raw.spyro_video}".toUri()
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()
        binding.videoView.setOnCompletionListener {
            finish()
        }
    }
}

