package com.zhdanon.bikefactory.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.zhdanon.bikefactory.R
import com.zhdanon.bikefactory.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: ViewModelBike by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateBike.setOnClickListener {
            viewModel.createBikeDagger(
                logo = "Stern",
                frame = "St" + Random.nextInt(0, 999999) + "F",
                isMTB = Random.nextBoolean()
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bike.collect { bike ->
                binding.bikeLogo.text = getString(R.string.bike_logo, bike.logo)
                binding.tvFrame.text = getString(R.string.frame_number, bike.frame.number)
                binding.tvWheelFront.text = getString(R.string.wheel_number, bike.wheels[0].number)
                binding.tvWheelBack.text = getString(R.string.wheel_number, bike.wheels[1].number)
            }
        }
    }
}