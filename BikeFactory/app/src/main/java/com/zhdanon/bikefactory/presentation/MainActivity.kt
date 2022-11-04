package com.zhdanon.bikefactory.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.zhdanon.bikefactory.App
import com.zhdanon.bikefactory.R
import com.zhdanon.bikefactory.databinding.ActivityMainBinding
import org.koin.android.ext.android.get
import javax.inject.Inject
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // for Dagger
    @Inject
    lateinit var viewModelFactory: BicycleViewModelFactory
    private lateinit var viewModel: BicycleViewModel

    // for Koin
    private val viewModelKoin: BicycleViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BicycleViewModel(bikeFactory = get()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as App).appDaggerComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[BicycleViewModel::class.java]

        binding.btnDagger.setOnClickListener {
            viewModel.createBikeDagger(
                logo = "Stern",
                frame = "St" + Random.nextInt(0, 999999) + "F",
                color = Color.YELLOW
            )
        }

        binding.btnKoin.setOnClickListener {
            viewModelKoin.createBikeKoin(
                logo = "Forward",
                frame = "Fd" + Random.nextInt(0, 999999) + "F",
                color = Color.RED
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModelKoin.bike.collect { bike ->
                binding.bikeLogo.text = getString(R.string.bike_logo, bike.logo)
                binding.tvFrame.text = getString(R.string.frame_number, bike.frame.number)
                binding.tvWheelFront.text = getString(R.string.wheel_number, bike.wheels[0].number)
                binding.tvWheelBack.text = getString(R.string.wheel_number, bike.wheels[1].number)
                binding.bikeImage.setColorFilter(bike.frame.color)
            }
        }
    }
}