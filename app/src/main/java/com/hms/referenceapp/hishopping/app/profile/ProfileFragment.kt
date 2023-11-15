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
package com.hms.referenceapp.hishopping.app.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import com.hms.referenceapp.hishopping.R
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.base.extensions.makeGone
import com.hms.referenceapp.hishopping.base.extensions.makeVisible
import com.hms.referenceapp.hishopping.core.view.LoadingDialog
import com.hms.referenceapp.hishopping.data.model.User
import com.hms.referenceapp.hishopping.databinding.FragmentProfileBinding
import com.hms.referenceapp.hishopping.shoplocations.presentation.LocationType
import com.hms.referenceapp.hishopping.signin.SignInManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileViewModel, FragmentProfileBinding>() {

    override var layoutRes: Int = R.layout.fragment_profile
    private lateinit var signInManager: SignInManager
    private lateinit var loadingDialog: LoadingDialog

    companion object {
        private const val RC_SIGN_IN = 1000
        private const val TAG = "ProfileFragment"
    }


    override fun viewCreated(view: View, savedInstanceState: Bundle?) {

        signInManager = SignInManager(requireActivity())
        loadingDialog = LoadingDialog.Builder(requireContext()).build()

        getUser()

        binding.buttonProfileSignIn.setOnClickListener {
            openSignInActivity()
        }

        binding.buttonProfileSignOut.setOnClickListener {
            signInManager.signOut()
            getUser()
        }

        binding.buttonProfileStores.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileToShopslocation(LocationType.NEAREST_SHOPS)
            )
        }

        binding.buttonProfileFavorites.setOnClickListener {
            signInManager.getUser()?.let {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileToMyOrdersGraph()
                )
            } ?: run { openSignInActivity() }
        }

        binding.buttonProfileMyAddresses.setOnClickListener {
            if (signInManager.getUser() == null) {
                openSignInActivity()
            } else {
                signInManager.getUser()?.let {
                    findNavController().navigate(
                        ProfileFragmentDirections
                            .actionProfileToAddress(it)
                    )
                }
            }
        }

        binding.buttonProfileCallCenter.setOnClickListener {
            Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:+905422140123")
                startActivity(this)
            }
        }

        binding.buttonProfileMyCreditCards.setOnClickListener {
            if (signInManager.getUser() == null) {
                openSignInActivity()
            } else findNavController().navigate(R.id.my_credit_cards_nav_graph)
        }

        binding.buttonProfileProfileEditing.setOnClickListener {
            if (signInManager.getUser() == null) {
                openSignInActivity()
            } else findNavController().navigate(R.id.action_profile_to_profile_edit_nav)
        }
    }

    override fun observeViewModel() {
        viewModel.uiState.observe(viewLifecycleOwner){ uiState ->
            if (uiState.loading) loadingDialog.show()
            else{
                loadingDialog.hide()
                updateUIWithUser(uiState.user)
            }
        }
    }

    private fun getUser() {
        signInManager.getUser()?.let {
            viewModel.getUser(it)
        }?:run{ updateUIWithoutUser() }
    }

    private fun updateUIWithoutUser(){
        Log.i(TAG, "updateUI: user null")
        binding.constraintLayoutProfileDisconnected.makeVisible()
        binding.constraintLayoutProfileConnected.makeGone()
        binding.buttonProfileProfileEditing.makeGone()
        binding.buttonProfileSignOut.makeGone()
    }

    private fun updateUIWithUser(user:User){
        binding.constraintLayoutProfileDisconnected.makeGone()
        binding.constraintLayoutProfileConnected.makeVisible()
        binding.buttonProfileProfileEditing.makeVisible()
        binding.buttonProfileSignOut.makeVisible()

        val name = when {
            user.fullName.isNotEmpty() -> {
                user.fullName
            }
            user.email.isNotEmpty() -> {
                user.email
            }
            else -> {
                ""
            }
        }

        binding.textViewProfileDisplayName.text = name

        when {
            user.photoUrl.isNotEmpty() -> {
                binding.textViewProfileAvatar.makeGone()
                binding.imageViewProfileAvatar.load(user.photoUrl)
            }
            user.fullName.isNotEmpty() -> {
                binding.textViewProfileAvatar.makeVisible()
                binding.textViewProfileAvatar.text =
                    getFirstLettersFromDisplayName(user.fullName)
            }
            user.email.isNotEmpty() -> {
                binding.textViewProfileAvatar.makeVisible()
                binding.textViewProfileAvatar.text =
                    user.email.take(2).uppercase(Locale.getDefault())
            }
            else -> {
                binding.textViewProfileAvatar.makeGone()
                binding.imageViewProfileAvatar.load(R.drawable.ic_blank_avatar)
            }
        }
    }

    private fun getFirstLettersFromDisplayName(displayName: String): String {
        return if (displayName.contains(" ")) {
            val tmp = displayName.split(' ')
            (tmp[0].take(1) + tmp[1].take(1)).uppercase(Locale.getDefault())
        } else {
            displayName.take(2).uppercase(Locale.getDefault())
        }
    }

    private fun openSignInActivity() {
        signInManager.getIntent(requireActivity()) {
            startActivityForResult(it, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            Log.i("hello", "onActivityResult: resultCode: $resultCode")
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "onActivityResult: sign in cancelled")
            }
            if (resultCode == Activity.RESULT_OK) {
                getUser()
            }
        }
    }
}