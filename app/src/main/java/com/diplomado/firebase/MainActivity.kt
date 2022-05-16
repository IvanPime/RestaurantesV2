package com.diplomado.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.diplomado.firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var autenticacion: FirebaseAuth
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Iniciar Sesión")

        autenticacion = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(this)
        binding.btnRegistrarse.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        val user: FirebaseUser? = autenticacion.currentUser
        if (user == null) {

        } else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                if (validarCamposForm()) {
                    val correo: String = binding.username.text.toString()
                    val password: String = binding.password.text.toString()
                    autenticacion.signInWithEmailAndPassword(correo,password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                // Toast.makeText(this,"Login exitoso",Toast.LENGTH_LONG).show()
                            }
                            else{
                                Toast.makeText(this,"Credenciales incorrectas",Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
            R.id.btnRegistrarse -> {
                val intent = Intent(this, RegistroActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validarCamposForm(): Boolean {
        if (binding.username.text.toString() == "") {
            Toast.makeText(this,"Escribir email",Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.password.text.toString() == "") {
            Toast.makeText(this,"Escribir password",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
}