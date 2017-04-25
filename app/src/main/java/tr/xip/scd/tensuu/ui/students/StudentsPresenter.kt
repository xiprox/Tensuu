package tr.xip.scd.tensuu.ui.students

import android.text.Editable
import io.realm.Case
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.data.model.StudentFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter

class StudentsPresenter : RealmPresenter<StudentsView>() {

    override fun attachView(view: StudentsView?) {
        super.attachView(view)
        init()
    }

    private fun init() {
        view?.setAdapter(
                StudentsAdapter(realm, realm.where(Student::class.java).findAll(), {
                    view?.onStudentClicked(it)
                })
        )
    }

    fun onStudentClicked(student: Student) {
        view?.startStudentActivity(student)
    }

    fun onSearchTextChangedInstant(s: Editable?) {
        view?.setSearchExitVisible(s?.toString()?.isNotEmpty() ?: false)
    }

    fun onSearchTextChanged(s: Editable?) {
        if (s == null) return

        val q = s.toString()

        view?.runOnUiThread {
            view?.getAdapter()?.updateData(
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