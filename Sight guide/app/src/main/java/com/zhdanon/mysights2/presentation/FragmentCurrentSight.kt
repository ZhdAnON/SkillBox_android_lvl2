package com.zhdanon.mysights2.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.zhdanon.mysights2.R
import com.zhdanon.mysights2.data.model.CurrentSightDto
import com.zhdanon.mysights2.databinding.FragmentCurrentSightBinding

class FragmentCurrentSight : Fragment() {
    private var _binding: FragmentCurrentSightBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GMapViewModel by activityViewModels()

    private lateinit var currentSight: CurrentSightDto

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentSightBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            currentSight = savedInstanceState.getParcelable(CURRENT_SIGHT_KEY)!!
            setInfo()
        } else {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.currentSight.collect { sight ->
                    currentSight = sight
                    setInfo()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CURRENT_SIGHT_KEY, currentSight)
    }

    private fun setInfo() {
        binding.sightTitle.text = currentSight.name!!.ifEmpty { "Безымянный объект" }
        binding.sightAbout.text = currentSight.wikipediaExtracts?.text
        Glide.with(requireView().context)
            .load(currentSight.preview?.source)
            .placeholder(R.drawable.sight)
            .into(binding.sightImage)
    }

    companion object {
        const val CURRENT_SIGHT_KEY = "Current sight key"
    }
}