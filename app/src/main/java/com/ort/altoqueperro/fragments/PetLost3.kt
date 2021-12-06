package com.ort.altoqueperro.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ort.altoqueperro.R
import com.ort.altoqueperro.viewmodels.PetLostViewModel

class PetLost3 : Fragment() {

    companion object {
        fun newInstance() = PetLost3()
    }

    private lateinit var nextButton: Button
    private lateinit var date: TextView
    lateinit var comments: TextView
    lateinit var v: View
    private lateinit var rootLayout: ConstraintLayout
    private val viewModel: PetLostViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.pet_lost_3_fragment, container, false)

        nextButton = v.findViewById(R.id.btnNext)

        rootLayout = v.findViewById(R.id.pet_lost_root_layout_3)

        comments = v.findViewById(R.id.txtComments)
        comments.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setComments(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        date = v.findViewById(R.id.txtDate)
        /*date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setLostDate(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })*/

        /*date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setLostDate(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })*/

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(),
                { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
                    val mMonthFix = mMonth + 1
                    date.text = "$mDay/$mMonthFix/$mYear"
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        nextButton.setOnClickListener {
            viewModel.setLostDate(date.text.toString())
            if (viewModel.validateStep3()) {
                val action = PetLost3Directions.actionPetLost3ToPetLostConfirmation()
                v.findNavController().navigate(action)
            } else {
                Snackbar.make(rootLayout, "* Campos obligatorios", Snackbar.LENGTH_SHORT).show()

            }
        }

        return v
    }

    override fun onResume() {
        super.onResume()
        fillData()
    }

    private fun fillData() {
        date.text = viewModel.lostDate.value
        comments.text = viewModel.comments.value
    }
}