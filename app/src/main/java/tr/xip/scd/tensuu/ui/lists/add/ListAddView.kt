package tr.xip.scd.tensuu.ui.lists.add

import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter
import tr.xip.scd.tensuu.ui.mypoints.StudentsAutoCompleteAdapter

interface ListAddView : MvpView {
    fun setAdapter(value: StudentsAddingAdapter)
    fun getAdapter(): StudentsAddingAdapter?
    fun setAutoCompleteAdapter(value: StudentsAutoCompleteAdapter)
    fun getAutoCompleteAdapter(): StudentsAutoCompleteAdapter?

    fun setSearchClearVisible(value: Boolean = true)

    fun runOnUi(body: () -> Unit)

    fun die()
}