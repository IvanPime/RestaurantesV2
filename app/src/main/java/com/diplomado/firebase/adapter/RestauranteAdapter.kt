package com.diplomado.firebase.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.diplomado.firebase.R
import com.diplomado.firebase.model.Restaurante
import com.diplomado.firebase.databinding.ItemRestauranteBinding


class RestauranteAdapter(
    private var restaurantes: List<Restaurante>,
    private var setOnClickListener: (Restaurante) -> Unit
):
    RecyclerView.Adapter<RestauranteAdapter.ViewHolder>() {

    class ViewHolder(val item: View): RecyclerView.ViewHolder(item) {
        val tvNombre = item.findViewById<TextView>(R.id.tvNombre)
        val tvCalificacion = item.findViewById<TextView>(R.id.tvCalificacion)
        val tvAnio = item.findViewById<TextView>(R.id.tvAnio)
        val tvCostoPromedio = item.findViewById<TextView>(R.id.tvCostoPromedio)
        val imageView = item.findViewById<ImageView>(R.id.imgRestaurante)
        val loadingWheel = item.findViewById<ProgressBar>(R.id.loading_wheel)

        fun bindTitular(restaurante: Restaurante) {
            tvNombre.text = restaurante.nombre
            tvCalificacion.text = restaurante.calificacion.toString()
            tvAnio.text = restaurante.anio.toString()
            tvCostoPromedio.text = restaurante.costoPromedio.toString()

            loadingWheel.visibility = View.VISIBLE
            Glide.with(item.context).load(restaurante.imagenes[0]).listener(object:
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    loadingWheel.visibility = View.GONE
                    imageView.setImageResource(R.drawable.ic_image_not_supported_black)
                    return false
                }
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    loadingWheel.visibility = View.GONE
                    return false
                }

            }).error(R.drawable.ic_image_not_supported_black).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemRestauranteBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurante = restaurantes[position]
        holder.bindTitular(restaurante)
        holder.item.setOnClickListener {
            setOnClickListener(restaurante)
        }
    }
    override fun getItemCount() = restaurantes.size

}