package com.petitpre.easybelote.utils

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.lifecycle.MutableLiveData

fun EditText.setOnDoneListener(handler: (EditText) -> Unit) {
    this.setOnEditorActionListener({ v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE
            || actionId == EditorInfo.IME_ACTION_SEND
            || actionId == EditorInfo.IME_ACTION_NEXT
        ) {
            handler(this)
        }
        false
    })
}

fun <T> MutableLiveData<T>.update(update: (T) -> Unit) {
    this.value?.let { r ->
        update(r)
        this.value = r
    }
}