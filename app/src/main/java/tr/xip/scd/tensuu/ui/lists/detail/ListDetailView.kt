package tr.xip.scd.tensuu.ui.lists.detail

import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter

interface ListDetailView : MvpView {
    fun setName(value: String)
    fun setEditButtonShown(show: Boolean)
    fun setAdapter(value: StudentsAddingAdapter)
    fun getAdapter(): StudentsAddingAdapter?

    fun showRenameDialog()

    fun startListEditActivity(listName: String)

    fun die()
}