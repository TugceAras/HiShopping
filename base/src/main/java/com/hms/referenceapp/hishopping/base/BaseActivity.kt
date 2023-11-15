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
package com.hms.referenceapp.hishopping.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hms.lib.commonmobileservices.core.ResultData
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<T : BaseViewModel, B : ViewDataBinding> : AppCompatActivity() {
    abstract val layoutRes: Int

    abstract var viewLifeCycleOwner: LifecycleOwner

    @Suppress("UNCHECKED_CAST")
    val viewModel by lazy {
        val persistentViewModelClass = (this.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments.first() as Class<T>
        ViewModelProvider(this).get(persistentViewModelClass)
    }


    open fun initBinding() {
        this._binding?.lifecycleOwner = this
        viewLifeCycleOwner = this
    }

    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        _binding = DataBindingUtil.inflate(layoutInflater, layoutRes, null, false)
        setContentView(_binding!!.root)
        initBinding()
        observeLoadingAndError()
        observeViewModel()
    }

    abstract fun observeViewModel()

    private fun observeLoadingAndError() {
        viewModel.loadingErrorState.observe(this, Observer {
            when (it) {
                is ResultData.Loading -> {
                    showLoading()
                }
                is ResultData.Success -> {
                    hideLoading()
                }
                is ResultData.Failed -> {
                    hideLoading()
                    showErrorDialog(it.error)
                }
            }
        })
    }

    private val loadingAlertDialog by lazy {
        Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.dialog_loading_view)
            setCancelable(false)
        }
    }

    private fun showLoading() {
        loadingAlertDialog.show()
    }

    private fun hideLoading() {
        loadingAlertDialog.dismiss()
    }

    private fun showErrorDialog(message: String?, callback: () -> Unit = {}) {
        AlertDialog.Builder(this).apply {
            setTitle("Uyarı")
            setMessage(message)
            setPositiveButton("Kapat") { _, _ -> callback.invoke() }
        }.show()
    }
}