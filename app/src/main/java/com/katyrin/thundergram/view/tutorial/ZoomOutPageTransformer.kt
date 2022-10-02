package com.katyrin.thundergram.view.tutorial

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float): Unit = when {
        position < -1 -> page.alpha = ZERO_ALPHA
        position <= 1 -> modifyDefaultSlideTransition(page, position)
        else -> page.alpha = ZERO_ALPHA
    }

    private fun modifyDefaultSlideTransition(page: View, position: Float) {
        val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
        val verticalMargin = page.height * (1 - scaleFactor) / 2
        val horizontalMargin = page.width * (1 - scaleFactor) / 2
        page.translationX =
            if (position < 0) horizontalMargin - verticalMargin / 2
            else -horizontalMargin + verticalMargin / 2
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor
        page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
    }

    private companion object {
        const val MIN_SCALE = 0.85f
        const val MIN_ALPHA = 0.5f
        const val ZERO_ALPHA = 0f
    }
}