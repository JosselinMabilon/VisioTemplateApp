package com.example.visioglobe.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.visioglobe.R
import com.example.visioglobe.databinding.FragmentHomeBinding
import com.example.visioglobe.polestar.MyNaoService
import com.example.visioglobe.viewmodel.HomeViewModel
import com.polestar.naosdk.api.external.*
import com.visioglobe.visiomoveessential.VMEMapView
import com.visioglobe.visiomoveessential.listeners.VMELifeCycleListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(), NAOLocationListener,
    NAOSensorsListener, NAOSyncListener {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var mLifeCycleListener: VMELifeCycleListener
    private lateinit var locationHandle: NAOLocationHandle
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var poi: String = ""
    private val naoApiKey: String = "XXXXXXXXXXXXXXXX"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        requirePermission()

        return view
    }

    private fun requirePermission() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (!isGranted) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.BLUETOOTH
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.enable_bluetooth),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.enable_location_authorization),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) == PackageManager.PERMISSION_DENIED
                    ) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.enable_location_authorization),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        requestLocationPermission()
    }


    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        mLifeCycleListener = object : VMELifeCycleListener() {
            override fun mapDidInitializeEngine(p0: VMEMapView?) {
                super.mapDidInitializeEngine(p0)
            }
        }

        binding.mapView.setLifeCycleListener(mLifeCycleListener)
        setUpLocation()
        binding.mapView.loadMap()
        onBackPressed()
    }

    private fun setUpLocation() {
        locationHandle = NAOLocationHandle(
            context,
            MyNaoService::class.java,
            naoApiKey,
            this,
            this
        )
        locationHandle.synchronizeData(this)
        locationHandle.start()
    }

    override fun onResume() {
        super.onResume()
        locationHandle.start()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        locationHandle.stop()
        binding.mapView.onPause()
    }

    private fun locatePOI() {
        val poi = poi
        val cameraUpdate = viewModel.setUpCamera(poi)
        binding.mapView.updateCamera(cameraUpdate)
        binding.mapView.showPlaceInfo(poi)
    }

    override fun onError(p0: NAOERRORCODE?, p1: String?) {
        Timber.tag("onError").d(p1.toString())
    }

    override fun onLocationChanged(location: Location?) {
        val vmeLocation = binding.mapView.createLocationFromLocation(location)
        binding.mapView.updateLocation(vmeLocation)
        locatePOI()
    }

    override fun onLocationStatusChanged(p0: TNAOFIXSTATUS?) {
        Timber.tag("statusChange").d(p0.toString())
    }

    override fun onEnterSite(p0: String?) {}

    override fun onExitSite(p0: String?) {}

    override fun requiresCompassCalibration() {}

    override fun requiresWifiOn() {
        Toast.makeText(
            requireContext(),
            getString(R.string.enable_wifi),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun requiresBLEOn() {
        Toast.makeText(
            requireContext(),
            getString(R.string.enable_bluetooth),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun requiresLocationOn() {
        Toast.makeText(
            requireContext(),
            getString(R.string.enable_location_authorization),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    override fun onSynchronizationSuccess() {
        //reloadMap()
        Timber.tag("onSynchroSuccess")
            .d("Sync OK ! The new files will be taken into account automatically by the NAO SDK, no need to restart it.")
    }

    override fun onSynchronizationFailure(p0: NAOERRORCODE?, p1: String?) {
        Timber.tag("onSynchroError").d(p1.toString())
    }
}