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
package com.hms.referenceapp.hishopping.app.details.addcomment.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.navArgs
import coil.load
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.app.details.comments.presentation.CommentsFragmentArgs
import com.hms.referenceapp.hishopping.app.mainactivity.MainActivity
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.customToast
import com.hms.referenceapp.hishopping.data.model.Comment
import com.hms.referenceapp.hishopping.databinding.FragmentAddCommentBinding
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddCommentFragment : BaseFragment<AddCommentViewModel, FragmentAddCommentBinding>() {

    override var layoutRes: Int = R.layout.fragment_add_comment
    private val args: CommentsFragmentArgs by navArgs()

    override fun observeViewModel() {}

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        binding.commentDescriptionText.setText(resources.getString(R.string.add_comment_size, "0"))

        binding.commentDescriptionText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    binding.textViewAddCommentDescriptionCounter.text =
                        resources.getString(R.string.add_comment_size, it.length.toString())
                }

            }
        })

        binding.buttonAddCommentSubmitComment.setOnClickListener {
            if (binding.checkBoxAddCommentAgreement.isChecked) {
                observeResultLiveData(
                    viewModel.insertComments(
                        Comment(
                            if (SignInManager(requireActivity()).getUser()?.fullName == null) {
                                resources.getString(R.string.add_comment_anonymous)
                            } else {
                                SignInManager(requireActivity()).getUser()?.fullName.toString()
                            },
                            SimpleDateFormat(
                                "dd-MM-yyyy",
                                Locale.getDefault()
                            ).format(Date()),
                            -1,
                            binding.commentDescriptionText.text.toString(),
                            args.productId,
                            binding.ratingBar.rating.toInt(),
                            "",
                            0,
                            0
                        )
                    )
                ) {
                    (this.requireActivity() as? MainActivity)?.customToast(resources.getString(R.string.add_comment_product_successfully))
                    (this.requireActivity() as? MainActivity)?.onBackPressed()
                }
            } else {
                (this.requireActivity() as? MainActivity)?.customToast(resources.getString(R.string.add_comment_accept_user_agreement))
            }
        }

        observeResultLiveData(viewModel.loadProductDetails(args.productId)) {
            binding.textViewAddCommentProductPrice.text =
                resources.getString(R.string.add_comment_price, it?.price.toString())
            binding.textViewAddCommentProductDescription.text = it?.description
            binding.textViewAddCommentProductTitle.text = it?.title
            binding.imageViewDetailsAddCommentProduct.load(it?.image?.get(0))
        }
    }
}