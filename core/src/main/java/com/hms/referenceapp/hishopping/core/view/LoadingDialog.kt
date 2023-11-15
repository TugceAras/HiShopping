package com.hms.referenceapp.hishopping.core.view

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.hms.referenceapp.hishopping.core.databinding.LoadingDialogBinding

/**
 * Created by Mustafa Kemal Özdemir on 3/28/2022.
 */
class LoadingDialog(val builder: Builder): Dialog(builder.context) {

    init {
        val inflater = LayoutInflater.from(builder.context)
        val loadingDialogBinding = LoadingDialogBinding.inflate(inflater, null, false)
        setContentView(loadingDialogBinding.root)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
    }

    init {
        setCanceledOnTouchOutside(builder.barrierDismissible)
    }

    class Builder(val context: Context) {
        var barrierDismissible = true

        fun build(): LoadingDialog {
            return LoadingDialog(this)
        }
    }

}