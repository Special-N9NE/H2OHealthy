package org.n9ne.h2ohealthy.ui.login

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.n9ne.h2ohealthy.R
import org.n9ne.h2ohealthy.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private lateinit var b: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSplashBinding.inflate(inflater)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



       setTextColors()
    }

    private fun setTextColors(){
        val textShader: Shader = LinearGradient(
            0f,
            0f,
            100f,
            0f,
            intArrayOf(
                requireContext().getColor(R.color.linearBlueStart),
                requireContext().getColor(R.color.linearBlueEnd)),
            floatArrayOf(0f, 1f),
            TileMode.CLAMP
        )
        b.tvTitle1.paint.shader = textShader

        val textShader2: Shader = LinearGradient(
            0f,
            0f,
            100f,
            0f,
            intArrayOf(
                requireContext().getColor(R.color.linearPurpleStart),
                requireContext().getColor(R.color.linearPurpleEnd)),
            floatArrayOf(0f, 1f),
            TileMode.CLAMP
        )
        b.tvTitle2.paint.shader = textShader2
    }
}