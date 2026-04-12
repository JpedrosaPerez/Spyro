package dam.pmdm.spyrothedragon.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.spyrothedragon.R
import dam.pmdm.spyrothedragon.databinding.CardviewBinding
import dam.pmdm.spyrothedragon.models.World

// Adapter para mostrar la lista de mundos
class WorldsAdapter(
    private val list: List<World>
) : RecyclerView.Adapter<WorldsAdapter.WorldsViewHolder>() {

    // Listener para detectar clicks
    var onItemClick: ((World) -> Unit)? = null

    // Relaciona nombre de imagen con drawable
    private val worldImages = mapOf(
        "sunny_beach" to R.drawable.sunny_beach,
        "midday_gardens" to R.drawable.midday_gardens,
        "autumn_plains" to R.drawable.autumn_plains,
        "glimmer" to R.drawable.glimmer,
        "cloud_spires" to R.drawable.cloud_spires,
        "hurricane_halls" to R.drawable.hurricane_halls,
        "frozen_altars" to R.drawable.frozen_altars,
        "lost_fleet" to R.drawable.lost_fleet,
        "sunset_beach" to R.drawable.sunset_beach
    )

    // Crea cada item usando ViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldsViewHolder {
        val binding = CardviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorldsViewHolder(binding)
    }

    // Asigna los datos a cada item
    override fun onBindViewHolder(holder: WorldsViewHolder, position: Int) {
        val world = list[position]

        // Nombre del mundo
        holder.binding.name.text = world.name

        // Imagen del mundo (o placeholder si no existe)
        val drawableRes = worldImages[world.image] ?: R.drawable.placeholder
        holder.binding.image.setImageResource(drawableRes)

        // Click en el item
        holder.binding.root.setOnClickListener {
            onItemClick?.invoke(world)
        }
    }

    // Número de elementos
    override fun getItemCount(): Int = list.size

    // ViewHolder usando ViewBinding (más limpio)
    class WorldsViewHolder(val binding: CardviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}