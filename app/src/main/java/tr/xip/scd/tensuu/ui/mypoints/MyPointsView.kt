package tr.xip.scd.tensuu.ui.mypoints

import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.ui.common.adapter.PointsAdapter

interface MyPointsView : MvpView {
    fun setAdapter(value: PointsAdapter)
    fun getAdapter(): PointsAdapter?
}