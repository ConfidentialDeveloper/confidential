package com.confidential.ui.common.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


object CommonBinding {

    @BindingAdapter("error")
    @JvmStatic
    fun setTextInputLayoutErrorMessage(view: TextInputLayout, errorMessage: String?) {
        view.error = errorMessage ?: ""
    }
}