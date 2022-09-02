package com.example.vinylworld

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.vinylworld.databinding.ActivityForgotpasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityForgotpasswordBinding

    // Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle click, begin password recovery process
        binding.buttonRecuperar.setOnClickListener {
            validateData()
        }

        // Flecha atras
        val flechaAtras = findViewById<ImageView>(R.id.flechaAtras)
        flechaAtras.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private var email = " "
    private fun validateData() {

        // Get data
        email = binding.emailRecuperar.editText?.text.toString().trim()

        // Validate data
        if (email.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un E-mail", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor ingrese un E-mail valido", Toast.LENGTH_SHORT).show()
        } else {
            recoverPassword()
        }
    }

    private fun recoverPassword() {
        // Show progres
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Toast.makeText(this, "Enviando E-mail", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ha surgido un Error", Toast.LENGTH_SHORT).show()

            }
    }
}

