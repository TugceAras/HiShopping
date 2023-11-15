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
package com.hms.referenceapp.hishopping.app.details.detailbody.presentation

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.cardview.widget.CardView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import coil.load
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.details.comments.presentation.CommentsRecyclerViewAdapter
import com.hms.referenceapp.hishopping.app.mainactivity.CrossNavBackStacksNavigator
import com.hms.referenceapp.hishopping.app.mainactivity.MainActivity
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.*
import com.hms.referenceapp.hishopping.data.model.CartItem
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import com.hms.referenceapp.hishopping.databinding.FragmentProductDetailsBinding
import com.hms.referenceapp.hishopping.databinding.ProductDetailBottomSheetBinding
import com.hms.referenceapp.hishopping.shoplocations.presentation.LocationType
import com.hms.referenceapp.hishopping.trywithar.SharedViewModel
import com.jaredrummler.android.device.DeviceName
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import dagger.hilt.android.AndroidEntryPoint
import kotlin.reflect.KMutableProperty0

@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel, FragmentProductDetailsBinding>(),
    AdapterView.OnItemSelectedListener {

    override var layoutRes: Int = R.layout.fragment_product_details

    private lateinit var sharedViewModel: SharedViewModel
    private var isDescriptionExpanded: Boolean = false
    private var isPropertiesExpanded: Boolean = false
    private var isCommentsExpanded: Boolean = false
    private var isCategoriesExpanded: Boolean = false
    private var showDialog: Boolean = false

    private val args: DetailsFragmentArgs by navArgs()

    private var productId: Int = -1

    private val ALPHA_THRESHOLD = 0.02f

    private val RADIUS = 110.0f

    private var bottomSheetBehavior: BottomSheetBehavior<CardView>? = null
    private var adapterCommentsRecyclerView: CommentsRecyclerViewAdapter? = null

    private var targetLangCode = "zh"

    private val productSliderItemList = arrayListOf<String>()

    private lateinit var productIntroViewPagerAdapter: ProductIntroViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.imageViewProductDetails.transitionName = args.animationId
        super.onViewCreated(view, savedInstanceState)
    }

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        sharedViewModel.productId.observe(viewLifecycleOwner) {
            if (it != -1) {
                productId = it
                viewModel.loadProductDetails(productId)
                viewModel.loadCommentsById(productId)
            }
        }
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
                .addListener(object : Transition.TransitionListener {
                    override fun onTransitionEnd(transition: Transition?) {
                        binding.viewPagerProductDetailsProductIntro.makeVisible() // viewpager doesnt fit to transition animation thats why we use temporary product imageview. After animation finish viewpager would be visible.
                        binding.imageViewProductDetails.makeGone()
                        binding.imageViewProductDetails.alpha = 0f
                    }

                    override fun onTransitionResume(transition: Transition?) {}

                    override fun onTransitionPause(transition: Transition?) {}

                    override fun onTransitionCancel(transition: Transition?) {}

                    override fun onTransitionStart(transition: Transition?) {}
                })
        //Initial view setting
        binding.detailBottomSheet.imageViewProductDetailTryWithArIcon.visibility = View.GONE
        binding.shadow.alpha = 0.0f
        binding.bottomSheetProductDetails.radius = RADIUS
        //I have set the visibility gone because the product image should be in the foreground and clickable when the screen is first opened.
        // Below I am changing this again in the onSlide method of BottomSheetCallback
        binding.shadow.visibility = View.GONE
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetProductDetails)

        //Listener to listen the Standard BottomSheetDialog behaviours
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, state: Int) {
                /*implementation*/
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //Set shadow animation
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    binding.shadow.alpha = slideOffset
                    if (binding.shadow.alpha <= ALPHA_THRESHOLD) {
                        binding.shadow.visibility = View.GONE
                    } else {
                        binding.shadow.visibility = View.VISIBLE
                    }
                    //Set BottomSheetDialog corner radius animation
                    binding.bottomSheetProductDetails.radius = RADIUS - RADIUS * slideOffset
                }
            }
        })

        arrangeBottomSheet(binding.detailBottomSheet)

        //Get productId to load product data
        productId = args.productId
        viewModel.loadProductDetails(productId)
        viewModel.loadCommentsById(productId)
        viewModel.getUsersLikeList()
        binding.imageViewProductDetails.transitionName = args.animationId



        binding.viewPagerProductDetailsProductIntro.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                /*implementation*/
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                /*implementation*/
            }

            override fun onPageSelected(position: Int) {
                binding.pageIndicatorViewProductDetails.selection = position
            }
        })
    }

    private fun arrangeBottomSheet(productDetailBottomSheet: ProductDetailBottomSheetBinding) {
        // To prevent the BottomSheetDialog from collapsing before the scroll view reaches top
        productDetailBottomSheet.scrollViewDetails.viewTreeObserver.addOnScrollChangedListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
                bottomSheetBehavior?.isDraggable =
                    binding.detailBottomSheet.scrollViewDetails.scrollY == 0
        }

        productDetailBottomSheet.scrollViewDetails.viewTreeObserver.addOnScrollChangedListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
                bottomSheetBehavior?.isDraggable =
                    binding.detailBottomSheet.scrollViewDetails.scrollY == 0
        }

        //To navigate to TryWithArFragment to try ar products
        productDetailBottomSheet.imageViewProductDetailTryWithArIcon.setOnClickListener {
            //Camera permission check
            this.requireActivity().runWithPermissions(Manifest.permission.CAMERA) {
                val action = DetailsFragmentDirections.actionDetailsToTryWithArNavGraph(productId)
                findNavController().navigate(action)
            }
        }

        //Changed favorite icon
        productDetailBottomSheet.imageViewProductDetailFavoriteIcon.setOnClickListener(object :
            View.OnClickListener {
            var isAnimated = false
            override fun onClick(v: View) {
                isAnimated = if (!isAnimated) {
                    binding.detailBottomSheet.imageViewProductDetailFavoriteIcon.setImageResource(R.drawable.ic_red_favorite_24)
                    (requireActivity() as? MainActivity)?.customToast(getString(R.string.error_search_feature_not_available))
                    true
                } else {
                    binding.detailBottomSheet.imageViewProductDetailFavoriteIcon.setImageResource(R.drawable.ic_favorite_border)
                    false
                }
            }
        })

        productDetailBottomSheet.buttonProductDetailAddBasket.setOnClickListener {
            showDialog = true
            observeResultLiveData(viewModel.productDetailsLiveData) {
                if (showDialog) {
                    showDialog = false
                    it?.let {
                        observeResultLiveData(
                            viewModel.addToBasket(
                                CartItem(
                                    -1,
                                    it.title,
                                    it.price,
                                    it.description,
                                    it.category,
                                    it.image[0],
                                    it.id
                                )
                            )
                        ) {
                            SapDialog(requireActivity()).apply {
                                titleText = getString(R.string.congratulations)
                                messageText = getString(R.string.your_order_added_cart)
                                animResource = R.raw.done_anim
                                loopAnimation = false
                                isCancellable = true
                                addPositiveButton(getString(R.string.go_to_card)) {
                                    findNavController().popBackStack()
                                    (requireActivity() as? CrossNavBackStacksNavigator)?.navigateToMyCart()
                                }
                                addNegativeButton(getString(R.string.continue_shopping)) {}
                            }.build().show()
                        }
                    }
                }
            }
        }

        productDetailBottomSheet.imageViewProductDetailNearStoreIcon.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsToShopslocation(
                LocationType.PRODUCT_SHOPS,
                productId
            )
            findNavController().navigate(action)
        }

        productDetailBottomSheet.textViewProductDetailAllComments.setOnClickListener {
            collapse(
                binding.detailBottomSheet.frameLayoutProductDetailComment,
                binding.detailBottomSheet.imageViewProductDetailExpandComments
            )
            isCommentsExpanded = false
            val action = DetailsFragmentDirections.actionDetailsToCommentNavGraph(productId)
            findNavController().navigate(action)
        }

        productDetailBottomSheet.buttonProductDetailAddComment.setOnClickListener {
            collapse(
                binding.detailBottomSheet.frameLayoutProductDetailComment,
                binding.detailBottomSheet.imageViewProductDetailExpandComments
            )
            isCommentsExpanded = false
            val action = DetailsFragmentDirections.actionDetailToAddCommentNavGraph(productId)
            findNavController().navigate(action)
        }

        productDetailBottomSheet.spinnerProductDetailCommentLanguage.onItemSelectedListener = this
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            LANGUAGE_ARRAY
        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.detailBottomSheet.spinnerProductDetailCommentLanguage.adapter = spinnerAdapter

        //Click listeners to set collapsing and expanding animations
        setDetailBottomSheetClickListeners()
    }

    private fun setDetailBottomSheetClickListeners() {
        //Click description linearlayout to expand description with animation
        detailBottomSheetClickSetter(
            binding.detailBottomSheet.constraintLayoutProductDetailDescription,
            ::isDescriptionExpanded,
            {
                expand(
                    binding.detailBottomSheet.textViewProductDetailAddDescription,
                    binding.detailBottomSheet.imageViewProductDetailExpandDescription
                )
            }) {
            collapse(
                binding.detailBottomSheet.textViewProductDetailAddDescription,
                binding.detailBottomSheet.imageViewProductDetailExpandDescription
            )
        }
        //Click properties linearlayout to expand description with animation
        detailBottomSheetClickSetter(
            binding.detailBottomSheet.constraintLayoutProductDetailProperties,
            ::isPropertiesExpanded,
            {
                expand(
                    binding.detailBottomSheet.textViewProductDetailProductProperties,
                    binding.detailBottomSheet.imageViewProductDetailExpandProperties
                )
            }) {
            collapse(
                binding.detailBottomSheet.textViewProductDetailProductProperties,
                binding.detailBottomSheet.imageViewProductDetailExpandProperties
            )
        }
        //Click comments linearlayout to expand description with animation
        detailBottomSheetClickSetter(
            binding.detailBottomSheet.constraintLayoutProductDetailComment,
            ::isCommentsExpanded,
            {
                expand(
                    binding.detailBottomSheet.frameLayoutProductDetailComment,
                    binding.detailBottomSheet.imageViewProductDetailExpandComments
                )
                if (viewModel.lastCommentsSize < 3) {
                    binding.detailBottomSheet.textViewProductDetailAllComments.makeInvisible()
                }
                if (viewModel.lastCommentsSize == 0) {
                    binding.detailBottomSheet.spinnerProductDetailCommentLanguage.makeInvisible()
                }
            }) {
            collapse(
                binding.detailBottomSheet.frameLayoutProductDetailComment,
                binding.detailBottomSheet.imageViewProductDetailExpandComments
            )
        }
        //Click categories linearlayout to expand description with animation
        detailBottomSheetClickSetter(
            binding.detailBottomSheet.constraintLayoutProductDetailCategories,
            ::isCategoriesExpanded,
            {
                expand(
                    binding.detailBottomSheet.textViewProductDetailCategory,
                    binding.detailBottomSheet.imageViewProductDetailExpandCategories
                )
            }) {
            collapse(
                binding.detailBottomSheet.textViewProductDetailCategory,
                binding.detailBottomSheet.imageViewProductDetailExpandCategories
            )
        }
    }

    private fun detailBottomSheetClickSetter(
        view: View,
        bool: KMutableProperty0<Boolean>,
        notExpendedBlock: () -> Unit,
        expendedBlock: () -> Unit
    ) {
        view.setOnClickListener {
            if (!bool.get()) {
                notExpendedBlock.invoke()
            } else {
                expendedBlock.invoke()
            }
            bool.set(!bool.get())
        }
    }


    private fun checkDeviceSupport(): Boolean {
        DeviceName.init(requireContext())
        return Device.getMobileServiceType(requireContext()) == MobileServiceType.HMS
                && AR_VIEW_SUPPORTED_DEVICES.contains(
            DeviceName.getDeviceName().removePrefix("HUAWEI ")
        )
    }

    override fun observeViewModel() {
        observeResultLiveData(viewModel.productDetailsLiveData) {
            it?.let {
                if (it.arSupported && checkDeviceSupport())
                    binding.detailBottomSheet.imageViewProductDetailTryWithArIcon.visibility =
                        View.VISIBLE
                //Set data
                binding.imageViewProductDetails.load(it.image[0])
                binding.detailBottomSheet.productDetailProductName.text = it.title
                binding.detailBottomSheet.textViewProductDetailCategory.text = it.category
                binding.detailBottomSheet.textViewProductDetailProductProperties.text =
                    getString(R.string.error_details_no_property)
                binding.detailBottomSheet.textViewProductDetailProductPrice.text =
                    getString(R.string.price, it.price.toString())
                binding.detailBottomSheet.textViewProductDetailAddDescription.text = it.description
                binding.detailBottomSheet.textViewProductDetailComments.text = getString(
                    R.string.product_details_comments,
                    viewModel.lastCommentsSize.toString()
                )
                binding.detailBottomSheet.textViewProductDetailCategory.text = it.category
                binding.detailBottomSheet.textViewProductDetailProductProperties.text =
                    getString(R.string.error_details_no_property)

                productSliderItemList.clear()
                productSliderItemList.addAll(it.image)
                if (!::productIntroViewPagerAdapter.isInitialized) productIntroViewPagerAdapter =
                    ProductIntroViewPagerAdapter(
                        this.requireActivity(),
                        productSliderItemList
                    )
                binding.viewPagerProductDetailsProductIntro.adapter = productIntroViewPagerAdapter

                binding.pageIndicatorViewProductDetails.count =
                    productSliderItemList.size // specify total count of indicators
                binding.pageIndicatorViewProductDetails.selection = 0
            }
        }

        observeResultLiveData(viewModel.productCommentsLiveData) {
            binding.detailBottomSheet.textViewProductDetailComments.text = getString(
                R.string.product_details_comments,
                it?.size.toString()
            )
            observeResultLiveData(viewModel.getUsersLikeList()) { likeDislikeList ->
                arrangeCommentsRecyclerView(it?.toMutableList() ?: mutableListOf(), likeDislikeList)
            }

            val allCommentsSize = it?.size ?: 0
            binding.detailBottomSheet.textViewProductDetailAllComments.text =
                resources.getString(R.string.details_see_all_comments, allCommentsSize.toString())

        }
    }

    private fun arrangeCommentsRecyclerView(
        comments: MutableList<Comment>,
        likeDislikeList: LikeDislikeHolder?
    ) {
        adapterCommentsRecyclerView = CommentsRecyclerViewAdapter(
            this@DetailsFragment.requireActivity(), true,
            likeDislikeList,
            comments
        )
        with(binding.detailBottomSheet.recyclerViewProductDetailComment) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterCommentsRecyclerView
        }

        adapterCommentsRecyclerView!!.onItemSelected = { position, comment, likeValueChange ->
            observeResultLiveData(viewModel.upsertComments(comment)) {}
            if (likeValueChange) {
                observeResultLiveData(viewModel.setUsersLikeList(comment.id, comment.like)) {}
            } else {
                observeResultLiveData(viewModel.setUsersDislikeList(comment.id, comment.dislike)) {}
            }
        }


    }


    private fun expand(view: View, rotateView: View) {
        val deg = if (rotateView.rotation == 90f) 0f else 90f
        rotateView.animate().rotation(deg).interpolator = AccelerateDecelerateInterpolator()

        view.visibility = View.VISIBLE
        view.alpha = 0.0f

        // Start the animation
        view.animate()
            .translationY(0.0f)
            .alpha(1.0f)
            .setListener(null)
    }

    private fun collapse(view: View, rotateView: View) {
        val deg = if (rotateView.rotation == 90f) 0f else 90f
        rotateView.animate().rotation(deg).interpolator = AccelerateDecelerateInterpolator()

        view.animate()
            .translationY(0.0f)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.GONE
                }
            })
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {/*implementation*/
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when {
            LANGUAGE_ARRAY[position] == "Default" -> {
                targetLangCode = "Default"
            }
            LANGUAGE_ARRAY[position] == "Chinese" -> {
                targetLangCode = "zh"
            }
            LANGUAGE_ARRAY[position] == "Russian" -> {
                targetLangCode = "ru"
            }
            LANGUAGE_ARRAY[position] == "Turkish" -> {
                targetLangCode = "tr"
            }
            LANGUAGE_ARRAY[position] == "Portuguese" -> {
                targetLangCode = "pt"
            }
            LANGUAGE_ARRAY[position] == "Japanese" -> {
                targetLangCode = "ja"
            }
            LANGUAGE_ARRAY[position] == "German" -> {
                targetLangCode = "de"
            }
            LANGUAGE_ARRAY[position] == "Italian" -> {
                targetLangCode = "it"
            }
            LANGUAGE_ARRAY[position] == "English" -> {
                targetLangCode = "en"
            }
            LANGUAGE_ARRAY[position] == "French" -> {
                targetLangCode = "fr"
            }
            LANGUAGE_ARRAY[position] == "Arabic" -> {
                targetLangCode = "ar"
            }
            LANGUAGE_ARRAY[position] == "Thai" -> {
                targetLangCode = "th"
            }
            LANGUAGE_ARRAY[position] == "Spanish" -> {
                targetLangCode = "es"
            }
        }
        adapterCommentsRecyclerView?.setCommentsLanguage(targetLangCode)
    }

    override fun onResume() {
        super.onResume()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.detailBottomSheet.spinnerProductDetailCommentLanguage.setSelection(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.setProductId(-1)
        bottomSheetBehavior = null
        adapterCommentsRecyclerView = null
    }


    companion object {
        private const val TAG = "DetailsFragment"
        const val AR_VIEW_REQUEST_CODE = 5
        private val LANGUAGE_ARRAY = arrayOf(
            "Default",
            "Chinese",
            "English",
            "French",
            "Arabic",
            "Thai",
            "Spanish",
            "Turkish",
            "Portuguese",
            "Japanese",
            "German",
            "Italian",
            "Russian"
        )
        private val AR_VIEW_SUPPORTED_DEVICES = arrayOf(
            "P40", "P40 Pro", "P40 Pro+", "P30", "P30 Pro", "P20",
            "P20 Pro", "Mate 30", "Mate 30 Pro", "Mate 30 RS",
            "Mate 30 (5G)", "Mate 30 Pro (5G)", "Mate X", "Mate Xs", "Mate 20 X (5G)",
            "Mate RS", "Mate 20 X", "Mate 20", "Mate 20 Pro", "Mate 20 RS",
            "Nova 7", "Nova 7 pro", "Nova 6", "Nova 6 Pro", "Nova 4", "Nova 3",
            "HONOR 30", "HONOR 30 Pro", "HONOR 30 Pro+", "HONOR 30S", "HONOR View30",
            "HONOR View30 Pro", "HONOR 20", "HONOR 20 Pro", "HONOR View20", "HONOR 9X"
        )


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AR_VIEW_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val action = DetailsFragmentDirections.actionDetailsToTryWithArNavGraph(productId)
                findNavController().navigate(action)
                //hide bottom view when camera is running in the TryWithArFragment
                (requireActivity() as MainActivity).binding.bottomNavView.visibility = View.GONE
            }
        }
    }
}