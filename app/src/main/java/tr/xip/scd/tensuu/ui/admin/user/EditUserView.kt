package tr.xip.scd.tensuu.ui.admin.user

import com.hannesdorfmann.mosby3.mvp.MvpView

interface EditUserView : MvpView {
    fun setEmail(value: String?)
    fun setPassword(value: String?)
    fun setName(value: String?)
    fun setAdmin(value: Boolean)
    fun setCanModify(value: Boolean)
    fun showToast(text: String)
    fun die()
}