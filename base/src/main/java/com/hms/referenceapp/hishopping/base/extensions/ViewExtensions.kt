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
package com.hms.referenceapp.hishopping.base.extensions

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import coil.load
import coil.transform.CircleCropTransformation
import com.hms.referenceapp.hishopping.base.databinding.BaseToastViewBinding


fun View.makeGone() {
    this.visibility = View.GONE
}

fun View.makeVisible() {
    this.visibility = View.VISIBLE
}

fun View.makeInvisible() {
    this.visibility = View.INVISIBLE
}

fun ViewGroup.inflateLayout(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

@BindingAdapter("srcUrlCircle")
fun ImageView.loadCircleImageFromUrl(url: String?) {
    url?.let {
        load(url) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
    }
}

@BindingAdapter("srcUrl")
fun ImageView.loadImageFromUrl(url: String?) {
    url?.let {
        load(url) {
            crossfade(true)
        }
    }
}

fun LifecycleOwner.showToast(msg: String) {
    (this as? Fragment)?.let {
        Toast.makeText(it.requireActivity(), msg, Toast.LENGTH_SHORT).show()
    } ?: let {
        (it as? Activity)?.let { Toast.makeText(it, msg, Toast.LENGTH_SHORT).show() }
    }
}

fun Activity.customToast(toastMessage: String) {
    val binding = BaseToastViewBinding
        .inflate(LayoutInflater.from(this), window.decorView.rootView as ViewGroup, false)
    val toast = Toast(this)
    toast.duration = Toast.LENGTH_LONG
    binding.textViewCustomToast.text = toastMessage
    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
    toast.view = binding.root
    toast.show()
}