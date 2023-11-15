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
package com.hms.referenceapp.hishopping.shoplocations.presentation

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hms.lib.commonmobileservices.location.CommonLocationClient
import com.hms.lib.commonmobileservices.location.Constants
import com.hms.lib.commonmobileservices.location.factory.LocationFactory
import com.hms.lib.commonmobileservices.location.model.CommonLocationResult
import com.hms.lib.commonmobileservices.location.model.EnableGPSFinalResult
import com.hms.lib.commonmobileservices.location.model.LocationResultState
import com.hms.lib.commonmobileservices.mapkit.factory.CommonMap
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.showToast
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.shoplocations.R
import com.hms.referenceapp.hishopping.shoplocations.databinding.FragmentShopLocationBinding
import com.hms.referenceapp.hishopping.shoplocations.utils.saveData
import com.huawei.hms.analytics.HiAnalytics
import com.hms.referenceapp.hishopping.shoplocations.presentation.model.StoreItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopLocationFragment : BaseFragment<ShopLocationViewModel, FragmentShopLocationBinding>() {

    override val layoutRes: Int = R.layout.fragment_shop_location

    private lateinit var commonMap: CommonMap
    private var locationClient: CommonLocationClient? = null
    private var currentDialog: Dialog? = null
    private var loadingDialog: LoadingDialog? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<CardView>

    private val args: ShopLocationFragmentArgs by navArgs()

    private lateinit var storeRecyclerViewAdapter: StoreRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        loadingDialog = LoadingDialog.Builder(requireContext()).build()
        setupRecyclerView()
        binding.mapViewStores.onCreate(savedInstanceState, lifecycle).apply {
            getMapAsync {
                commonMap = it
                locationClient =
                    LocationFactory.getLocationClient(requireActivity(), lifecycle)?.also {
                        it.enableGps { enableGPSFinalResult, _ ->
                            when (enableGPSFinalResult) {
                                EnableGPSFinalResult.ENABLED -> {
                                    locationEnabled(it)
                                }
                                EnableGPSFinalResult.FAILED -> showToast("GPS Enabling Failed")
                                EnableGPSFinalResult.USER_CANCELLED -> showToast("GPS Enabling Cancelled")
                            }
                        }
                    }
            }
        }
        bottomSheetBehavior =
            BottomSheetBehavior.from(binding.storesBottomSheetLayout.bottomSheetCardView).apply {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }

        binding.storesBottomSheetLayout.imageViewStoresDragIcon.setOnClickListener {
            arrangeBottomSheetPosition()
        }
        binding.storesBottomSheetLayout.textViewStoresTitle.apply {
            text = when (args.locationType) {
                LocationType.NEAREST_SHOPS -> getString(R.string.shop_location_nearest_shops)
                LocationType.PRODUCT_SHOPS -> getString(R.string.shop_location_product_available_shops)
            }
            setOnClickListener {
                arrangeBottomSheetPosition()
            }
        }
    }

    private fun locationEnabled(commonLocationClient: CommonLocationClient) {
        commonLocationClient.getLastKnownLocation { locationResult ->
            when (locationResult.state) {
                LocationResultState.SUCCESS -> {
                    locationSuccess(locationResult)
                }
                LocationResultState.NO_LAST_LOCATION -> {
                    commonLocationClient.requestLocationUpdates {
                        when (it.state) {
                            LocationResultState.SUCCESS ->
                                locationSuccess(it)
                            else -> showToast(resources.getString(R.string.error_shop_location_track_gps_location_fail))
                        }
                        locationClient?.removeLocationUpdates()
                    }
                }
                else -> showToast(
                    locationResult.exception?.message
                        ?: resources.getString(R.string.error_shop_location_track_gps_location_fail)
                )
            }
        }
    }

    private fun arrangeBottomSheetPosition() {
        when (bottomSheetBehavior.state) {
            BottomSheetBehavior.STATE_HALF_EXPANDED ->
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_EXPANDED ->
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            BottomSheetBehavior.STATE_DRAGGING -> {}
            BottomSheetBehavior.STATE_HIDDEN -> {}
            BottomSheetBehavior.STATE_SETTLING -> {}
        }
    }

    private fun locationSuccess(locationResult: CommonLocationResult) {
        commonMap.run {
            clear()
            setMyLocationEnabled(true, requireContext())
            setMyLocationButtonEnabled(true)
        }

        locationResult.location?.let { location ->
            commonMap.moveCamera(location.latitude, location.longitude, 15f)
            viewModel.getStores(location,args.locationType)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.OPEN_LOCATION_SETTING_REQUEST_CODE)
            locationClient?.run {
                handleResolutionResult(resultCode)
            }
    }

    private fun addAllStoreMarkers(shops: List<StoreItem>) {
        shops.forEach {
            val ir = ImageRequest.Builder(requireContext()).data(it.photoUrl).target { drawable ->
                (drawable as? BitmapDrawable)?.let { bd ->
                    commonMap.addMarker(
                        title = it.name,
                        snippet = it.addressLine,
                        latitude = it.lat,
                        longitude = it.lon,
                        iconBitmap = bd.bitmap
                    )
                }
            }.size(100, 100).transformations(CircleCropTransformation()).allowHardware(false)
                .build()
            requireContext().imageLoader.enqueue(ir)
        }
        commonMap.setOnInfoWindowClickListener { markerTitle, markerSnippet, commonLatLng ->

            markerTitle?.let {
                HiAnalytics.getInstance(requireContext()).saveData(
                    "ShopMapMarkerClicked",
                    "location",
                    (commonLatLng.lat to commonLatLng.lng).toString(),
                    "markerTitle",
                    it,
                    "markerSnippet",
                    markerSnippet ?: ""
                )
            }
            currentDialog?.dismiss()
            currentDialog = SapDialog(requireActivity()).apply {
                titleText = markerTitle
                messageText = getString(R.string.shop_location_do_you_want_to_navigate)
                addPositiveButton(getString(R.string.shop_location_navigate)) {
                    navigateToShop(commonLatLng)
                }
                animResource = R.raw.navigation_animation
                loopAnimation = true
                isCancellable = true
            }.build().show()
        }
    }

    private fun navigateToShop(commonLatLng: CommonLatLng) {
        val res = NavigationAppOpener(requireContext()).open(commonLatLng)
        if (!res.opened) {
            currentDialog?.dismiss()
            currentDialog = SapDialog(requireActivity()).apply {
                titleText = getString(R.string.error)
                messageText = res.exception?.message
                animResource = R.raw.sap_error_anim
                loopAnimation = false
                isCancellable = true
            }.build().show()
        }
    }

    private fun setupRecyclerView() {
        storeRecyclerViewAdapter = StoreRecyclerViewAdapter()
        with(binding.storesBottomSheetLayout.recyclerViewStores){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = storeRecyclerViewAdapter
        }
        storeRecyclerViewAdapter.setOnItemClickListener {
            HiAnalytics.getInstance(requireContext()).saveData(
                "ShopMapListItemClicked",
                "location",
                (it.lat to it.lon).toString(),
                "distance",
                it.distanceText
            )
            //arrange bottom sheet position
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

            commonMap.animateCamera(it.lat,it.lon,15f)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentDialog?.dismiss()
    }

    override fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            storeRecyclerViewAdapter.differ.submitList(uiState.storeItemList)
            addAllStoreMarkers(uiState.storeItemList)
            if (uiState.loading) loadingDialog?.show()
            else loadingDialog?.hide()
        }
    }
}


