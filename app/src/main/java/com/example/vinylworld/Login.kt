package com.example.vinylworld

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.vinylworld.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")
        binding.TextMail.editText?.setText(email)

        auth = Firebase.auth

        setupView()
    }

    private fun setupView() {
        //Boton iniciar sesion 2
        binding.bottonIniarSesion.setOnClickListener {
            btnLogin()
        }

        //Boton de registro 1
        binding.buttonRegistro.setOnClickListener {
            val intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        // Texto olvidaste la contraseña? = ForgotPassword
        binding.olvidastecontra.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun btnLogin() {
        with(binding) {
            val email: String = TextMail.editText?.text.toString().trim { it <= ' ' }
            val password: String = TextPasswordLogin.editText?.text.toString().trim { it <= ' ' }

            if (login(email, password)) {
                auth.signInWithEmailAndPassword(
                    email,
                    password
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val firebaseUser: FirebaseUser = it.result!!.user!!
                        showHome(firebaseUser, ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun login(email: String, pass: String): Boolean {

        var emailValido = false
        var passValida = false

        if (email.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un E-mail y una password", Toast.LENGTH_SHORT)
                .show()
        } else {
            emailValido = validateData(email)
            passValida = validatePassword(pass)
        }


        return emailValido && passValida
    }

    // Para validar el email
    private fun validateData(email: String): Boolean {
        //validate data
        if (email.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese un E-mail", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Por favor ingrese un E-mail valido", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        return false
    }

    // Para validar contrase単a
    private fun validatePassword(pass: String): Boolean {
        //validate data
        if (pass.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese una password", Toast.LENGTH_SHORT).show()
        }else if (!Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}\$")
                .matcher(pass).matches()
        ) {
            Toast.makeText(this, "Por favor ingrese una password valida", Toast.LENGTH_SHORT)
                .show()
        }else {
            return true
        }
        return false
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Se ha producido un error auntenticando al usuario")
        builder.setPositiveButton("Acectar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: FirebaseUser, provider: ProviderType) {

        val homeIntent = Intent(this, Menu::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("email", email.email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
        finish()
    }
}
