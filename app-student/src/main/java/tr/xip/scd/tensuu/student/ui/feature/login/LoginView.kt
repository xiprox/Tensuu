package tr.xip.scd.tensuu.student.ui.feature.login

import com.hannesdorfmann.mosby3.mvp.MvpView

interface LoginView : MvpView {
    fun setIdError(error: String?)
    fun setPasswordError(error: String?)
    fun enableId(enable: Boolean = true)
    fun enablePassword(enable: Boolean = true)
    fun enableSignInButton(enable: Boolean = true)
    fun showProgress(show: Boolean = true)

    fun showSnackbar(text: String, actionText: String? = null, actionListener: (() -> Unit)? = null)
    fun startMainActivity()
    fun die()
}