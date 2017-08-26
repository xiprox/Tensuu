package tr.xip.scd.tensuu.ui.mypoints

import com.hannesdorfmann.mosby3.mvp.MvpView
import java.util.*

interface BatchPointsAddView : MvpView {
    fun setAdapter(value: BatchPointsStudentsAdapter)
    fun getAdapter(): BatchPointsStudentsAdapter?
    fun setStudentsAdapter(value: StudentsAutoCompleteAdapter)
    fun getStudentsAdapter(): StudentsAutoCompleteAdapter?
    fun setReasonsAdapter(value: ReasonsAutoCompleteAdapter)
    fun getReasonsAdapter(): ReasonsAutoCompleteAdapter?

    fun setDate(cal: Calendar)
    fun setSearchClearVisible(value: Boolean = true)

    fun runOnUi(body: () -> Unit)

    fun die()
}