package com.zhdanon.bikefactory.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zhdanon.bikefactory.R
import com.zhdanon.bikefactory.databinding.FragmentDaggerBinding
import javax.inject.Inject
import kotlin.random.Random

class FragmentDagger : Fragment() {

    private var _binding: FragmentDaggerBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: BicycleViewModelFactory
    private lateinit var viewModel: BicycleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[BicycleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDaggerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.daggerCreate.setOnClickListener {
            viewModel.createBikeDagger(
                logo = "Stern",
                frame = "St" + Random.nextInt(0, 999999) + "F",
                color = Color.YELLOW
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bike.collect { bike ->
                binding.daggerBikeLogo.text = getString(R.string.bike_logo, bike.logo)
                binding.tvDaggerFrame.text = getString(R.string.frame_number, bike.frame.number)
                binding.tvDaggerWheelFront.text =
                    getString(R.string.wheel_number, bike.wheels[0].number)
                binding.tvDaggerWheelBack.text =
                    getString(R.string.wheel_number, bike.wheels[1].number)
                binding.daggerBikeImage.setColorFilter(bike.frame.color)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}