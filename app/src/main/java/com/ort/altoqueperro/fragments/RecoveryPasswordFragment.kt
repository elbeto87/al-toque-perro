package com.ort.altoqueperro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.RecoveryPasswordViewModel

class RecoveryPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = RecoveryPasswordFragment()
    }

    private lateinit var viewModel: RecoveryPasswordViewModel
    private lateinit var txtEmail: EditText
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var btnRecovery: Button
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.recovery_password_fragment, container, false)
        txtEmail = v.findViewById(R.id.editTextEmail)
        auth = FirebaseAuth.getInstance()
        rootLayout = v.findViewById(R.id.frameLayoutRecovery)
        btnRecovery = v.findViewById(R.id.btnRecovery)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecoveryPasswordViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        btnRecovery.setOnClickListener {
            sendMail()
        }
    }

    private fun sendMail() {
        val email = txtEmail.text.toString()
        if (email.isEmpty()) {
            Snackbar.make(rootLayout, "Por favor ingresar su Email", Snackbar.LENGTH_SHORT)
                .show()
        } else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Snackbar.make(
                            rootLayout,
                            "Se envio el mail con exito",
                            Snackbar.LENGTH_LONG
                        ).show()
                        v.findNavController()
                            .navigate(RecoveryPasswordFragmentDirections.actionRecoveryPasswordFragmentToLogFragment())

                    } else {
                        Snackbar.make(
                            rootLayout,
                            "Problemas para enviar el mail de recuperación",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}


