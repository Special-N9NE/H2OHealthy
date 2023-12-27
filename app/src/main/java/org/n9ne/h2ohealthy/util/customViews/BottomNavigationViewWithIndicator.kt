package org.n9ne.h2ohealthy.util.customViews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.n9ne.common.R.color
import org.n9ne.common.R.dimen

private const val DEFAULT_SCALE = 1f

class BottomNavigationViewWithIndicator : BottomNavigationView,
    BottomNavigationView.OnNavigationItemSelectedListener {


    private var externalSelectedListener: OnNavigationItemSelectedListener? = null
    private var animator: ValueAnimator? = null

    private val indicator = RectF()
    private val ps = ResourcesCompat.getColor(resources, color.linearPurpleStart, context.theme)
    private val pe = ResourcesCompat.getColor(resources, color.linearPurpleEnd, context.theme)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            ps,
            pe,
            Shader.TileMode.MIRROR
        )
    }

    private val bottomOffset = resources.getDimension(dimen.bottom_margin)
    private val defaultSize = resources.getDimension(dimen.default_size)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    init {
        super.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val add = org.n9ne.common.R.id.add
        if (externalSelectedListener?.onNavigationItemSelected(item) != false && item.itemId != add
        ) {
            onItemSelected(item.itemId)
            return true
        }
        return false
    }

    override fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        externalSelectedListener = listener
    }

    override fun onAttachedToWindow() {

        this.itemRippleColor = null
        this.itemIconTintList = null
        super.onAttachedToWindow()
        doOnPreDraw {
            // Move the indicator in place when the view is laid out
            onItemSelected(selectedItemId, false)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clean up the animator if the view is going away
        cancelAnimator(setEndValues = true)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (isLaidOut) {
            val cornerRadius = indicator.height() / 2f
            canvas.drawRoundRect(indicator, cornerRadius, cornerRadius, paint)
        }
    }

    private fun onItemSelected(itemId: Int, animate: Boolean = true) {
        if (!isLaidOut) return

        // Interrupt any current animation, but don't set the end values,
        // if it's in the middle of a movement we want it to start from
        // the current position, to make the transition smoother.
        cancelAnimator(setEndValues = false)

        val itemView = findViewById<View>(itemId) ?: return
        val fromCenterX = indicator.centerX()

        val progress = DEFAULT_SCALE
        val distanceTravelled = linearInterpolation(progress, fromCenterX, itemView.centerX)

        val scale = DEFAULT_SCALE
        val indicatorWidth = defaultSize * scale

        val left = distanceTravelled - indicatorWidth / 2f
        val top = height - bottomOffset - defaultSize
        val right = distanceTravelled + indicatorWidth / 2f
        val bottom = height - bottomOffset

        indicator.set(left, top, right, bottom)

        animator = ValueAnimator.ofFloat(0.0f, 255.0f).apply {
            addUpdateListener {

                paint.alpha = (it.animatedValue as Float).toInt()

                invalidate()
            }
            interpolator = LinearOutSlowInInterpolator()
            duration = if (animate) 500L else 0L

            start()
        }
    }

    /**
     * Linear interpolation between 'a' and 'b' based on the progress 't'
     */
    private fun linearInterpolation(t: Float, a: Float, b: Float) = (1 - t) * a + t * b

    /**
     * Convenience property for getting the center X value of a View
     */
    private val View.centerX get() = left + width / 2f

    private fun cancelAnimator(setEndValues: Boolean) = animator?.let {
        if (setEndValues) {
            it.end()
        } else {
            it.cancel()
        }
        it.removeAllUpdateListeners()
        animator = null
    }
}