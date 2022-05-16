package com.diplomado.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.diplomado.firebase.adapter.RestauranteAdapter
import com.diplomado.firebase.databinding.ActivityHomeBinding
import com.diplomado.firebase.model.Restaurante
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var autenticacion: FirebaseAuth
    lateinit var binding: ActivityHomeBinding
    private lateinit var adapter: RestauranteAdapter
    private val restaurantes = mutableListOf<Restaurante>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Restaurantes")
        autenticacion = FirebaseAuth.getInstance()
        binding.btnCerrarSesion.setOnClickListener(this)

        initRecViewRestaurantes()

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getRestaurantes("listaRestaurantes")
            val response = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    Log.i("DEBUG_INFO", "${response}")
                    var restaurantesAux = response?.restaurantes ?: emptyList()
                    restaurantes.clear()
                    restaurantes.addAll(restaurantesAux)
                    adapter.notifyDataSetChanged()
                } else {

                }
            }
        }

    }

    override fun onClick(p0: View?) {
        autenticacion.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://demo4851577.mockable.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun initRecViewRestaurantes() {
        adapter = RestauranteAdapter(restaurantes) {
            val intent = Intent(this, DetalleRestauranteActivity::class.java)
            intent.putExtra("restaurante", it)
            startActivity(intent)
        }
        binding.rvRestaurantes.setHasFixedSize(true)
        binding.rvRestaurantes.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvRestaurantes.adapter = adapter
    }
}