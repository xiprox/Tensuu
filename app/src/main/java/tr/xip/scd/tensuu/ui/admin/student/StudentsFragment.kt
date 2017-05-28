package tr.xip.scd.tensuu.ui.admin.student

import android.content.Intent
import tr.xip.scd.tensuu.data.model.Student

class StudentsFragment : tr.xip.scd.tensuu.ui.students.StudentsFragment() {

    override fun startStudentActivity(student: Student, rangeStart: Long?, rangeEnd: Long?) {
        val intent = Intent(context, EditStudentActivity::class.java)
        intent.putExtra(EditStudentActivity.ARG_STUDENT_SSID, student.ssid)
        context.startActivity(intent)
    }

    companion object {
        fun new(): StudentsFragment {
            return StudentsFragment()
        }
    }
}