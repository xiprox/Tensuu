package tr.xip.scd.tensuu.ui.admin.student

import com.hannesdorfmann.mosby3.mvp.MvpView

interface EditStudentView : MvpView {
    fun setId(value: String?)
    fun setSsid(value: String?)
    fun setFirstName(value: String?)
    fun setLastName(value: String?)
    fun setGrade(value: String?)
    fun setFloor(value: Int?)
    fun showToast(text: String)
    fun die()
}

