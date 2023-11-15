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
package com.hms.referenceapp.hishopping.trywithar

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hms.lib.commonmobileservices.scene.arview.ui.CommonArView
import com.hms.lib.commonmobileservices.scene.common.CommonData
import com.hms.lib.commonmobileservices.scene.common.CommonView
import com.hms.lib.commonmobileservices.scene.common.ViewType
import com.hms.lib.commonmobileservices.scene.faceview.ui.CommonAugmentedFaceView
import com.hms.lib.commonmobileservices.scene.sceneview.ui.CommonSceneView
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.trywithar.databinding.BottomSheetTryWithArDialogBinding
import com.hms.referenceapp.hishopping.trywithar.databinding.FragmentTryWithArBinding
import com.hms.referenceapp.hishopping.trywithar.dialog.TryWithArRecyclerViewAdapter
import com.hms.referenceapp.hishopping.trywithar.model.RenderParams
import com.hms.referenceapp.hishopping.trywithar.util.CommonDataTypeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TryWithArFragment :
    BaseFragment<TryWithArViewModel, FragmentTryWithArBinding>() {

    override var layoutRes: Int = R.layout.fragment_try_with_ar

    private var isLoaded: Boolean = false
    private lateinit var renderParams: RenderParams
    private lateinit var renderView: View
    private lateinit var oldViewType: ViewType
    private lateinit var propositionDialog: BottomSheetDialog
    private lateinit var model: SharedViewModel
    private lateinit var navController: NavController

    private val renderParamsList = arrayListOf<RenderParams>()

    private val args: TryWithArFragmentArgs by navArgs()

    private lateinit var bottomSheetBinding: BottomSheetTryWithArDialogBinding

    private lateinit var adapterTryWithArRecyclerView: TryWithArRecyclerViewAdapter


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        populateList()

        createBottomSheetDialog()

        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        renderParams = loadById(args.productId)

        //Set selected product as button icon for the first time. It will be updated in recyclerview's onclick
        binding.openMenu.load(renderParams.image)

        renderParams.params.viewType.let {
            renderView = initRenderView(it)

            setClickListeners()

            binding.relativeLayoutTryWithAr.addView(renderView, 0)

            //I use this variable to avoid reloading the view if the viewType of the item
            // selected from bottomsheet is the same as the previous one.
            oldViewType = it

            setButtonVisibility()
        }
    }

    private fun setButtonVisibility() {
        renderParams.params.viewType.let {
            when (it) {
                ViewType.AR_VIEW -> {
                    if (!isLoaded) {
                        binding.buttonTryWithArLoad.visibility = View.VISIBLE
                        binding.buttonTryWithArDetail.visibility = View.GONE
                        binding.cardViewTryWithArOpenMenu.visibility = View.VISIBLE
                        binding.buttonTryWithArClear.visibility = View.VISIBLE
                    } else {
                        binding.buttonTryWithArLoad.visibility = View.GONE
                        binding.buttonTryWithArDetail.visibility = View.VISIBLE
                        binding.cardViewTryWithArOpenMenu.visibility = View.VISIBLE
                        binding.buttonTryWithArClear.visibility = View.VISIBLE
                    }
                }
                ViewType.FACE_VIEW -> {
                    binding.buttonTryWithArLoad.visibility = View.VISIBLE
                    binding.buttonTryWithArDetail.visibility = View.GONE
                    binding.cardViewTryWithArOpenMenu.visibility = View.VISIBLE
                    binding.buttonTryWithArClear.visibility = View.VISIBLE
                }
                ViewType.SCENE_VIEW -> {
                    binding.buttonTryWithArLoad.visibility = View.GONE
                    binding.buttonTryWithArDetail.visibility = View.VISIBLE
                    binding.cardViewTryWithArOpenMenu.visibility = View.VISIBLE
                    binding.buttonTryWithArClear.visibility = View.GONE
                }
            }
        }
    }

    private fun initRenderView(viewType: ViewType): View {
        val tempRenderView: View
        when (viewType) {
            ViewType.AR_VIEW -> {
                tempRenderView = CommonArView(requireContext())
                tempRenderView.init(commonData = renderParams.params)
                makeToastMessage("Move the camera around yourself until the plane dots appear")
            }
            ViewType.FACE_VIEW -> {
                tempRenderView = CommonAugmentedFaceView(requireContext())
                tempRenderView.init(commonData = renderParams.params)
            }
            ViewType.SCENE_VIEW -> {
                tempRenderView = CommonSceneView(requireContext())
                tempRenderView.init(commonData = renderParams.params)
            }
        }
        return tempRenderView
    }

    private fun makeToastMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun createBottomSheetDialog() {
        //Bottom Sheet initialization
        view?.let {
            bottomSheetBinding = BottomSheetTryWithArDialogBinding
                .inflate(LayoutInflater.from(requireContext()), it as ViewGroup, false)

            val dialogView = bottomSheetBinding.root

            //Transparent background for bottom sheet dialog
            dialogView.setBackgroundColor(Color.TRANSPARENT)
            propositionDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
            propositionDialog.setContentView(dialogView)

            //Bottom sheet dialog horizontal recyclerview
            adapterTryWithArRecyclerView = TryWithArRecyclerViewAdapter(
                requireActivity(),
                renderParamsList
            )
            with(bottomSheetBinding.recyclerViewBottomSheetTryWithAr) {
                layoutManager = LinearLayoutManager(
                    this@TryWithArFragment.requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                adapter = adapterTryWithArRecyclerView
            }
            adapterTryWithArRecyclerView.onItemSelected = { position, item ->
                this@TryWithArFragment.renderParams = item
                binding.openMenu.load(renderParams.image)
                isLoaded = true
                if (renderParams.params.viewType != oldViewType) {
                    (renderView as CommonView).destroy()
                    renderView =
                        initRenderView(this@TryWithArFragment.renderParams.params.viewType)
                    (binding.relativeLayoutTryWithAr[0].parent as ViewGroup).removeViewAt(0)
                    oldViewType = renderParams.params.viewType
                    binding.relativeLayoutTryWithAr.addView(renderView, 0)
                    isLoaded = false
                    if (renderParams.params.viewType != ViewType.SCENE_VIEW)
                        (renderView as CommonView).onResume()
                } else {
                    (binding.relativeLayoutTryWithAr[0] as CommonView).init(renderParams.params)
                    (binding.relativeLayoutTryWithAr[0] as CommonView).load()
                }

                propositionDialog.hide()

                binding.openMenu.visibility = View.VISIBLE
                binding.buttonTryWithArClear.visibility = View.VISIBLE
                binding.cardViewTryWithArOpenMenu.visibility = View.VISIBLE

                setButtonVisibility()

            }
        }

    }

    private fun setClickListeners() {

        binding.openMenu.setOnClickListener {
            propositionDialog.show()
            binding.buttonTryWithArClear.visibility = View.GONE
            binding.buttonTryWithArLoad.visibility = View.GONE
            binding.buttonTryWithArDetail.visibility = View.GONE
            binding.cardViewTryWithArOpenMenu.visibility = View.GONE
        }

        binding.buttonTryWithArLoad.setOnClickListener {
            (renderView as CommonView).load()
            binding.openMenu.load(renderParams.image)
            isLoaded = true
            setButtonVisibility()
        }

        binding.buttonTryWithArClear.setOnClickListener {
            (renderView as CommonView).clear()
            // binding.buttonLoad.isEnabled = false
            binding.openMenu.setImageResource(R.drawable.ic_arrow_up_48)
            isLoaded = false
            setButtonVisibility()
        }

        binding.buttonTryWithArDetail.setOnClickListener {
            model.setProductId(renderParams.productId)
            navController = Navigation.findNavController(binding.root)
            navController.navigateUp()
        }

        propositionDialog.setOnCancelListener {
            propositionDialog.hide()
            setButtonVisibility()
        }


        bottomSheetBinding.imageViewBottomSheetTryWithArClose.setOnClickListener {
            propositionDialog.hide()
            setButtonVisibility()
        }
    }

    private fun loadById(productId: Int): RenderParams {
        return renderParamsList.first {
            it.productId == productId
        }
    }

    private fun populateList() {
        val jsonString =
            requireContext().resources.openRawResource(R.raw.ar_products).bufferedReader()
                .use { it.readText() }
        val typeToken = object : TypeToken<List<RenderParams>>() {}.type
        renderParamsList.addAll(
            GsonBuilder()
                .registerTypeAdapter(CommonData::class.java, CommonDataTypeAdapter())
                .create().fromJson<List<RenderParams>>(jsonString, typeToken)
        )
    }

    override fun onResume() {
        super.onResume()
        (renderView as CommonView).onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (renderView as CommonView).destroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        (renderView as CommonView).destroy()
    }

    override fun onPause() {
        super.onPause()
        (renderView as CommonView).onPause()
    }

    override fun observeViewModel() {}
}