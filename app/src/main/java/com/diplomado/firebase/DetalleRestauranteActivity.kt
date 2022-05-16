package com.diplomado.firebase

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.diplomado.firebase.adapter.ImagenesRestauranteAdapter
import com.diplomado.firebase.databinding.ActivityDetalleRestauranteBinding
import com.diplomado.firebase.model.Restaurante
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class DetalleRestauranteActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetalleRestauranteBinding
    private lateinit var map: GoogleMap
    private lateinit var restaurante: Restaurante

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleRestauranteBinding.inflate(layoutInflater)
        setTitle("Detalle")

        setContentView(binding.root)
// calling the action bar
        // calling the action bar
        val actionBar: ActionBar? = supportActionBar

        // showing the back button in action bar

        // showing the back button in action bar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras!!
        restaurante = bundle.getParcelable<Restaurante>("restaurante")!!

        Log.i("DEBUG_INFO", "${restaurante}")

        binding.tvNombreRestaurante.text = "Nombre: " + restaurante.nombre
        binding.tvCalificacionRestaurante.text = "Calificacion: " + restaurante.calificacion?.toString()
        binding.tvAnioRestaurante.text = "Año: " + restaurante?.anio.toString()
        binding.tvCostoPromedioRestaurante.text = "Costo Promedio: " + restaurante.costoPromedio.toString()
        binding.loadingWheel.visibility = View.VISIBLE
        Glide.with(this).load(restaurante.imagenes[0]).listener(object:
            RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                binding.loadingWheel.visibility = View.GONE
                binding.imgRestaurante.setImageResource(R.drawable.ic_image_not_supported_black)
                return false
            }
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.loadingWheel.visibility = View.GONE
                return false
            }

        }).error(R.drawable.ic_image_not_supported_black).into(binding.imgRestaurante)

        Log.i("DEBUG_INFO", "${restaurante.imagenes}")
        val adaptador = ImagenesRestauranteAdapter(restaurante.imagenes)
        binding.rvImagenesRestaurante.setHasFixedSize(true)
        binding.rvImagenesRestaurante.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImagenesRestaurante.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
        binding.rvImagenesRestaurante.adapter = adaptador

        createMapFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }

        val favoritePlace = LatLng(restaurante.latitud, restaurante.longitud)
        map.addMarker(MarkerOptions().position(favoritePlace).title(restaurante.nombre.toString()))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
            2000,
            null
        )
    }

}