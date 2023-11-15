package com.hms.referenceapp.hishopping.app.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hms.lib.commonmobileservices.core.ResultData
import com.hms.referenceapp.hishopping.base.BaseViewModel
import com.hms.referenceapp.hishopping.data.clouddb.CloudDbDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Mustafa Kemal Özdemir on 3/18/2022.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val cloudDbDataSource: CloudDbDataSource
): BaseViewModel() {
    private val mSplashStateLiveData = MutableLiveData(SplashState.INITIAL)
    val splashStateLiveData: LiveData<SplashState>
        get() = mSplashStateLiveData

    fun init() {
        viewModelScope.launch {
            when(cloudDbDataSource.initialize()) {
                is ResultData.Loading -> Unit
                is ResultData.Success -> mSplashStateLiveData.value = SplashState.COMPLETED
                is ResultData.Failed -> mSplashStateLiveData.value = SplashState.ERROR
            }
        }
    }

}