package com.ort.altoqueperro.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ort.altoqueperro.R
import com.ort.altoqueperro.activities.SplashActivity
import com.ort.altoqueperro.viewmodels.ChangePasswordViewModel

class ChangePassword : Fragment() {


    companion object {
        fun newInstance() = ChangePassword()
    }

    private lateinit var newPassword: TextView
    private lateinit var confirmPassword: TextView
    private lateinit var btnConfirmPassword: Button
    lateinit var v: View
    private lateinit var currentUser: FirebaseUser

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.change_password_fragment, container, false)

        newPassword = v.findViewById(R.id.newPassword)
        confirmPassword = v.findViewById(R.id.confirmPassword)
        btnConfirmPassword = v.findViewById(R.id.btnConfirmPassword)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        currentUser = ChangePasswordArgs.fromBundle(requireArguments()).user
    }

    override fun onStart() {
        super.onStart()

        btnConfirmPassword.setOnClickListener {

            if (newPassword.text.isNotEmpty() && confirmPassword.text.isNotEmpty()) {
                if (newPassword.text.contentEquals(confirmPassword.text)) {

                    println("password changed")
                    currentUser.updatePassword(newPassword.text.toString())
                        .addOnSuccessListener {
                            Snackbar.make(v, "Password changed!", Snackbar.LENGTH_SHORT).show()
                        }
                    Firebase.auth.signOut()
                    startActivity(Intent(context, SplashActivity::class.java))

                } else {
                    Snackbar.make(
                        v,
                        "Las nuevas contraseñas tienen que ser iguales",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                Snackbar.make(v, "Todos los campos son obligatorios", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}