package com.ort.altoqueperro.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.ort.altoqueperro.R
import com.ort.altoqueperro.activities.HomeNavigationActivity
import com.ort.altoqueperro.viewmodels.SignUpViewModel

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var userName: TextView
    private lateinit var userPassword: TextView
    private lateinit var userMail: TextView
    private lateinit var passwordVerify: TextView
    private lateinit var userPhone: TextView
    private lateinit var userBirth: TextView
    private lateinit var signUp: Button
    lateinit var v: View

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.sign_up_fragment, container, false)
        userName = v.findViewById(R.id.userName)
        userPassword = v.findViewById(R.id.userPassword)
        userMail = v.findViewById(R.id.userMail)
        userPhone = v.findViewById(R.id.userPhone)
        userBirth = v.findViewById(R.id.userBirth)
        passwordVerify = v.findViewById(R.id.passwordAuth)
        signUp = v.findViewById(R.id.btnSignUser)

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        viewModel.success.observe(viewLifecycleOwner, {
            if (it.toString() == "success") { //ToDo arreglar?
                startActivity(Intent(activity, HomeNavigationActivity::class.java))
            } else Snackbar.make(v, it, Snackbar.LENGTH_SHORT)
                .show()
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        signUp.setOnClickListener {
            viewModel.registerUser(
                userName.text.toString(),
                userPassword.text.toString(),
                userMail.text.toString(),
                userPhone.text.toString(),
                userBirth.text.toString(),
                passwordVerify.text.toString()
            )
        }
        userBirth.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
                { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
                    val mMonthFix = mMonth + 1
                    userBirth.text = "$mDay/$mMonthFix/$mYear"
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }
}