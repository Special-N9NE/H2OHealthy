package org.n9ne.common.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter

data class Setting(
    val title: String,
    val icon: Int,
    val type: SettingItem
) {
    companion object {
        @JvmStatic
        @BindingAdapter("android:setImage")
        fun setImage(imageView: ImageView, icon: Int) {
            imageView.setImageResource(icon)
        }
    }
}
