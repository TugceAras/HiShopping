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
package com.hms.referenceapp.hishopping.app.mainactivity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.hms.lib.commonmobileservices.safety.RootDetectionResponse
import com.hms.lib.commonmobileservices.safety.SafetyService
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.setupWithNavController
import com.hms.referenceapp.hishopping.base.BaseActivity
import com.hms.referenceapp.hishopping.base.extensions.*
import com.hms.referenceapp.hishopping.databinding.ActivityMainBinding
import com.huawei.hms.aaid.HmsInstanceId
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    CrossNavBackStacksNavigator {

    override val layoutRes: Int = R.layout.activity_main
    override var viewLifeCycleOwner: LifecycleOwner = this


    private lateinit var navController: NavController

    private lateinit var mainToolbar: Toolbar
    private var safetyService: SafetyService? = null
    private var appKey: String = ""
    private var mPreferences: SharedPreferences? = null
    private var tokenPreferences: SharedPreferences? = null

    override fun observeViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("On_create")
        mainToolbar = binding.toolbarLayout.root
        setSupportActionBar(mainToolbar)
        getToken()


        val navGraphIds = listOf(
            R.navigation.navigation_explore,
            R.navigation.navigation_categories,
            R.navigation.navigation_my_cart,
            R.navigation.navigation_favorites,
            R.navigation.navigation_profile
        )

        binding.bottomNavView.setupWithNavController(
            navGraphIds,
            supportFragmentManager,
            R.id.nav_host_fragment,
            intent
        ).observe(this) {
            navController = it
            navController.addOnDestinationChangedListener { _, destination, _ ->
                arrangeToolbarAndBottomNavView(destination)
            }
        }

        mPreferences = this.getSharedPreferences(
            "rootDetection", android.app.Service.MODE_PRIVATE
        )
        tokenPreferences = this.getSharedPreferences(
            "device_token", Context.MODE_PRIVATE
        )

        safetyService = SafetyService.Factory.create(applicationContext)
        appKey = if (Device.getMobileServiceType(this) == MobileServiceType.GMS) {
            getString(R.string.google_device_verification_api_key)
        } else {
            getString(R.string.app_id)
        }

        val rootDetection = mPreferences?.getBoolean("rootDetection", false)
        if (!rootDetection!!) {
            safetyService?.rootDetection(
                appKey,
                object : SafetyService.SafetyRootDetectionCallback<RootDetectionResponse> {
                    override fun onFailRootDetect(e: Exception) {
//                        Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccessRootDetect(result: RootDetectionResponse?) {
                        if (result != null) {
                            if (result.basicIntegrity) {
                                mPreferences?.edit()?.putBoolean("rootDetection", true)?.apply()
                                showSecurityAlertMessage(
                                    getString(R.string.root_device_info),
                                    "Info",
                                    true
                                )
                            } else {
                                showSecurityAlertMessage(
                                    getString(R.string.error_no_root_device_error),
                                    "Security Warning",
                                    false
                                )
                            }
                        }
                    }
                })
        }


    }
    private fun getToken() {
        object : Thread() {
            override fun run() {
                try {
                    val appId = getString(R.string.app_id)
                    val pushToken = HmsInstanceId.getInstance(applicationContext).getToken(appId, "HCM")

                    if (!TextUtils.isEmpty(pushToken)) {
                        Log.i("PushActivity", "get token:${pushToken}")
                        tokenPreferences?.edit()?.putString("device_token",pushToken)?.apply()
                    }
                } catch (e: Exception) {
                    Log.i("PushActivity", "getToken failed, $e")
                }
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("On_pause")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("On_resume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("On_destroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.d("On_save_instance")
    }


    private fun arrangeToolbarAndBottomNavView(navDestination: NavDestination) {

        val mainScreenFragments = listOf(
            R.id.explore,
            R.id.categories,
            R.id.profile,
            R.id.my_cart,
            R.id.favorites
        )

        if (navDestination.id == R.id.tryWithArFragment) {
            mainToolbar.makeGone()
        } else {
            mainToolbar.makeVisible()
        }

        if (mainScreenFragments.contains(navDestination.id)) {
            binding.bottomNavView.makeVisible()
            (mainToolbar).apply {
                navigationIcon = null

                //setting end margin
                with(binding.toolbarLayout.imageViewCustomToolbarHeader) {
                    layoutParams = (layoutParams as
                            ViewGroup.MarginLayoutParams).apply { marginEnd = 0 }
                    requestLayout()
                }
                setContentInsetsAbsolute(0, 0)
            }
        } else {
            binding.bottomNavView.makeGone()
            (mainToolbar).apply {
                setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
                setContentInsetsAbsolute(0, 0)
                setNavigationOnClickListener { onBackPressed() }
                val navWidth = navigationIcon?.let {
                    abs(it.bounds.left - it.bounds.right)
                }?.takeIf { it > 0 } ?: 24.toPx()
                val endMargin =
                    contentInsetStartWithNavigation.takeIf { it != 0 } ?: navWidth + 2 * 16.toPx()
                //setting end margin
                with(binding.toolbarLayout.imageViewCustomToolbarHeader) {
                    layoutParams = (layoutParams as
                            ViewGroup.MarginLayoutParams).apply {
                        marginEnd = endMargin
                    }
                    requestLayout()
                }
                setContentInsetsAbsolute(0, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.firstOrNull { it is NavHostFragment }?.let {
            it.childFragmentManager.primaryNavigationFragment?.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
    }

    override fun navigateToMyOrders() {
        binding.bottomNavView.selectedItemId = R.id.my_orders_graph
    }

    override fun navigateToMyCart() {
        binding.bottomNavView.selectedItemId = R.id.my_cart_graph
    }

    fun showSecurityAlertMessage(message: String, title: String, root: Boolean) {
        this.let {
            AlertDialog.Builder(it).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton("OK") { _, _ -> if (!root) finish(); }
            }.show()
        }
    }

}

interface CrossNavBackStacksNavigator {
    fun navigateToMyOrders()
    fun navigateToMyCart()
}