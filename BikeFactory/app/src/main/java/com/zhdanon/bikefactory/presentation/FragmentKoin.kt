package com.zhdanon.bikefactory.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.zhdanon.bikefactory.R
import com.zhdanon.bikefactory.databinding.FragmentKoinBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class FragmentKoin : Fragment() {
    private var _binding: FragmentKoinBinding? = null
    private val binding get() = _binding!!

    private val viewModelKoin by viewModel<BicycleViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.koinCreate.setOnClickListener {
            viewModelKoin.createBikeKoin(
                logo = "Forward",
                frame = "Fd" + Random.nextInt(0, 999999) + "F",
                color = Color.RED
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModelKoin.bike.collect { bike ->
                binding.koinBikeLogo.text = getString(R.string.bike_logo, bike.logo)
                binding.tvKoinFrame.text = getString(R.string.frame_number, bike.frame.number)
                binding.tvKoinWheelFront.text = getString(R.string.wheel_number, bike.wheels[0].number)
                binding.tvKoinWheelBack.text = getString(R.string.wheel_number, bike.wheels[1].number)
                binding.koinBikeImage.setColorFilter(bike.frame.color)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}