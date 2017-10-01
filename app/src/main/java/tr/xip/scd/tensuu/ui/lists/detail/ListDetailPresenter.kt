package tr.xip.scd.tensuu.ui.lists.detail

import android.annotation.SuppressLint
import android.view.MenuItem
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.*
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter

class ListDetailPresenter : RealmPresenter<ListDetailView>() {
    private var list: StudentList? = null

    fun loadWith(listName: String?, ownerEmail: String?) {
        list = realm.where(StudentList::class.java)
                .equalTo(StudentListFields.NAME, listName)
                .equalTo(StudentListFields.OWNER.EMAIL, ownerEmail)
                .findFirst()

        list?.let {
            view?.setName(it.name ?: "?")
            view?.setOwner(it.owner?.name)
            view?.setFabIcon(
                    if (it.students.size == 0) {
                        R.drawable.ic_add_white_24dp
                    } else {
                        R.drawable.ic_my_points_white_24dp
                    }
            )
            view?.setAdapter(StudentsAddingAdapter(it.students, false) { position ->
                realm.executeTransaction {
                    list?.students?.let { students ->
                        view?.getAdapter()?.let { adapter ->
                            val lastIndex = adapter.itemCount - 1
                            students.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, lastIndex)
                        }
                    }
                }
            })
        }
    }

    fun onPrepareOptionsMenu() {
        view?.setEditMenuItemVisible(Credentials.canModify || Credentials.isAdmin)
    }

    @SuppressLint("SimpleDateFormat")
    fun onOptionsItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.edit -> {
                view?.startListEditActivity(list!!.name!!)
            }
            R.id.delete -> {
                realm.executeTransaction {
                    list?.deleteFromRealm()
                }
                view?.die()
            }
        }
    }

    fun onRenameOkClicked(newName: String) {
        realm.executeTransaction {
            list?.name = newName
        }
        view?.setName(newName)
    }

    fun onFabClicked() {
        if (list?.name == null) return
        if (list?.students?.size ?: 0 == 0) {
            view?.startListEditActivity(list!!.name!!)
        } else {
            view?.startAddPointsActivity(list!!.students)
        }
    }
}