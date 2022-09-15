package com.zhdanon.mytimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zhdanon.mytimer.databinding.FragmentTimerBinding

class FragmentTimer : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clock = binding.myAnalogClock

        binding.btnRun.setOnClickListener {
            clock.timer.isPlayed = !clock.timer.isPlayed
            binding.btnReset.isEnabled = !clock.timer.isPlayed
            if (clock.timer.isPlayed) {
                clock.start()
                binding.btnRun.text = getString(R.string.btn_stop)
            } else {
                clock.stop()
                binding.btnRun.text = getString(R.string.btn_start)
            }
        }

        binding.btnReset.setOnClickListener {
            clock.reset()
            binding.myDigitalClock.reset()
        }

        binding.myAnalogClock.addUpdateListener(binding.myDigitalClock::setTime)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}