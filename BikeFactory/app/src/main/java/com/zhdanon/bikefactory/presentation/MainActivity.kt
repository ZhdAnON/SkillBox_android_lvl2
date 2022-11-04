package com.zhdanon.bikefactory.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zhdanon.bikefactory.App
import com.zhdanon.bikefactory.R
import com.zhdanon.bikefactory.databinding.ActivityMainBinding
import javax.inject.Inject
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: BicycleViewModelFactory
    private lateinit var viewModel: BicycleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as App).appDaggerComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[BicycleViewModel::class.java]

        binding.btnCreateBike.setOnClickListener {
            viewModel.createBikeDagger(
                logo = "Stern",
                frame = "St" + Random.nextInt(0, 999999) + "F",
                color = Color.YELLOW
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bike.collect { bike ->
                binding.bikeLogo.text = getString(R.string.bike_logo, bike.logo)
                binding.tvFrame.text = getString(R.string.frame_number, bike.frame.number)
                binding.tvWheelFront.text = getString(R.string.wheel_number, bike.wheels[0].number)
                binding.tvWheelBack.text = getString(R.string.wheel_number, bike.wheels[1].number)
                binding.bikeImage.setColorFilter(bike.frame.color)
            }
        }
    }
}