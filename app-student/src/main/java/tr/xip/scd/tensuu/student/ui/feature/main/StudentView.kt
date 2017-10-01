package tr.xip.scd.tensuu.student.ui.feature.main

import com.hannesdorfmann.mosby3.mvp.MvpView

interface StudentView : MvpView {
    fun setTitle(value: String)
    fun setSsid(value: String?)
    fun setGrade(value: String?)
    fun setFloor(value: Int?)
    fun setPoints(value: Int)
    fun setFlipperChild(position: Int)

    fun setAdapter(value: PointsAdapter)
    fun getAdapter(): PointsAdapter?

    fun showToast(text: String)
    fun notifyDateChanged()

    fun startLoginActivity()

    fun die()
}