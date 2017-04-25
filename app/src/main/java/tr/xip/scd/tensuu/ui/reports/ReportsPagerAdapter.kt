package tr.xip.scd.tensuu.ui.reports

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R

class ReportsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                WeeklyFragment()
            }
            1 -> {
                CustomRangeFragment()
            }
            else -> null
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.title_weekly)
            1 -> return context.getString(R.string.title_custom)
            else -> return "?"
        }
    }
}