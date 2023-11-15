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
package com.hms.referenceapp.hishopping.app.details.comments.presentation

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.mainactivity.CrossNavBackStacksNavigator
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.makeGone
import com.hms.referenceapp.hishopping.data.model.CartItem
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.data.model.LikeDislikeHolder
import com.hms.referenceapp.hishopping.databinding.FragmentCommentsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.floor

@AndroidEntryPoint
class CommentsFragment : BaseFragment<CommentsViewModel, FragmentCommentsBinding>(),
    AdapterView.OnItemSelectedListener {

    override var layoutRes: Int = R.layout.fragment_comments
    private val args: CommentsFragmentArgs by navArgs()
    lateinit var navController: NavController

    private val commentArrayList: ArrayList<Comment> = arrayListOf()

    private var targetLangCode = "zh"
    private lateinit var adapterCommentsRecyclerView: CommentsRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)

        observeResultLiveData(viewModel.getCommentsById(args.productId)) {
            it?.let {
                observeResultLiveData(viewModel.getUsersLikeList()) { likeDislikeList ->
                    commentArrayList.addAll(it)
                    binding.textViewStarAverage.text =
                        (floor(calculateCommentAverage(it as ArrayList<Comment>) * 100.0) / 100).toString()
                    arrangeCommentsRecyclerView(it, likeDislikeList)
                    binding.textViewCommentComments.text =
                        resources.getString(R.string.comment_size, it.size.toString())
                    setWeightFromAverage()
                }
            }
        }

        setupUI()
        val navBar: BottomNavigationView = this.requireActivity().findViewById(R.id.bottomNavView)
        navBar.makeGone()
    }

    private fun arrangeCommentsRecyclerView(
        comments: MutableList<Comment>,
        likeDislikeList: LikeDislikeHolder?
    ) {
        adapterCommentsRecyclerView = CommentsRecyclerViewAdapter(
            this@CommentsFragment.requireActivity(), true,
            likeDislikeList,
            comments
        )
        with(binding.recyclerViewComment) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterCommentsRecyclerView
        }

        adapterCommentsRecyclerView.onItemSelected = { position, comment, likeValueChange ->
            observeResultLiveData(viewModel.upsertComments(comment)) {}
            if (likeValueChange) {
                observeResultLiveData(
                    viewModel.setUsersLikeList(
                        comment.id,
                        comment.like
                    )
                ) {}
            } else {
                observeResultLiveData(
                    viewModel.setUsersDislikeList(
                        comment.id,
                        comment.dislike
                    )
                ) {}
            }

        }


    }

    private fun setupUI() {
        binding.buttonCommentAddComment.setOnClickListener {
            val action =
                CommentsFragmentDirections.actionCommentToAddCommentNavGraph(args.productId)
            navController.navigate(action)
        }

        binding.spinnerCommentLanguage.onItemSelectedListener = this
        val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            languageArray
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCommentLanguage.adapter = spinnerAdapter

        binding.buttonCommentAddToCart.setOnClickListener {
            observeResultLiveData(viewModel.getProductDetail(args.productId)) {
                it?.let {
                    observeResultLiveData(
                        viewModel.addProductToBasket(
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
                                findNavController().popBackStack(R.id.detail_nav_graph, true)
                                (requireActivity() as? CrossNavBackStacksNavigator)?.navigateToMyCart()
                            }
                            addNegativeButton(getString(R.string.continue_shopping)) {}
                        }.build().show()
                    }
                }
            }
        }
    }

    private fun calculateCommentAverage(commentList: ArrayList<Comment>): Float {
        var commentStarSum = 0f
        commentList.forEach {
            commentStarSum += it.star
        }
        return commentStarSum / commentList.size
    }

    private fun setWeightFromAverage() {
        var commentStarSum = 0f

        var counterStarOf5 = 0f
        var counterStarOf4 = 0f
        var counterStarOf3 = 0f
        var counterStarOf2 = 0f
        var counterStarOf1 = 0f

        commentArrayList.forEach {
            commentStarSum += it.star
            when (it.star) {
                5 -> {
                    counterStarOf5++
                }
                4 -> {
                    counterStarOf4++
                }
                3 -> {
                    counterStarOf3++
                }
                2 -> {
                    counterStarOf2++
                }
                1 -> {
                    counterStarOf1++
                }
            }
        }

        val averageStarOf5: Float = (counterStarOf5 / commentArrayList.size)
        val averageStarOf4: Float = (counterStarOf4 / commentArrayList.size)
        val averageStarOf3: Float = (counterStarOf3 / commentArrayList.size)
        val averageStarOf2: Float = (counterStarOf2 / commentArrayList.size)
        val averageStarOf1: Float = (counterStarOf1 / commentArrayList.size)

        val star1Param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        val star2Param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        val star3Param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        val star4Param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        val star5Param: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

        if (averageStarOf1 != 0f) {
            star1Param.weight = (1 / averageStarOf1) - 1
            binding.viewCommentStarOneStart.layoutParams = star1Param
        }
        if (averageStarOf2 != 0f) {
            star2Param.weight = (1 / averageStarOf2) - 1
            binding.viewCommentStarTwoStart.layoutParams = star2Param
        }
        if (averageStarOf3 != 0f) {
            star3Param.weight = (1 / averageStarOf3) - 1
            binding.viewCommentStarThreeStart.layoutParams = star3Param
        }
        if (averageStarOf4 != 0f) {
            star4Param.weight = (1 / averageStarOf4) - 1
            binding.viewCommentStarFourStart.layoutParams = star4Param
        }
        if (averageStarOf5 != 0f) {
            star5Param.weight = (1 / averageStarOf5) - 1
            binding.viewCommentStarFiveStart.layoutParams = star5Param
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when {
            languageArray[position] == "Default" -> {
                targetLangCode = "Default"
            }
            languageArray[position] == "Chinese" -> {
                targetLangCode = "zh"
            }
            languageArray[position] == "Russian" -> {
                targetLangCode = "ru"
            }
            languageArray[position] == "Turkish" -> {
                targetLangCode = "tr"
            }
            languageArray[position] == "Portuguese" -> {
                targetLangCode = "pt"
            }
            languageArray[position] == "Japanese" -> {
                targetLangCode = "ja"
            }
            languageArray[position] == "German" -> {
                targetLangCode = "de"
            }
            languageArray[position] == "Italian" -> {
                targetLangCode = "it"
            }
            languageArray[position] == "English" -> {
                targetLangCode = "en"
            }
            languageArray[position] == "French" -> {
                targetLangCode = "fr"
            }
            languageArray[position] == "Arabic" -> {
                targetLangCode = "ar"
            }
            languageArray[position] == "Thai" -> {
                targetLangCode = "th"
            }
            languageArray[position] == "Spanish" -> {
                targetLangCode = "es"
            }
        }
        adapterCommentsRecyclerView.setCommentsLanguage(targetLangCode)
    }

    companion object {
        private val languageArray = arrayOf(
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
    }

    override fun observeViewModel() {}
}