package tr.xip.scd.tensuu.ui.admin.student

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.dialog_add_student.view.*
import tr.xip.scd.tensuu.App
import tr.xip.scd.tensuu.R
import tr.xip.scd.tensuu.data.model.Student
import tr.xip.scd.tensuu.util.ext.getLayoutInflater

object AddStudentDialog {

    fun new(realm: Realm, context: Context): AlertDialog? {
        @SuppressLint("InflateParams")
        val v = context.getLayoutInflater().inflate(R.layout.dialog_add_student, null, false)

        val dialog = AlertDialog.Builder(context)
                .setView(v)
                .setTitle(R.string.title_add_student)
                .setPositiveButton(R.string.action_add, null)
                .setNegativeButton(R.string.action_cancel, null)
                .create()

        dialog.setOnShowListener {
            val positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            positive.setOnClickListener {
                val ssid = v.ssid.text.toString()
                val firstName = v.firstName.text.toString()
                val lastName = v.lastName.text.toString()
                val grade = v.grade.text.toString()

                if (validateFields(v, ssid, firstName, lastName, grade)) {
                    val s = Student()
                    s.id = (s.ssid?.substring(2, s.ssid?.length ?: 1 - 1) ?: "-1").toInt()
                    s.ssid = ssid
                    s.firstName = firstName
                    s.lastName = lastName
                    s.grade = grade
                    s.fullName = "$firstName $lastName"
                    s.fullNameSimplified = s.fullName
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

                    realm.executeTransaction {
                        it.copyToRealmOrUpdate(s)
                    }

                    dialog.dismiss()
                }
            }
        }

        return dialog
    }

    private fun validateFields(v: View, ssid: String?, firstName: String?, lastName: String?, grade: String?): Boolean {
        /* SSID */
        if (ssid == null || ssid.trim().isEmpty()) {
            v.ssidLayout.error = App.context.getString(R.string.error_invalid_ssid)
            return false
        } else {
            v.ssidLayout.error = null
        }

        /* First name */
        if (firstName == null || firstName.trim().isEmpty()) {
            v.firstNameLayout.error = App.context.getString(R.string.error_invalid_first_name)
            return false
        } else {
            v.firstNameLayout.error = null
        }

        /* Last name */
        if (lastName == null || lastName.trim().isEmpty()) {
            v.lastNameLayout.error = App.context.getString(R.string.error_invalid_last_name)
            return false
        } else {
            v.lastNameLayout.error = null
        }

        /* Grade */
        if (grade == null || grade.trim().isEmpty()) {
            v.gradeLayout.error = App.context.getString(R.string.error_invalid_last_name)
            return false
        } else {
            v.gradeLayout.error = null
        }

        /* All iz wel */
        return true
    }
}