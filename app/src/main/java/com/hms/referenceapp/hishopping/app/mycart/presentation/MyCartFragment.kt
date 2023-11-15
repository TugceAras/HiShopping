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
package com.hms.referenceapp.hishopping.app.mycart.presentation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.mainactivity.CrossNavBackStacksNavigator
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.makeGone
import com.hms.referenceapp.hishopping.base.extensions.makeVisible
import com.hms.referenceapp.hishopping.databinding.FragmentMyCartBinding
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging
import com.huawei.agconnect.remoteconfig.AGConnectConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCartFragment : BaseFragment<MyCartViewModel, FragmentMyCartBinding>() {

    override var layoutRes: Int = R.layout.fragment_my_cart
    private lateinit var abconfig: AGConnectConfig
    private lateinit var appMessaging: AGConnectAppMessaging
    private var mPreferences: SharedPreferences? = null

    private lateinit var adapterMyCartRecyclerView: MyCartRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        arrangeMyCartRecyclerView()

        mPreferences = context?.getSharedPreferences(
            "showInAppMessage", android.app.Service.MODE_PRIVATE
        )

        observeResultLiveData(viewModel.getMyCarts()) {
            it?.let {
                adapterMyCartRecyclerView.updateDataSource(it.toMutableList())
                if (it.isNotEmpty()) {
                    binding.recyclerViewMyCart.makeVisible()
                    binding.floatingActionButtonMyCartBuy.makeVisible()
                } else {
                    binding.recyclerViewMyCart.makeGone()
                    binding.floatingActionButtonMyCartBuy.makeGone()
                }
            }
        }

        binding.floatingActionButtonMyCartBuy.setOnClickListener {
            SapDialog(requireActivity()).apply {
                titleText = getString(R.string.buy)
                messageText = getString(R.string.my_cart_are_you_sure_to_buy_all_items)
                animResource = R.raw.sap_warning_anim
                loopAnimation = false
                isCancellable = false
                addPositiveButton(getString(R.string.buy)) {

//                    observeResultLiveData(viewModel.addOrders(adapterMyCartRecyclerView.gatherMyCartList())){
//                        observeResultLiveData(viewModel.deleteCartItems(adapterMyCartRecyclerView.gatherMyCartList())){
//                            adapterMyCartRecyclerView.updateDataSource(mutableListOf())
//                            findNavController().popBackStack(R.id.my_cart_graph,true)
//                            (requireActivity() as? CrossNavBackStacksNavigator)?.navigateToMyOrders()
//                            findNavController().navigate(MyCartFragmentDirections.actionMyCartToMyOrdersGraph())
//                        }
//                    }
                }
                addNegativeButton(getString(R.string.courier_track_cancel)) {}
            }.build().show()
        }

        abconfig = AGConnectConfig.getInstance()
        abconfig.applyDefault(R.xml.ab_testing)

        appMessaging = AGConnectAppMessaging.getInstance()
        appMessaging.isFetchMessageEnable = true
        appMessaging.isDisplayEnable = true
        appMessaging.setForceFetch()

        val showInAppMessage = mPreferences?.getBoolean("showInAppMessage", false)
        if (!showInAppMessage!!) {
            fetch()
        }
    }

    private fun arrangeMyCartRecyclerView() {
        adapterMyCartRecyclerView = MyCartRecyclerViewAdapter(mutableListOf())
        with(binding.recyclerViewMyCart) {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = adapterMyCartRecyclerView
        }
    }

    private fun fetch() {
        abconfig.fetch(0).addOnSuccessListener {
            abconfig.apply(it)
            when (abconfig.getValueAsString("TechOrSport")) {
                "Technology" -> {
                    triggerMessage("Technology")
                    mPreferences?.edit()?.putBoolean("showInAppMessage", true)?.apply()
                }
                "Sports" -> {
                    triggerMessage("Sports")
                    mPreferences?.edit()?.putBoolean("showInAppMessage", true)?.apply()
                }
                else -> {
                    Toast.makeText(context, "Other", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun triggerMessage(message: String) {
        appMessaging.trigger(message)
    }

    override fun observeViewModel() {}
}