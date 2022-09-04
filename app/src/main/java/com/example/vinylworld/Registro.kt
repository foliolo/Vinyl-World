package com.example.vinylworld

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.vinylworld.databinding.ActivityRegistroBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Analytics event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        auth = Firebase.auth
        setup()


        // Boton registrarse 3 = Login
        val boton3 = binding.buttonRegistrarse
        boton3.setOnClickListener {
            val nameValido = validateData()
            if (nameValido) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validateData(): Boolean {

        val nombreyapellidos = binding.TextPersonName.editText?.text.toString()
        val email = binding.TextEmailAddress.editText?.text.toString()
        val password = binding.TextPassword.editText?.text.toString()
        val calleportalypiso = binding.TextPostalAddress.editText?.text.toString()
        val ciudad = binding.TextPostalAddress2.editText?.text.toString()
        val provincia = binding.TextPostalAddress3.editText?.text.toString()
        val codigopostal = binding.TextPostalAddress4.editText?.text.toString()

        if (nombreyapellidos.isEmpty() || email.isEmpty() || password.isEmpty() || calleportalypiso.isEmpty() || ciudad.isEmpty() || provincia.isEmpty() || codigopostal.isEmpty()) {
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_SHORT)
                .show()
        } else {
            return true
        }
        return false
    }


    private fun setup() {
        binding.buttonRegistrarse.setOnClickListener {
            if (binding.TextEmailAddress.editText?.text.toString().isNotEmpty() && binding.TextPassword.editText?.text.toString().isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        binding.TextEmailAddress.editText.toString(),
                        binding.TextPassword.editText.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                    }

            }
        }
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Se ha producido un error auntenticando al usuario")
        builder.setPositiveButton("Acectar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showHome(email: String, provider: ProviderType) {

        val homeIntent = Intent(this, Login::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }

        startActivity(homeIntent)

    }

}





