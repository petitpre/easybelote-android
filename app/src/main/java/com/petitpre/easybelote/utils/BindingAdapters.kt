/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.petitpre.easybelote.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.petitpre.easybelote.R

@BindingAdapter("isVisible")
fun bindIsVisible(view: View, isVisible: Boolean) {
    if (isVisible) {
        view.animate().alpha(1f).setDuration(200).start()
    } else {
        view.animate().alpha(0f).setDuration(200).start()
    }
}

@BindingAdapter("isSelected")
fun bindIsSelected(view: TextView, isSelected: Boolean) {
    if (isSelected) {
        view.setTextColor(view.context.getColor(R.color.strawberry))
    } else {
        view.setTextColor(view.context.getColor(R.color.greyishBrown))
    }
}
