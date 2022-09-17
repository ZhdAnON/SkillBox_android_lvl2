package com.zhdanon.nasaphotos.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zhdanon.nasaphotos.R
import com.zhdanon.nasaphotos.data.photo.PhotoDto
import com.zhdanon.nasaphotos.databinding.FragmentNasaPhotosBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNasaPhotos : Fragment() {
    private var _binding: FragmentNasaPhotosBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var nasaViewModelFactory: NasaViewModelFactory
    private val viewModel: NasaViewModel by viewModels { nasaViewModelFactory }

    private val nasaPagedAdapter = NasaPhotosAdapterRV { photo -> onItemClick(photo) }
    private var adapterSpinnerRovers: ArrayAdapter<String>? = null
    private val spinnerRoversListener = RoverSpinnerInteractionListener()
    private var adapterSpinnerCameras: ArrayAdapter<String>? = null
    private val spinnerCamerasListener = CameraSpinnerInteractionListener()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNasaPhotosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка для Spinner выбора марсохода адаптера и Listener
        setRoverSpinnerAdapter()
        binding.spinnerRovers.setOnTouchListener(spinnerRoversListener)
        binding.spinnerRovers.onItemSelectedListener = spinnerRoversListener

        // Установка для Spinner выбора марсохода адаптера и Listener
        binding.spinnerCameras.setOnTouchListener(spinnerCamerasListener)
        binding.spinnerCameras.onItemSelectedListener = spinnerCamerasListener

        // Установка адаптера RecyclerView
        binding.nasaRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.nasaRecyclerView.adapter =
            nasaPagedAdapter.withLoadStateFooter(LoadStateAdapterRV())
        nasaPagedAdapter.loadStateFlow.onEach {
            binding.swipeRefresh.isRefreshing = it.refresh == LoadState.Loading
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefresh.setOnRefreshListener {
            nasaPagedAdapter.refresh()
        }

        binding.solEdittext.hint = "День съёмки: 0 < ${viewModel.currentRoverMaxSol}"
        // Установка Listener для кнопки выбора дня съёмки
        binding.btnSetSol.setOnClickListener {
            val sol = binding.solEdittext.text.toString().toInt()
            if (0 < sol && sol < viewModel.currentRoverMaxSol) {
                viewModel.setCurrentSol(sol)
                nasaPagedAdapter.refresh()
            } else {
                Toast.makeText(context, "Указано неверное значение", Toast.LENGTH_SHORT).show()
                binding.solEdittext.text.clear()
            }
        }

        // Получение фотографий в RecyclerView
        viewModel.pagedPhotos.onEach {
            nasaPagedAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Установка Spinner для выбора марсохода
    private fun setRoverSpinnerAdapter() {
        viewModel.rovers.onEach {
            val roversList = mutableListOf<String>()
            it.map { rover -> roversList.add(rover.name) }
            adapterSpinnerRovers =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    roversList
                )
            binding.spinnerRovers.adapter = adapterSpinnerRovers
            setCameraSpinnerAdapter()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // Listener для RoverSpinner
    private inner class RoverSpinnerInteractionListener : AdapterView.OnItemSelectedListener,
        View.OnTouchListener {
        var userSelect = false

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            userSelect = true
            return false
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (userSelect) {
                userSelect = false
                val temp = adapterSpinnerRovers?.getItem(position)
                viewModel.setCurrentRover(temp ?: "Curiosity")
                viewModel.setCurrentCamera(null)
                Toast.makeText(
                    requireContext(),
                    if (position != 0) "Выбрано $temp" else "$temp - К отображению фото со всех камер",
                    Toast.LENGTH_SHORT
                ).show()
                binding.solEdittext.hint = "День съёмки: 0 < ${viewModel.currentRoverMaxSol}"
                nasaPagedAdapter.refresh()
                setCameraSpinnerAdapter()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    // Установка Spinner для выбора камеры и условия ввода количества дней съёмки
    private fun setCameraSpinnerAdapter() {
        viewModel.cameras.onEach {
            val cameraList = mutableListOf("Все камеры")
            it.map { camera -> cameraList.add(camera) }
            adapterSpinnerCameras =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    cameraList
                )
            binding.spinnerCameras.adapter = adapterSpinnerCameras
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // Listener для CameraSpinner
    private inner class CameraSpinnerInteractionListener : AdapterView.OnItemSelectedListener,
        View.OnTouchListener {
        var userSelect = false

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            userSelect = true
            return false
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (userSelect) {
                userSelect = false
                val temp = if (position == 0) null else adapterSpinnerCameras?.getItem(position)
                viewModel.setCurrentCamera(temp)
                nasaPagedAdapter.refresh()
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    // Listener для элемента RecyclerView
    private fun onItemClick(photo: PhotoDto) {
        findNavController().navigate(
            R.id.action_fragmentNasaPhotos_to_fragmentCurrentPhoto,
            bundleOf(FragmentCurrentPhoto.PHOTO_LINK_KEY to photo)
        )
    }
}