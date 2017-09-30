package tr.xip.scd.tensuu.ui.lists.add

import android.text.Editable
import io.realm.Case
import io.realm.RealmList
import tr.xip.scd.tensuu.data.model.*
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter
import tr.xip.scd.tensuu.ui.mypoints.StudentsAutoCompleteAdapter

class ListAddPresenter : RealmPresenter<ListAddView>() {
    private val students = RealmList<Student>()

    private var name: String? = null
    private var isPrivate = true

    fun init() {
        view?.setAdapter(StudentsAddingAdapter(students,
                removeClickedListener = {
                    onStudentRemoveClicked(it)
                }
        ))

        view?.setAutoCompleteAdapter(
                StudentsAutoCompleteAdapter(realm, realm.where(Student::class.java).findAll())
        )
    }

    fun onNameChanged(name: String) {
        this.name = if (name.isBlank()) null else name
    }

    fun onPrivateChanged(private: Boolean) {
        isPrivate = private
    }

    fun onDoneClicked() {
        realm.executeTransaction {
            val list = StudentList()
            list.name = name
            list.owner = it.where(User::class.java)
                    .equalTo(UserFields.EMAIL, Credentials.email).findFirst()
            list.public = !isPrivate
            list.students = students

            // Append a number to the name if a list with the same name from this user already exists
            val existing = realm.where(StudentList::class.java).equalTo(StudentListFields.NAME, name).findAll()
            if (existing?.size != 0) list.name = "${list.name} ${existing.size + 1}"

            it.copyToRealm(list)
        }
        view?.die()
    }

    fun onNewStudentSelected(student: Student) {
        if (!students.contains(student)) {
            students.add(student)
            view?.getAdapter()?.notifyItemInserted(students.indexOf(student))
        }
    }

    private fun onStudentRemoveClicked(position: Int) {
        val adapter = view?.getAdapter() ?: return
        val lastIndex = adapter.itemCount - 1
        adapter.data?.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, lastIndex)
    }

    fun onSearchTextChangedInstant(s: Editable?) {
        view?.setSearchClearVisible(s?.toString()?.isNotEmpty() ?: false)
    }

    fun onSearchTextChanged(s: Editable?) {
        val q = s?.toString() ?: return

        view?.runOnUi {
            view?.getAutoCompleteAdapter()?.updateData(
                    realm.where(Student::class.java)
                            .beginGroup()
                            .contains(StudentFields.FULL_NAME, q, Case.INSENSITIVE)
                            .or()
                            .contains(StudentFields.FULL_NAME_SIMPLIFIED, q, Case.INSENSITIVE)
                            .endGroup()
                            .findAll()
            )
        }
    }
}