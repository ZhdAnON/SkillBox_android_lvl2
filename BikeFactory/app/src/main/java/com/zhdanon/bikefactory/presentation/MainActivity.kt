package com.zhdanon.bikefactory.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zhdanon.bikefactory.App
import com.zhdanon.bikefactory.R
import com.zhdanon.bikefactory.TAG
import com.zhdanon.bikefactory.databinding.ActivityMainBinding
import com.zhdanon.bikefactory.di.dagger.AppDaggerComponent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var appComponent: AppDaggerComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appComponent = (applicationContext as App).appDaggerComponent
        appComponent.inject(this)

        binding.btnDagger.setOnClickListener {
            Log.d(TAG, "onCreate: btnDagger clicked")
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, FragmentDagger())
                .commit()
        }

        binding.btnKoin.setOnClickListener {
            Log.d(TAG, "onCreate: btnKoin clicked")
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_container, FragmentKoin())
                .commit()
        }
    }
}