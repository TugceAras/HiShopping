// See the License for the specific language governing permissions and
// limitations under the License.
//
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.hms.referenceapp.hishopping.address.presentation.map

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.location.CommonLocationClient
import com.hms.lib.commonmobileservices.location.factory.LocationFactory
import com.hms.lib.commonmobileservices.location.model.CommonLocationResult
import com.hms.lib.commonmobileservices.location.model.EnableGPSFinalResult
import com.hms.lib.commonmobileservices.location.model.LocationResultState
import com.hms.lib.commonmobileservices.mapkit.CommonMapView
import com.hms.lib.commonmobileservices.mapkit.factory.CommonMap
import com.hms.referenceapp.hishopping.address.R
import com.hms.referenceapp.hishopping.address.databinding.FragmentMapBinding
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.data.model.UserAddress
import com.huawei.hms.maps.MapView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>() {
    override var layoutRes: Int = R.layout.fragment_map

    private var locationClient: CommonLocationClient? = null
    private var map: CommonMap? = null

    private var markerY: Float = 0F
    private var address: UserAddress? = null

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState, lifecycle).apply {
            getMapAsync { commonMap ->
                map = commonMap
                locationClient =
                    LocationFactory.getLocationClient(requireActivity(), lifecycle)
                        ?.also { locationClient ->
                            locationClient.enableGps { enableGPSFinalResult, _ ->
                                when (enableGPSFinalResult) {
                                    EnableGPSFinalResult.ENABLED -> {
                                        locationEnabled(locationClient)
                                    }
                                    EnableGPSFinalResult.FAILED -> {
                                    }
                                    EnableGPSFinalResult.USER_CANCELLED -> {
                                    }
                                }
                            }
                        }
            }
        }

        binding.imageViewMapMyLocation.setOnClickListener {
            locationClient?.getLastKnownLocation { lastLocation ->
                when (lastLocation.state) {
                    LocationResultState.SUCCESS -> {
                        moveCameraToLocation(lastLocation)
                    }
                    LocationResultState.NO_LAST_LOCATION -> {
                        locationClient?.requestLocationUpdates { updatedLocation ->
                            when (updatedLocation.state) {
                                LocationResultState.SUCCESS -> {
                                    moveCameraToLocation(updatedLocation)
                                }
                                else -> {
                                }
                            }
                            locationClient?.removeLocationUpdates()
                        }
                    }
                    else -> {}
                }
            }
        }

        binding.buttonMapContinue.setOnClickListener {
            address?.let {
                findNavController().navigate(
                    MapFragmentDirections.actionMapFragmentToAddFragment(it)
                )
            }
        }
    }

    private fun locationEnabled(commonLocationClient: CommonLocationClient) {
        commonLocationClient.getLastKnownLocation { lastLocation ->
            when (lastLocation.state) {
                LocationResultState.SUCCESS -> {
                    moveCameraToLocation(lastLocation)
                    setupMap()
                }
                LocationResultState.NO_LAST_LOCATION -> {
                    commonLocationClient.requestLocationUpdates { updatedLocation ->
                        when (updatedLocation.state) {
                            LocationResultState.SUCCESS -> {
                                moveCameraToLocation(updatedLocation)
                                setupMap()
                            }
                            else -> {
                            }
                        }
                        locationClient?.removeLocationUpdates()
                    }
                }
                else -> {
                }
            }
        }
    }

    private fun moveCameraToLocation(locationResult: CommonLocationResult) {
        locationResult.location?.let { location ->
            map?.animateCamera(location.latitude, location.longitude, 18f)
        }
    }

    private fun setupMap() {
        map?.run {
            clear()
            setZoomControlsEnabled(false)
        }
        markerY = binding.imageViewMapSelectedLocation.y

        map?.setOnCameraIdleListener {

            val animatorY: ObjectAnimator =
                ObjectAnimator.ofFloat(binding.imageViewMapSelectedLocation, "y", markerY)
            animatorY.duration = 250
            animatorY.start()

            val animatorScaleX = ObjectAnimator.ofFloat(binding.imageIndicator, "scaleX", 0F)
            val animatorScaleY = ObjectAnimator.ofFloat(binding.imageIndicator, "scaleY", 0F)
            animatorScaleX.duration = 250
            animatorScaleY.duration = 250

            val animatorSetScale = AnimatorSet()
            animatorSetScale.play(animatorScaleX).with(animatorScaleY)
            animatorSetScale.start()

            val animatorFadeText = ObjectAnimator.ofFloat(binding.textViewMapAddress, "alpha", 1F)
            animatorFadeText.duration = 500
            animatorFadeText.start()

            val animatorFadeButton = ObjectAnimator.ofFloat(binding.buttonMapContinue, "alpha", 1F)
            animatorFadeButton.duration = 500
            animatorFadeButton.start()

            map?.getCameraPosition()?.target?.let { latLng ->
                context?.let {
                    viewModel.fetchAddress(it, latLng)
                }
            }
        }

        map?.setOnCameraMoveStartedListener {

            val animatorY: ObjectAnimator =
                ObjectAnimator.ofFloat(binding.imageViewMapSelectedLocation, "y", markerY - 50)
            animatorY.duration = 250
            animatorY.start()

            val animatorScaleX = ObjectAnimator.ofFloat(binding.imageIndicator, "scaleX", 1F)
            val animatorScaleY = ObjectAnimator.ofFloat(binding.imageIndicator, "scaleY", 1F)
            animatorScaleX.duration = 250
            animatorScaleY.duration = 250

            val animatorSetScale = AnimatorSet()
            animatorSetScale.play(animatorScaleX).with(animatorScaleY)
            animatorSetScale.start()

            val animatorFadeText = ObjectAnimator.ofFloat(binding.textViewMapAddress, "alpha", 0F)
            animatorFadeText.duration = 500
            animatorFadeText.start()

            val animatorFadeButton = ObjectAnimator.ofFloat(binding.buttonMapContinue, "alpha", 0F)
            animatorFadeButton.duration = 500
            animatorFadeButton.start()
        }
    }

    override fun observeViewModel() {
        viewModel.address.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Success -> {
                    it.data?.let { address ->
                        this.address = address
                        address.addressLine?.let {
                            binding.buttonMapContinue.isEnabled = true
                            binding.textViewMapAddress.text = address.addressLine
                        }?:run {
                            binding.buttonMapContinue.isEnabled = false
                            binding.textViewMapAddress.text = ""
                        }
                    }
                }
                is ResultData.Failed -> {
                    this.address = null
                    binding.buttonMapContinue.isEnabled = false
                    binding.textViewMapAddress.text = ""
                }
                is ResultData.Loading -> {
                }
            }
        }
    }
}