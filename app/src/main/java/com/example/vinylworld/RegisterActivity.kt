package com.example.vinylworld

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.vinylworld.databinding.ActivityRegistroBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

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

        /*
        // ValidateData validacion campos
        validateData()
        */
    }

    /*
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
     */

    private fun setup() {
        with(binding) {
            buttonRegistrarse.setOnClickListener {
                when {
                    TextUtils.isEmpty(TextPersonName.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese un Nombre y Apellidos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(TextEmailAddress.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese un E-mail",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(TextPassword.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese una Password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(TextPostalAddress.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese una Calle, portal y piso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(TextPostalAddress2.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese una Ciudad",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(TextPostalAddress3.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese una Provincia",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    TextUtils.isEmpty(TextPostalAddress4.editText?.text.toString().trim { it <= ' ' }) -> {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Por favor ingrese un Codigo postal",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {
                        val email: String = TextEmailAddress.editText?.text.toString().trim { it <= ' ' }
                        val password: String = TextPassword.editText?.text.toString().trim { it <= ' ' }

                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    showLogin(firebaseUser, email, ProviderType.BASIC)
                                } else {
                                    showAlert()
                                }
                            }
                        Toast.makeText(
                            baseContext,
                            "El registro fue exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
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


    private fun showLogin(user: FirebaseUser, email: String, provider: ProviderType) {

        val homeIntent = Intent(this, LoginActivity::class.java).apply {
            putExtra("user", user)
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}




