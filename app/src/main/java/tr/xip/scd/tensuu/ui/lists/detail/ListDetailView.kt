package tr.xip.scd.tensuu.ui.lists.detail

import com.hannesdorfmann.mosby3.mvp.MvpView
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter

interface ListDetailView : MvpView {
    fun setName(value: String)
    fun setOwner(value: String?)
    fun setEditMenuItemVisible(show: Boolean)
    fun setAdapter(value: StudentsAddingAdapter)
    fun getAdapter(): StudentsAddingAdapter?

    fun showRenameDialog()

    fun startListEditActivity(listName: String)
    fun startAddPointsActivity(students: List<Student>)

    fun die()
}