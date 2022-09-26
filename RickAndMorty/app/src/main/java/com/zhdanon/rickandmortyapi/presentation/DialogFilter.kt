package com.zhdanon.rickandmortyapi.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.zhdanon.rickandmortyapi.R
import com.zhdanon.rickandmortyapi.databinding.DialogFilterBinding

class DialogFilter : DialogFragment() {

    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RaMViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.checkStatus.setOnClickListener {
            if (binding.groupStatus.visibility == View.VISIBLE) {
                binding.groupStatus.visibility = View.GONE
            } else {
                binding.groupStatus.visibility = View.VISIBLE
            }
        }

        binding.checkGender.setOnClickListener {
            if (binding.groupGender.visibility == View.VISIBLE) {
                binding.groupGender.visibility = View.GONE
            } else {
                binding.groupGender.visibility = View.VISIBLE
            }
        }

        binding.btnSetFilter.setOnClickListener {
            viewModel.setFilterParams(
                status = when (binding.groupStatus.checkedRadioButtonId) {
                    R.id.radio_status_alive -> "Alive"
                    R.id.radio_status_dead -> "Dead"
                    R.id.radio_status_unknown -> "Unknown"
                    else -> ""
                },
                gender = when (binding.groupGender.checkedRadioButtonId) {
                    R.id.radio_gender_male -> "Male"
                    R.id.radio_gender_female -> "Female"
                    else -> ""
                }
            )
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SHOW_FILTER_DIALOG = "SHOW_FILTER_DIALOG"
    }
}