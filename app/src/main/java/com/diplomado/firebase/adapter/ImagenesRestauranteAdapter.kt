package com.diplomado.firebase.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.diplomado.firebase.R
import com.diplomado.firebase.databinding.ItemImagenRestauranteBinding

class ImagenesRestauranteAdapter(
    private var imagenes: List<String>,
):
    RecyclerView.Adapter<ImagenesRestauranteAdapter.ViewHolder>() {

    class ViewHolder(val item: View): RecyclerView.ViewHolder(item) {
        val imageView = item.findViewById<ImageView>(R.id.detalleImagen)
        val loadingWheel = item.findViewById<ProgressBar>(R.id.loading_wheel)

        fun bindTitular(imagen: String) {
            loadingWheel.visibility = View.VISIBLE
            Glide.with(item.context).load(imagen).listener(object:
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
        var binding = ItemImagenRestauranteBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imagen = imagenes[position]
        holder.bindTitular(imagen)
    }
    override fun getItemCount() = imagenes.size

}