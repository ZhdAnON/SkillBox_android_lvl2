package com.zhdanon.mysights2.presentation

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zhdanon.mysights2.App
import com.zhdanon.mysights2.R
import com.zhdanon.mysights2.databinding.FragmentGoogleMapBinding
import com.zhdanon.mysights2.entity.Sight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GMapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentGoogleMapBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var gMapViewModelFactory: GMapViewModelFactory
    private val viewModel: GMapViewModel by activityViewModels { gMapViewModelFactory }

    private var needAnimateCamera = false
    private var needMoveCamera = true

    private val handler = Handler(Looper.getMainLooper())
    private val cameraMovedRunnable = Runnable { needMoveCamera = true }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoogleMapBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment: SupportMapFragment = childFragmentManager
            .findFragmentById(R.id.map_google) as SupportMapFragment
        mapFragment.getMapAsync(this)
        if (viewModel.gMap != null && viewModel.currentLocation != null) {
            viewModel.gMap!!.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    viewModel.currentLocation!!,
                    18f
                )
            )
        }

        // Получение текущего местоположения и перемещение камеры
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.location.collect {
                val cameraUpdate = CameraUpdateFactory
                    .newLatLngZoom(LatLng(it.latitude, it.longitude), 16f)

                if (needMoveCamera) {
                    if (needAnimateCamera) viewModel.gMap?.animateCamera(cameraUpdate)
                    else {
                        viewModel.gMap?.moveCamera(cameraUpdate)
                        needAnimateCamera = true
                    }
                }
                if (it.speed != 0f) {
                    binding.speed.visibility = View.VISIBLE
                    binding.speed.text = getString(R.string.speed, it.speed * 3.6)
                } else
                    binding.speed.visibility = View.GONE
            }
        }

        // Получение и отображение объектов в заданом (в репозитории) радиусе от текущего положения
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.sightsList.collect { showSightsAroundMyLocation(it) }
        }

        // Установка возможности скроллинга по карте
        binding.mapOverlay.setOnTouchListener { _, _ ->
            handler.removeCallbacks(cameraMovedRunnable)
            needMoveCamera = false
            handler.postDelayed(cameraMovedRunnable, 3_000)
            false
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.startLocation()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        needAnimateCamera = false
    }

    // Получение и установка карты
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.setMap(googleMap)

        with(googleMap.uiSettings) {
            this.isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
        }
        googleMap.isMyLocationEnabled = true

        googleMap.moveCamera(
            CameraUpdateFactory
                .newLatLngZoom(
                    if (viewModel.currentLocation != null)
                        viewModel.currentLocation!!
                    else YEKATERINBURG,
                    18f
                )
        )

        googleMap.setLocationSource(object : LocationSource {
            override fun activate(p0: LocationSource.OnLocationChangedListener) {
                viewModel.setLocationListener(p0)
            }

            override fun deactivate() {
                viewModel.setLocationListener(null)
            }
        })

        googleMap.setOnMarkerClickListener { marker ->
            viewModel.getSightInfo(marker.position)
            viewModel.setCurrentLocation(marker.position)
            findNavController().navigate(
                R.id.action_GMapFragment_to_fragmentCurrentSight,
                bundleOf(FragmentCurrentSight.CURRENT_SIGHT_KEY to marker.position)
            )
            true
        }
    }

    // Функция для отображения объектов на карте
    private fun showSightsAroundMyLocation(sights: List<Sight>) {
        sights.map { sight ->
            if (sight.name!!.isBlank()) createAssistNotification()
            viewModel.sightsMap[LatLng(sight.point.lat, sight.point.lon)] = sight.xid
            viewModel.gMap?.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.sight))
                    .anchor(0.5f, 0.5f)
                    .position(LatLng(sight.point.lat, sight.point.lon))
                    .title(sight.name)
            )?.showInfoWindow()
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun createAssistNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java)

        val pending = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivities(
                requireContext(),
                0,
                arrayOf(intent),
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivities(
                requireContext(),
                0,
                arrayOf(intent),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notification = NotificationCompat.Builder(requireContext(), App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_assist)
            .setContentTitle(getString(R.string.notification_sight_nameless_title))
            .setContentText(getString(R.string.notification_sight_nameless_text))
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(requireContext()).notify(App.NOTIFICATION_ID, notification)
    }

    companion object {
        private val YEKATERINBURG = LatLng(56.8379, 60.5971)
    }
}