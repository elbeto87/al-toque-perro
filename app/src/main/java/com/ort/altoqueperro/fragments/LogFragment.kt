package com.ort.altoqueperro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ort.altoqueperro.R
import com.ort.altoqueperro.activities.SplashWelcomeActivity
import com.ort.altoqueperro.viewmodels.LogViewModel


class LogFragment : Fragment() {

    companion object {
        fun newInstance() = LogFragment()
    }

    private lateinit var inputMail: TextView
    private lateinit var inputPassword: TextView
    private lateinit var login: Button
    private lateinit var signUp: TextView
    private lateinit var txtSignUp: TextView
    private lateinit var lostPass: TextView
    private lateinit var rootLayout: ConstraintLayout
    lateinit var v: View
    private lateinit var viewModel: LogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.log_fragment, container, false)

        login = v.findViewById(R.id.btnLogIn)
        signUp = v.findViewById(R.id.btnSign)
        inputMail = v.findViewById(R.id.idUser)
        inputPassword = v.findViewById(R.id.idPassword)
        txtSignUp = v.findViewById(R.id.textView5)
        lostPass = v.findViewById(R.id.lostPassword)
        rootLayout = v.findViewById(R.id.frameLayout)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LogViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        signUp.setOnClickListener {
            println("ingresando")
            val action = LogFragmentDirections.actionLogFragmentToSignUpFragment()
            v.findNavController().navigate(action)

        }

        lostPass.setOnClickListener {
            println("recuperar contraseña")
            val action = LogFragmentDirections.actionLogFragmentToRecoveryPasswordFragment()
            v.findNavController().navigate(action)
        }

        login.setOnClickListener {
            val mail = inputMail.text.toString()
            val password = inputPassword.text.toString()

            if (mail.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(context, SplashWelcomeActivity::class.java))
                        } else {
                            println("credenciales no válidas")
                            Snackbar.make(
                                rootLayout,
                                "Email o Contraseña incorrecta",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                        }
                    }
            } else {
                Snackbar.make(rootLayout, "Email o Contraseña incorrecta", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

}