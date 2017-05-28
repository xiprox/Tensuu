package tr.xip.scd.tensuu.ui.admin

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import tr.xip.scd.tensuu.App.Companion.context
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.ui.admin.period.PeriodFragment
import tr.xip.scd.tensuu.ui.admin.student.StudentsFragment
import tr.xip.scd.tensuu.ui.admin.user.UsersFragment

class AdminToolsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> UsersFragment.new()
            1 -> StudentsFragment.new()
            2 -> PeriodFragment.new()
            else -> null
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.title_users)
            1 -> return context.getString(R.string.title_students)
            2 -> return context.getString(R.string.title_period)
            else -> return "?"
        }
    }
}