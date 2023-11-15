package com.hms.referenceapp.hishopping.app.profileedit.presentation

import com.hms.referenceapp.hishopping.core.UIText

data class ProfileEditViewState(
    val successMessage: UIText? = null,
    val errorMessage: UIText? = null,
    val noMessage:Boolean = false,
    val loading:Boolean = false
)
