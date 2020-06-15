package com.github.bentilbrook.barnacle.sample

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnApplyWindowInsetsListener
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach

/** Re-applies window insets when child views are added, to ensure they get a chance to handle them. */
@Suppress("unused")
class WindowInsetsConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        configureInsetHandling()
    }
}

/** Re-applies window insets when child views are added, to ensure they get a chance to handle them. */
@Suppress("unused")
class WindowInsetsFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        configureInsetHandling()
    }
}

/** A [OnApplyWindowInsetsListener] that forwards the insets to all children, but doesn't consume them. */
private object NonConsumingWindowInsetsListener : OnApplyWindowInsetsListener {
    override fun onApplyWindowInsets(v: View, insets: WindowInsets): WindowInsets {
        // Configure system inset handling for fragment container so it dispatches insets to each child view
        // breadth-first, instead of the default depth-first, which would mean subsequently added fragments
        // don't account for the status bar.
        (v as? ViewGroup)?.forEach {
            // Supply a copy to each child, preventing them from consuming ours
            it.dispatchApplyWindowInsets(WindowInsets(insets))
        }
        return insets
    }
}

private fun ViewGroup.configureInsetHandling() {
    setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
        override fun onChildViewAdded(parent: View, child: View) = requestApplyInsets()
        override fun onChildViewRemoved(parent: View?, child: View?) {
        }
    })
    setOnApplyWindowInsetsListener(NonConsumingWindowInsetsListener)
}
