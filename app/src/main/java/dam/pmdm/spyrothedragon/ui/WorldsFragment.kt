package dam.pmdm.spyrothedragon.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.spyrothedragon.R
import dam.pmdm.spyrothedragon.adapters.WorldsAdapter
import dam.pmdm.spyrothedragon.databinding.FragmentWorldsBinding
import dam.pmdm.spyrothedragon.models.World
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class WorldsFragment : Fragment() {

    private var _binding: FragmentWorldsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WorldsAdapter
    private val worldsList = mutableListOf<World>()
    private var contClick = 0
    private var lastWorld: World? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWorldsBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerViewWorlds
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = WorldsAdapter(worldsList)
        adapter.onItemClick = { world ->
            handleWorldClick(world)
        }

        recyclerView.adapter = adapter

        loadWorlds()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadWorlds() {
        try {
            val inputStream: InputStream =
                resources.openRawResource(R.raw.worlds)

            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var currentWorld: World? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "world" -> currentWorld = World()
                            "name" -> currentWorld?.name = parser.nextText()
                            "description" -> currentWorld?.description = parser.nextText()
                            "image" -> currentWorld?.image = parser.nextText()
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        if (parser.name == "world" && currentWorld != null) {
                            worldsList.add(currentWorld)
                        }
                    }
                }
                eventType = parser.next()
            }

            adapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleWorldClick(world: World) {
        if (lastWorld?.name == world.name) {
            contClick++
        } else {
            contClick = 1
            lastWorld = world
        }

        if (contClick == 3) {
            contClick = 0
            lastWorld = null
            openVideo()
        }
    }

    private fun openVideo() {
        Log.d("EASTER_EGG", "hola")
        val intent = Intent(context, VideoActivity::class.java)
startActivity(intent)
    }
}