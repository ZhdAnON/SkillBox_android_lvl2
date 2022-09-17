package com.zhdanon.nasaphotos.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.zhdanon.nasaphotos.R
import com.zhdanon.nasaphotos.data.photo.PhotoDto
import com.zhdanon.nasaphotos.databinding.FragmentCurrentPhotoBinding

class FragmentCurrentPhoto : Fragment() {
    private var _binding: FragmentCurrentPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photo = arguments?.getParcelable<PhotoDto>(PHOTO_LINK_KEY)

        photo?.let {
            val roverName: String = String.format(getString(R.string.rover_name), it.rover.name)
            val cameraName = String.format(getString(R.string.rover_camera_name), it.camera.name)
            val roverSol = String.format(getString(R.string.rover_sol), it.sol)
            val roverDate = String.format(getString(R.string.rover_date), it.earthDate)

            binding.currentRoverName.text = roverName
            binding.currentRoverCameraName.text = cameraName
            binding.currentSol.text = roverSol
            binding.currentDate.text = roverDate
            Glide.with(view.context)
                .load(it.imgSrc)
                .into(binding.currentPhoto)
        }
    }

    companion object {
        const val PHOTO_LINK_KEY = "PHOTO_LINK"
    }
}