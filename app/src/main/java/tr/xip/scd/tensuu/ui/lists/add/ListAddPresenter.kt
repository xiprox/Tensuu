package tr.xip.scd.tensuu.ui.lists.add

import android.text.Editable
import io.realm.Case
import tr.xip.scd.tensuu.local.Credentials
import tr.xip.scd.tensuu.realm.model.*
import tr.xip.scd.tensuu.common.ui.mvp.RealmPresenter
import tr.xip.scd.tensuu.ui.lists.StudentsAddingAdapter
import tr.xip.scd.tensuu.ui.mypoints.StudentsAutoCompleteAdapter

class ListAddPresenter : RealmPresenter<ListAddView>() {
    private var list = StudentList()

    private var isEditMode = false

    fun init() {
        setAdapter()

        view?.setAutoCompleteAdapter(
                StudentsAutoCompleteAdapter(realm, realm.where(Student::class.java).findAll())
        )
    }

    fun loadList(listName: String) {
        isEditMode = true

        realm.where(StudentList::class.java).equalTo(StudentListFields.NAME, listName).findFirst()?.let {
            list = it
            setAdapter()
            view?.showExitButton(false)
            view?.setName(list.name ?: "")
            view?.setPrivate(!list.public)
        }
    }

    private fun setAdapter() {
        view?.setAdapter(StudentsAddingAdapter(list.students,
                removeClickedListener = {
                    onStudentRemoveClicked(it)
                }
        ))
    }

    fun onNameChanged(name: String) {
        view?.runOnUi {
            realm.executeTransaction {
                list.name = if (name.isBlank()) null else name
            }
        }
    }

    fun onPrivateChanged(private: Boolean) {
        view?.runOnUi {
            realm.executeTransaction {
                list.public = !private
            }
        }
    }

    fun onDoneClicked() {
        if (!isEditMode) {
            realm.executeTransaction {
                // Append a number to the name if a list with the same name already exists
                val existing = realm.where(StudentList::class.java).equalTo(StudentListFields.NAME, list.name).findAll()
                if (existing.size != 0) list.name = "${list.name} ${existing.size + 1}"

                list.owner = it.where(User::class.java)
                        .equalTo(UserFields.EMAIL, Credentials.email).findFirst()
                it.copyToRealm(list)
            }
        }

        view?.die()
    }

    fun onNewStudentSelected(student: Student) {
        if (!list.students.contains(student)) {
            view?.runOnUi {
                realm.executeTransaction {
                    list.students.add(student)
                }
            }
            view?.getAdapter()?.notifyItemInserted(list.students.indexOf(student))
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