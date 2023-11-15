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
package com.hms.referenceapp.hishopping.app.search.presentation

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.lib.commonmobileservices.core.handleSuccess
import com.hms.lib.commonmobileservices.scan.manager.HuaweiGoogleScanManager
import com.hms.lib.commonmobileservices.speechtotext.manager.HuaweiGoogleSpeechToTextManager
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.mainactivity.MainActivity
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.customToast
import com.hms.referenceapp.hishopping.base.extensions.hideKeyboard
import com.hms.referenceapp.hishopping.databinding.FragmentSearchBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    override var layoutRes: Int = R.layout.fragment_search
    private val recordAudioResultCode = 203
    private val scanBarcodeResultCode = 204

    override fun observeViewModel() {}

    private lateinit var adapterSearchProductRecyclerView: SearchProductRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        sharedElementEnterTransition = TransitionInflater.from(this.requireContext())
            .inflateTransition(android.R.transition.move)
    }

    override fun arrangeUI() {

        arrangeSearchRecyclerView()

        binding.textViewSearch.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(binding.textViewSearch.text.toString())
                handled = true
                requireActivity().hideKeyboard()
            }
            handled
        }

        binding.imageViewSearchIcon.setOnClickListener {
            performSearch(binding.textViewSearch.text.toString())
        }

        with(binding.recyclerViewSearch) {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterSearchProductRecyclerView
        }

        binding.imageViewSearchInnerBarcodeIcon.setOnClickListener {
            this.requireActivity().runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                HuaweiGoogleScanManager(this.requireActivity()).performScan(
                    this.requireActivity(),
                    scanBarcodeResultCode
                )
            }
        }
        binding.imageViewSearchCameraIcon.setOnClickListener {
            (this.requireActivity() as? MainActivity)?.customToast(getString(R.string.error_search_feature_not_available))
        }
        binding.imageViewSearchMicrophoneIcon.setOnClickListener {
            this.requireActivity().runWithPermissions(Manifest.permission.RECORD_AUDIO) {
                HuaweiGoogleSpeechToTextManager(this.requireActivity()).performSpeechToText(
                    this.requireActivity(),
                    recordAudioResultCode,
                    "en-US",
                    this.requireContext().getString(R.string.hms_api_key)
                )
            }
        }
        binding.imageViewSearchBarcodeIcon.setOnClickListener {
            this.requireActivity().runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                HuaweiGoogleScanManager(this.requireActivity()).performScan(
                    this.requireActivity(),
                    scanBarcodeResultCode
                )
            }
        }
    }

    private fun arrangeSearchRecyclerView() {
        adapterSearchProductRecyclerView = SearchProductRecyclerViewAdapter(mutableListOf())
        adapterSearchProductRecyclerView.onItemSelected =
            { productId, imageView, transitionName ->
                val extras = FragmentNavigatorExtras(
                    imageView to transitionName
                )
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchToDetailNavGraph(
                        productId,
                        transitionName
                    ),
                    extras
                )
            }
    }

    private fun performSearch(searchKeyWord: String) {
        observeResultLiveData(viewModel.getSearchProduct(searchKeyWord.toLowerCase(Locale.getDefault()))) {
            it?.let { adapterSearchProductRecyclerView.updateDataSource(it.toMutableList()) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == recordAudioResultCode && data != null) {
            HuaweiGoogleSpeechToTextManager(this.requireActivity()).parseSpeechToTextData(
                {
                    it.handleSuccess {
                        binding.textViewSearch.append(it.data ?: "")
                        performSearch(it.data ?: "")
                    }
                }, this.requireActivity(), data, resultCode
            )
        }

        if (requestCode == scanBarcodeResultCode && data != null) { // When run scan kit from search fragment, activity's onActivityResult called by Scan Kit for some reason(?).
            HuaweiGoogleScanManager(this.requireActivity()).parseScanToTextData({
                it.handleSuccess {
                    binding.textViewSearch.setText(it.data ?: "")
                    performSearch(it.data ?: "")
                }
            }, this.requireActivity(), data)
        }
    }

}