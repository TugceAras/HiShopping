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
package com.hms.referenceapp.hishopping.mycreditcards.presentation

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperenbabagil.simpleanimationpopuplibrary.SapDialog
import com.hms.lib.commonmobileservices.core.handleSuccess
import com.hms.lib.commonmobileservices.creditcardscanner.CreditCardScanner
import com.hms.referenceapp.hishopping.base.BaseFragment
import com.hms.referenceapp.hishopping.data.model.CreditCard
import com.hms.referenceapp.hishopping.mycreditcards.R
import com.hms.referenceapp.hishopping.mycreditcards.databinding.FragmentMyCreditCardsBinding
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCreditCardsFragment :
    BaseFragment<MyCreditCardsViewModel, FragmentMyCreditCardsBinding>() {

    override val layoutRes: Int = R.layout.fragment_my_credit_cards

    private lateinit var adapterMyCreditCardsRecyclerView: MyCreditCardsRecyclerViewAdapter

    override fun viewCreated(view: View, savedInstanceState: Bundle?) {
        getAllCards()
        binding.floatingActionButtonAddCreditCard.setOnClickListener {
            runWithPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                CreditCardScanner.instance(requireContext())?.scan {
                    it.handleSuccess {
                        it.data?.let {
                            val creditCard = CreditCard(
                                it.cardNumber ?: "",
                                it.expireDate ?: "",
                                it.issuer ?: "",
                                it.type ?: ""
                            )
                            observeResultLiveData(viewModel.upsertCreditCard(creditCard)) {
                                getAllCards()
                            }
                        } ?: {
                            Toast.makeText(
                                requireContext(), getString(R.string.my_credit_cards_could_not_detect),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.swipeRefreshLayoutMyCreditCards.apply {
            setOnRefreshListener {
                getAllCards()
                isRefreshing = false
            }
        }

    }

    private fun getAllCards() {
        observeResultLiveData(viewModel.getAllCreditCards()) {
            it?.run {
                fillCardList(this)
            }
        }
    }

    private fun fillCardList(list: List<CreditCard>) {
        adapterMyCreditCardsRecyclerView = MyCreditCardsRecyclerViewAdapter(list.toMutableList())
        with(binding.recyclerViewMyCreditCards) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterMyCreditCardsRecyclerView
        }
        adapterMyCreditCardsRecyclerView.onItemSelected = { position, item ->
            Toast.makeText(requireContext(), item.cardNumber, Toast.LENGTH_SHORT).show()
        }

        adapterMyCreditCardsRecyclerView.onItemLongSelected = { position, item ->
            SapDialog(requireActivity()).apply {
                titleText = getString(SapDialog.WARNING_RES_ID)
                messageText = getString(R.string.my_credit_cards_are_you_sure_to_delete)
                addPositiveButton(getString(R.string.my_credit_cards_delete)) {
                    observeResultLiveData(viewModel.deleteCreditCard(item)) {
                        getAllCards()
                    }
                }
                addNegativeButton(getString(R.string.cancel)) {}
                animResource = R.raw.sap_warning_anim
                isCancellable = false
            }.build().show()
        }

    }


    override fun observeViewModel() {}

}