package tr.xip.scd.tensuu.ui.reports

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.fragment_reports.*
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.common.AnimateableFragment
import tr.xip.scd.tensuu.common.util.OnPageChangeListenerAdapter
import tr.xip.scd.tensuu.common.ext.doOnLayout
import tr.xip.scd.tensuu.common.ext.toPx

/**
 * Pretty much just a container. No MVP here.
 */
class ReportsFragment : Fragment(), AnimateableFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        pager.adapter = ReportsPagerAdapter(childFragmentManager)
        tabs.setupWithViewPager(pager)

        appbar.doOnLayout {
            animateEnter()
        }

        pager.addOnPageChangeListener(object : OnPageChangeListenerAdapter() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val victim = if (position == 0) rangeControls else weekControls
                val hero = if (position == 0) weekControls else rangeControls

                victim.alpha = positionOffset
                hero.alpha = 1 - positionOffset

                victim.translationY = 48.toPx(context) * (1 - positionOffset)
                hero.translationY = 48.toPx(context) * positionOffset
            }
        })
    }

    override fun animateEnter() {
        animate(true, getEnterDuration())
    }

    override fun animateExit() {
        animate(false, getExitDuration())
    }

    private fun animate(enter: Boolean, duration: Long) {
        if (appbar == null) return

        val height = appbar.measuredHeight.toFloat()
        val translationY = appbar.translationY

        if (enter) {
            appbar.translationY = translationY - height
        }

        appbar.animate()
                .translationYBy(if (enter) height else -height)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(duration)
                .start()

        for (i in 0..(appbar.childCount - 1)) {
            val child = appbar.getChildAt(i)
            val translationYBy = child.height * 0.25f

            if (enter) {
                child.alpha = 0f
                child.translationY = child.translationY - translationYBy
            }

            child.animate()
                    .translationYBy(if (enter) translationYBy else -translationYBy)
                    .alpha(if (enter) 1f else 0f)
                    .setStartDelay(if (enter) 100 else 0)
                    .setInterpolator(DecelerateInterpolator())
                    .setDuration(if (enter) duration + 100 else duration - 50)
                    .start()
        }
    }

    override fun getEnterDuration(): Long = 200

    override fun getExitDuration(): Long = 100
}