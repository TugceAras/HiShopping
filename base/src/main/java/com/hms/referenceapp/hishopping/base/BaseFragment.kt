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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.hms.lib.commonmobileservices.core.ResultData
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment() {

    abstract val layoutRes: Int

    @Suppress("UNCHECKED_CAST")
    val viewModel by lazy {
        val persistentViewModelClass = (this.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments.first() as Class<T>
        ViewModelProvider(this).get(persistentViewModelClass)
    }

    open fun initBinding() {

    }

    abstract fun observeViewModel()
    abstract fun viewCreated(view: View, savedInstanceState: Bundle?)

    open fun arrangeUI() {

    }

    open fun gatherArgs() {

    }

    private var _binding: B? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        initBinding()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gatherArgs()
        viewCreated(view, savedInstanceState)
        arrangeUI()
        observeLoadingAndError()
        observeViewModel()
    }

    private fun observeLoadingAndError() {
        viewModel.loadingErrorState.observe(viewLifecycleOwner) {
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
                else -> {
                    hideLoading()
                }
            }
        }
    }

    private val loadingAlertDialog by lazy {
        context?.let {
            Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(R.layout.dialog_loading_view)
                setCancelable(false)
            }
        }
    }

    private fun showLoading() {
        loadingAlertDialog?.show()
    }

    private fun hideLoading() {
        loadingAlertDialog?.dismiss()
    }

    private fun showErrorDialog(message: String?, callback: () -> Unit = {}) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Uyarı")
                setMessage(message)
                setPositiveButton("Kapat") { _, _ -> callback.invoke() }
            }.show()
        }
    }

    protected fun <T> observeResultLiveData(
        liveData: LiveData<ResultData<T>>,
        onSuccessCallback: ((data: T?) -> Unit)? = null
    ): Observer<ResultData<T>> {
        return liveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultData.Loading -> {
                    showLoading()
                }
                is ResultData.Success -> {
                    hideLoading()
                    onSuccessCallback?.invoke(it.data)
                }
                is ResultData.Failed -> {
                    hideLoading()
                    showErrorDialog(it.error)
                }
            }
        }
    }

    fun showSuccessDialog(
        title: String?,
        buttonText: String?,
        message: String?,
        callback: () -> Unit = {}
    ) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle(title)
                setMessage(message)
                setPositiveButton(buttonText) { _, _ -> callback.invoke() }
            }.show()
        }
    }


}