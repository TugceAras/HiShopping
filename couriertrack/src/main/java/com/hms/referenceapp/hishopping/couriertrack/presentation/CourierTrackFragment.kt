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
package com.hms.referenceapp.hishopping.couriertrack.presentation

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.lib.commonmobileservices.mapkit.factory.CommonMap
import com.hms.lib.commonmobileservices.mapkit.model.CommonLatLng
import com.hms.lib.commonmobileservices.mapkit.model.CommonMarker
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.couriertrack.R
import com.hms.referenceapp.hishopping.couriertrack.databinding.FragmentCourierTrackBinding
import com.hms.referenceapp.hishopping.couriertrack.domain.model.CourierPosition
import com.hms.referenceapp.hishopping.data.model.Order
import com.huawei.agconnect.function.AGConnectFunction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourierTrackFragment : BaseFragment<CourierTrackViewModel, FragmentCourierTrackBinding>() {

    override val layoutRes: Int = R.layout.fragment_courier_track

    private val args: CourierTrackFragmentArgs by navArgs()
    private var tokenPreferences: SharedPreferences? = null
    private var pushToken: String? = null

    private lateinit var courierMarker: CommonMarker
    private lateinit var scooterBitmap: Bitmap
    private lateinit var userBitmap: Bitmap
    private lateinit var loadingDialog: LoadingDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog = LoadingDialog.Builder(requireContext()).build()
        initPushToken()
        binding.mapViewCourierTrack.onCreate(savedInstanceState, lifecycle).apply {
            getMapAsync {
                initMapImages()
                initMap(this)
                startCourierSimulation(this)
            }
        }
    }

    override fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            if (uiState.loading) loadingDialog.show() else loadingDialog.hide()
            if (uiState.orderUpdated){
                sendNotification()
                findNavController().popBackStack()
            }
        }
    }

    private fun startCourierSimulation(commonMap: CommonMap) {
        val courierObserver = viewModel.getCourierPosition(
            args.orderId,
            CommonLatLng(args.orderEntity.userLat, args.orderEntity.userLng),
            CommonLatLng(args.orderEntity.storeLat, args.orderEntity.storeLng)
        )
        courierObserver.observe(viewLifecycleOwner){ resultData ->
            if (resultData is ResultData.Success){
                resultData.data?.let { courierPosition ->
                    if (courierPosition.isArrived) {
                        courierObserver.removeObservers(viewLifecycleOwner)
                        updateOrder()
                    } else {
                        updateMap(commonMap,courierPosition)
                    }
                }
            }
        }
    }

    private fun updateOrder() {
        viewModel.makeOrderDelivered(
            args.orderEntity.copy(
                orderStatus = Order.OrderStatus.DELIVERED
            )
        )
    }

    private fun updateMap(commonMap: CommonMap,courierPosition: CourierPosition) {
        commonMap.apply {
            courierMarker.remove()
            animateCamera(courierPosition.currentPosition.lat, courierPosition.currentPosition.lng, 15f)
            courierMarker = addMarker(
                "",
                "",
                courierPosition.currentPosition.lat,
                courierPosition.currentPosition.lng,
                iconBitmap = scooterBitmap
            )
        }
    }

    private fun sendNotification() {
        pushToken?.let { token ->
            val parameterMap: HashMap<String, String> = HashMap()
            parameterMap["deviceToken"] = token
            AGConnectFunction.getInstance()
                .wrap("hishopping")
                .call(parameterMap)
        }
    }

    private fun initMap(commonMap: CommonMap) {
        commonMap.moveCamera(
            args.orderEntity.userLat,
            args.orderEntity.userLng,
            15f
        )
        commonMap.addMarker(
            "",
            "",
            args.orderEntity.userLat,
            args.orderEntity.userLng,
            iconBitmap = userBitmap
        )
        courierMarker = commonMap.addMarker(
            "",
            "",
            args.orderEntity.storeLat,
            args.orderEntity.storeLng,
            iconBitmap = scooterBitmap
        )
    }

    private fun initMapImages(){
        scooterBitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.scooter_icon),
            100,
            100,
            false
        )
        userBitmap = resources.getDrawable(R.drawable.ic_user_location).toBitmap(100,100)
    }

    private fun initPushToken() {
        tokenPreferences = activity?.getSharedPreferences(
            "device_token", Context.MODE_PRIVATE
        )
        pushToken = tokenPreferences?.getString("device_token",null)
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {}
}
