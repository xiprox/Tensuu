package tr.xip.scd.tensuu.util

import android.support.v4.view.ViewPager

open class OnPageChangeListenerAdapter : ViewPager.OnPageChangeListener{
    override fun onPageScrollStateChanged(state: Int) {}
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {}
}