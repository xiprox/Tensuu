package tr.xip.scd.tensuu.ui.student

import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.ui.common.adapter.PointsAdapter

interface StudentView : MvpView {
    fun setTitle(value: String)
    fun setSsid(value: String?)
    fun setGrade(value: String?)
    fun setFloor(value: Int?)
    fun setPoints(value: Int)
    fun setRestrictedRangeText(start: Long, end: Long)
    fun setFlipperChild(position: Int)

    fun setAdapter(value: PointsAdapter)
    fun getAdapter(): PointsAdapter?

    fun showRestrictedRange(value: Boolean = true)

    fun showToast(text: String)
    fun notifyDateChanged()
    fun die()
}