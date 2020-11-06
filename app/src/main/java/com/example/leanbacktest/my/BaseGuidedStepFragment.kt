package com.example.leanbacktest.my

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import androidx.transition.TransitionInflater
import com.example.leanbacktest.R


abstract class BaseGuidedStepFragment :  GuidedStepSupportFragment() {

    companion object {

        private const val UNDEFINED = -1
        const val PLAIN_TITLE_TEXT = -2
        const val PLAIN_DESCRIPTION_TEXT = -3
        const val PLAIN_BREADCRUMB_TEXT = -4
        const val PLAIN_ACTION_TEXT = -5
        const val MAXIMAL_ACTIONS_COUNT = 10
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        PLAIN_TITLE_TEXT, PLAIN_DESCRIPTION_TEXT, PLAIN_BREADCRUMB_TEXT
    )
    internal annotation class PlainText

    /** Layout of the fragment passed to guidance. */
    @LayoutRes
    open val onProvideFragmentLayoutId = R.layout.guidance_tv_base

    /** String resource for guidance title. If not implemented null is passed to Guidance. */
    @StringRes
    open val titleRes: Int = UNDEFINED

    /** String resource for guidance description. If not implemented null is passed to Guidance. */
    @StringRes
    open val descriptionRes: Int = UNDEFINED

    /** String resource for guidance breadcrumb. */
    @StringRes
    open val breadcrumbRes: Int = UNDEFINED

    /** Drawable resource for guidance icon. If not implemented null is passed to Guidance. */
    @DrawableRes
    open val iconRes: Int = UNDEFINED

    /** List of actions in side menu. */
    open val guidedActions: List<Pair<Long, Int>> = emptyList()

    @StyleRes
    override fun onProvideTheme() = R.style.Theme_Vpn_Leanback_GuidedStep

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        activity?.run {
            window?.decorView?.setBackgroundColor(getThemeColor(R.attr.colorPrimary))
        }
    }

    /**
     * Called during [onCreateView]
     */
    override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
        context?.run {
            return GuidanceStylist.Guidance(
                getStringOrNull(this, titleRes),
                getStringOrNull(this, descriptionRes),
                getStringOrNull(this, breadcrumbRes),
                getDrawableOrNull(this, iconRes)
            )
        }
        return GuidanceStylist.Guidance(null, null, null, null)
    }

    private fun getStringOrNull(context: Context, resourceId: Int): String? {
        return when (resourceId) {
            PLAIN_TITLE_TEXT, PLAIN_DESCRIPTION_TEXT, PLAIN_BREADCRUMB_TEXT -> getPlainText(resourceId)
            UNDEFINED -> null
            in PLAIN_ACTION_TEXT downTo PLAIN_ACTION_TEXT - MAXIMAL_ACTIONS_COUNT -> getPlainText(resourceId)
            else -> context.getString(resourceId)
        }
    }

    private fun getDrawableOrNull(context: Context, resourceId: Int): Drawable? {
        return if (resourceId == UNDEFINED) {
            null
        } else {
            ContextCompat.getDrawable(context, resourceId)
        }
    }

    /**
     * String for given id in case that resource id isn't sufficient for our needs.
     */
    open fun getPlainText(@PlainText id: Int): String? = null

    /**
     * Wraps [onGuidedActionClicked] for null safety.
     *
     * @return true if event has been consumed, false otherwise.
     */
    abstract fun onSafeGuidedActionClicked(action: GuidedAction): Boolean

    override fun onGuidedActionClicked(action: GuidedAction?) {
        if (action == null || !onSafeGuidedActionClicked(action)) {
            super.onGuidedActionClicked(action)
        }
    }

    override fun onCreateGuidanceStylist(): GuidanceStylist {
        return object : GuidanceStylist() {
            @LayoutRes
            override fun onProvideLayoutId(): Int = onProvideFragmentLayoutId
        }
    }

    @CallSuper
    override fun onCreateActions(actions: MutableList<GuidedAction>, savedInstanceState: Bundle?) {
        context?.run {
            for (actionParams in guidedActions) {
                val title = getStringOrNull(this, actionParams.second) ?: continue
                actions.add(createGuidedAction(actionParams.first, title))
            }
        }
    }

    protected fun createGuidedAction(actionId: Long, actionTitle: String): GuidedAction {
        return GuidedAction.Builder(context)
            .id(actionId)
            .title(actionTitle)
            .multilineDescription(true)
            .build()
    }

    override fun onProvideFragmentTransitions() {
        val context = context ?: return
        enterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
        exitTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.fade)
    }
}

fun Context.getThemeColor(attributeReference: Int): Int {
    val themeColorTypedValue = TypedValue()
    theme.resolveAttribute(attributeReference, themeColorTypedValue, true)

    val color: Int
    obtainStyledAttributes(themeColorTypedValue.data, intArrayOf(attributeReference)).apply {
        color = getColor(0, -1)
        recycle()
    }
    return color
}