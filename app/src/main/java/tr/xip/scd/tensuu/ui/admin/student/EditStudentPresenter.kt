package tr.xip.scd.tensuu.ui.admin.student

import android.os.Bundle
import android.text.Editable
import android.view.MenuItem
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.realm.model.Student
import tr.xip.scd.tensuu.realm.model.StudentFields
import tr.xip.scd.tensuu.ui.common.mvp.RealmPresenter

class EditStudentPresenter : RealmPresenter<EditStudentView>() {
    private var student: Student? = null

    fun loadWith(extras: Bundle?) {
        if (extras == null) {
            throw IllegalArgumentException("No extra passed to ${EditStudentPresenter::class.java.simpleName}")
        }

        student = realm.where(Student::class.java)
                .equalTo(StudentFields.SSID, extras.getString(EditStudentActivity.ARG_STUDENT_SSID))
                .findFirst()

        if (student != null) {
            view?.setSsid(student?.ssid)
            view?.setFirstName(student?.firstName)
            view?.setLastName(student?.lastName)
            view?.setGrade(student?.grade)
            view?.setFloor(student?.floor)
        } else {
            view?.showToast(App.context.getString(R.string.error_couldnt_find_student))
            view?.die()
        }
    }

    private fun recalcFullName(s: Student?) {
        s?.fullName = "${s?.firstName} ${s?.lastName}"
        s?.fullNameSimplified = s?.fullName
                ?.replace("Č", "C")
                ?.replace("Ć", "C")
                ?.replace("Ç", "C")
                ?.replace("ć", "c")
                ?.replace("č", "c")
                ?.replace("ç", "c")
                ?.replace("Ž", "Z")
                ?.replace("ž", "z")
                ?.replace("Đ", "D")
                ?.replace("đ", "d")
                ?.replace("Š", "S")
                ?.replace("š", "s")
    }

    fun onSsidChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            student?.ssid = s.toString()
        }
    }

    fun onFirstNameChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            student?.firstName = s.toString()
            recalcFullName(student)
        }
    }

    fun onLastNameChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            student?.lastName = s.toString()
            recalcFullName(student)
        }
    }

    fun onGradeChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            student?.grade = s.toString()
        }
    }

    fun onFloorChanged(s: Editable?) {
        if (s == null) return
        realm.executeTransaction {
            student?.floor = if (s.toString().trim().isEmpty()) {
                0
            } else {
                s.toString().toInt()
            }
        }
    }

    fun onOptionsMenuItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_delete -> realm.executeTransaction {
                student?.deleteFromRealm()
                if (!(student?.isValid ?: false)) {
                    view?.die()
                }
            }
        }
    }
}