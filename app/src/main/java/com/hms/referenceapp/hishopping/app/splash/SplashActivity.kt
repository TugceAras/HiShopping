package com.hms.referenceapp.hishopping.app.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.mainactivity.MainActivity
import com.hms.referenceapp.hishopping.base.BaseActivity
import com.hms.referenceapp.hishopping.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Created by Mustafa Kemal Özdemir on 3/18/2022.
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity: BaseActivity<SplashViewModel, ActivitySplashBinding>() {
    override val layoutRes: Int
        get() = R.layout.activity_splash

    override var viewLifeCycleOwner: LifecycleOwner = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("On_create")
        viewModel.init()
    }

    override fun observeViewModel() {
        viewModel.splashStateLiveData.observe(viewLifeCycleOwner) {
            when(it) {
                null -> Unit
                SplashState.INITIAL -> Unit
                SplashState.COMPLETED -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                SplashState.ERROR -> Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show()
            }
        }
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

}