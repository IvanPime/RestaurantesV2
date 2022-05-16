package com.diplomado.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.diplomado.firebase.databinding.ActivityMainBinding
import com.diplomado.firebase.databinding.ActivityRegistroBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegistroActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityRegistroBinding
    lateinit var autenticacion: FirebaseAuth
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Registro")
        autenticacion = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        binding.btnRegistrar.setOnClickListener(this)
        binding.btnRegresarLogin.setOnClickListener(this)

        binding.editTextNombre.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.toString().trim { it <= ' ' }.isEmpty()) {
                        binding.textInputLayoutNombre.setErrorEnabled(true)
                        binding.textInputLayoutNombre.setError("Escribe el nombre")
                    } else {
                        binding.textInputLayoutNombre.setErrorEnabled(false)
                        binding.textInputLayoutNombre.setError("")
                    }
                }
            })

        binding.editTextEmail.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.toString().trim { it <= ' ' }.isEmpty()) {
                        binding.textInputLayoutEmail.setErrorEnabled(true)
                        binding.textInputLayoutEmail.setError("Escribe el email")
                    } else {
                        binding.textInputLayoutEmail.setErrorEnabled(false)
                        binding.textInputLayoutEmail.setError("")
                    }
                }
            })

        binding.editTextPassword.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (s.toString().trim { it <= ' ' }.isEmpty()) {
                        binding.textInputLayoutPassword.setErrorEnabled(true)
                        binding.textInputLayoutPassword.setError("Escribe el password")
                    } else {
                        binding.textInputLayoutPassword.setErrorEnabled(false)
                        binding.textInputLayoutPassword.setError("")
                    }
                }
            })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnRegistrar -> {
                val password = binding.editTextPassword.text.toString()
                val nombre = binding.editTextNombre.text.toString()
                val correo = binding.editTextEmail.text.toString()

                if (validarCamposForm() && validateEmail(correo) && validatePassword()) {
                    autenticacion.createUserWithEmailAndPassword(correo, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                var idUser = autenticacion.currentUser?.uid
                                if (idUser != null) {
                                    var map = mutableMapOf<String, String>()
                                    map["nombre"] = nombre
                                    map["correo"] = correo
                                    map["password"] = password

                                    Log.i("DEBUG_INFO", map.toString())
                                    database
                                        .child("Users")
                                        .child(idUser)
                                        .setValue(map).addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                val intent = Intent(this, HomeActivity::class.java)
                                                startActivity(intent)
                                            } else {
                                                Toast.makeText(this,"No se pudo registrar este usuario",Toast.LENGTH_LONG)
                                            }
                                        }
                                }
                            } else {
                                Toast.makeText(this,"No se pudo registrar este usuario",Toast.LENGTH_LONG).show()
                            }
                        }



                }
            }
            R.id.btnRegresarLogin -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validarCamposForm(): Boolean {
        if (binding.editTextNombre.text.toString() == "") {
            binding.textInputLayoutNombre.setError("Escribe el nombre")
            return false
        }
        if (binding.editTextEmail.text.toString() == "") {
            binding.textInputLayoutEmail.setError("Escribe el email")
            return false
        }
        return true
    }

    private val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=\\S+$)" +  // no white spaces
                "(?=.*[0-9])" + //at least 1 digit
                "(?=.*[a-zA-Z])" + // any letter
                ".{6}" +  // at least 6 characters
                "$"
    )
    private fun validatePassword(): Boolean {
        val passwordInput: String = binding.editTextPassword.text.toString().trim()
        // if password field is empty
        // it will display error message "Field can not be empty"
        return if (passwordInput.isEmpty()) {
            binding.textInputLayoutPassword.setError("Escribe un password")
            false
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            binding.textInputLayoutPassword.setError("Escribe un password de 6 caracteres y que incluya " +
                    "un numero y una letra al menos")
            false
        } else {
            binding.textInputLayoutPassword.setErrorEnabled(false)
            binding.textInputLayoutPassword.setError("")
            true
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            binding.textInputLayoutEmail.setError("Escribe un Email")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.textInputLayoutEmail.setError("Escribe un Email Válido")
            false
        } else {
            binding.textInputLayoutEmail.setErrorEnabled(false)
            binding.textInputLayoutEmail.setError("")
            true
        }
    }
}