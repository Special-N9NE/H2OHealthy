package org.n9ne.common.util

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes

fun ImageView.setUserAvatar(image: String) {
    if (image.isNotEmpty()) {
        val resId = resources.getIdentifier(
            image, "drawable", this.context.packageName
        )
        if (resId != 0) {
            this.setImageResource(resId)
        }
    }
}

fun TextView.setGradient(context: Context, @ColorRes startColor: Int, @ColorRes endColor: Int) {
    val textShader: Shader = LinearGradient(
        0f,
        0f,
        100f,
        0f,
        intArrayOf(
            context.getColor(startColor),
            context.getColor(endColor)
        ),
        floatArrayOf(0f, 1f),
        Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}